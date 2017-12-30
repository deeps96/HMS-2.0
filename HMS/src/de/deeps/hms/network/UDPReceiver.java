package de.deeps.hms.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * @author Deeps
 */

public abstract class UDPReceiver implements Runnable {

	private final static int BLOCK_SIZE = 1024;

	private DatagramSocket receiverSocket;

	public UDPReceiver(int receiverPort) throws SocketException {
		receiverSocket = new DatagramSocket(receiverPort);
		new Thread(this).start();
	}

	@Override
	public void run() {
		while (!receiverSocket.isClosed()) {
			try {
				DatagramPacket receivedPacket = new DatagramPacket(
						new byte[BLOCK_SIZE], BLOCK_SIZE);
				receiverSocket.receive(receivedPacket);
				handleMessage(
					new String(receivedPacket.getData()),
					receivedPacket.getAddress(),
					receivedPacket.getPort());
			} catch (IOException e) {
			}
		}
	}

	public void shutdown() {
		receiverSocket.close();
	}

	// abstract
	public abstract void handleMessage(String message, InetAddress receivedFrom,
			int port);
}
