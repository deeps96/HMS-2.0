#!/usr/bin/env python

import atexit
from BaseHTTPServer import BaseHTTPRequestHandler, HTTPServer
import bluetooth
import itertools
import json
import requests
import SocketServer
import thread
import time

##### BT Discovery #####

DELAY = 10
TIMEOUT = 5
registeredListeners = {}
deviceStates = {}
onDiscoveryChangeFunctions = {}

def bluetoothDiscoveryLoop(threadName, deviceBTMac):
	while deviceBTMac in registeredListeners:
		result = bluetooth.lookup_name(deviceBTMac, timeout = TIMEOUT) #this results into loosing network connection to pi
		isOnline = (result != None)

		if deviceStates[deviceBTMac] != isOnline:
			deviceStates[deviceBTMac] = isOnline
			onDiscoveryChangeFunctions[deviceBTMac](deviceBTMac, isOnline, registeredListeners[deviceBTMac])

		time.sleep(DELAY) 

		
def initializeBTModule():
	pass
	
def registerListener(deviceBTMac, replyUrl, onDiscoveryChange):
	global registeredListeners
	registeredListeners[deviceBTMac] = replyUrl
	deviceStates[deviceBTMac] = False
	onDiscoveryChangeFunctions[deviceBTMac] = onDiscoveryChange
	try:
		thread.start_new_thread( bluetoothDiscoveryLoop, ("Thread-1", deviceBTMac, ) )
	except:
		print "Error: unable to start thread"
	
def unregisterListener(deviceBTMac, replyUrl):
	global registeredListeners, deviceStates
	if deviceBTMac in registeredListeners: 
		del registeredListeners[deviceBTMac]
		del deviceStates[deviceBTMac]
		del onDiscoveryChangeFunctions[deviceBTMac]
		
def currentStatus(deviceBTMac):
	if deviceBTMac in deviceStates:
		return deviceStates[deviceBTMac]
	else:
		return False

##### WebServer #####

HTTP_PORT = 2201
httpd = 0
headers = {'Content-Type': 'application/json'}

def initializeWebserver():
	global httpd
	serverAddress = ('', HTTP_PORT)
	httpd = HTTPServer(serverAddress, WebServer)
	print 'BTModule started on port', HTTP_PORT
	print 'STARTPROCESS COMPLETE'
	httpd.serve_forever()
		
def shutdownWebserver():
	httpd.server_close()

class WebServer(BaseHTTPRequestHandler):
		
	def do_POST(request):
		commandURI = request.path[1:]
		reply = None
		if (commandURI == 'ping'):
			reply = '{ "isOnline" : true }'
		elif (commandURI == 'registerListenerForDevice'):
			reply = registerListenerForDevice(request)
		elif (commandURI == 'unregisterListenerForDevice'):
			reply = unregisterListenerForDevice(request)
		elif (commandURI == 'currentStatusOfDevice'):
			reply = currentStatusOfDevice(request)
		
		if (reply != None):
			request.send_response(200)
			request.send_header('Content-type', 'application/json')
			request.end_headers()
			request.wfile.write(reply)

def registerListenerForDevice(request):
	jsonData = getJsonFromRequest(request)
	if (not 'deviceBTMac' in jsonData or not 'responseUrl' in jsonData):
		return None
	registerListener(jsonData['deviceBTMac'], jsonData['responseUrl'], discoveryChanged)
	return ""
	
def unregisterListenerForDevice(request):
	jsonData = getJsonFromRequest(request)
	if (not 'deviceBTMac' in jsonData or not 'responseUrl' in jsonData):
		return None
	unregisterListener(jsonData['deviceBTMac'], jsonData['responseUrl'])
	return ""
	
def currentStatusOfDevice(request):
	jsonData = getJsonFromRequest(request)
	if (not 'deviceBTMac' in jsonData):
		return None
	return json.dumps({
		"deviceBTMac": jsonData['deviceBTMac'],
		"status": currentStatus(jsonData['deviceBTMac'])
	})

def getJsonFromRequest(request):
	content_length = int(request.headers['Content-Length'])
	post_data = request.rfile.read(content_length)
	return json.loads(post_data)

def discoveryChanged(deviceBTMac, newStatus, replyUrl):
	data = {
		"deviceBTMac": deviceBTMac,
		"status": newStatus
	}
	try:
		thread.start_new_thread( requests.post, (replyUrl, headers, data) )
	except:
		print "Error: unable to start thread"

##### Lifecycle #####

isShutdown = False
	
def initialize():
	initializeBTModule()
	initializeWebserver()

@atexit.register
def shutdown():
	global isShutdown
	if isShutdown:
		pass
	isShutdown = True
	shutdownWebserver()
	print 'BTModule shutdown'
	
if __name__ == '__main__':
	initialize()