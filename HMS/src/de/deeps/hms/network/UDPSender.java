package de.deeps.hms.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * @author Deeps
 */

public class UDPSender {

	private DatagramSocket senderSocket;

	public UDPSender() throws SocketException {
		initSenderSocket();
	}

	private void initSenderSocket() throws SocketException {
		senderSocket = new DatagramSocket();
	}

	public boolean sendMessage(String message, InetAddress targetAddress,
			int targetPort) {
		return sendMessage(message.getBytes(), targetAddress, targetPort);
	}

	public boolean sendMessage(byte[] rawMessage, InetAddress targetAddress,
			int targetPort) {
		if (targetAddress.equals(NetworkUtils.getBroadcastAddress())) {
			try {
				senderSocket.setBroadcast(true);
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
		DatagramPacket packet = new DatagramPacket(rawMessage,
				rawMessage.length, targetAddress, targetPort);
		try {
			senderSocket.send(packet);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void shutdown() {
		senderSocket.close();
	}

}
