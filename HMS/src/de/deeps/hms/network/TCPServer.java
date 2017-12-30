package de.deeps.hms.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Deeps
 */

public abstract class TCPServer implements Runnable {

	private List<TCPClient> connectedClients;
	private ServerSocket serverSocket;

	public TCPServer(int serverPort) throws IOException {
		serverSocket = new ServerSocket(serverPort);
		initialize();
		new Thread(this).start();
	}

	private void initialize() throws IOException {
		connectedClients = new LinkedList<>();
	}

	public boolean sendMessage(String message, InetAddress targetAddress,
			int targetPort) {
		for (TCPClient client : connectedClients) {
			if (client.getTargetInetAddress().equals(targetAddress)
					&& client.getTargetPort() == targetPort) {
				client.sendMessage(message, targetAddress, targetPort);
				return true;
			}
		}
		return false;
	}

	public void shutdown() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (!serverSocket.isClosed()) {
			try {
				TCPClient newClient = new TCPClient(serverSocket.accept()) {

					@Override
					public void handleMessage(String message,
							InetAddress receivedFrom, int port) {
						TCPServer.this
								.handleMessage(message, receivedFrom, port);
					}

					@Override
					public void disconnected() {
						connectedClients.remove(this);
						TCPServer.this.clientDisconnected(this);
					}
				};
				connectedClients.add(newClient);
				newClientConnected(newClient);
			} catch (IOException e) {
			}
		}
	}

	public int getConnectedClients() {
		return connectedClients.size();
	}

	public int getPort() {
		return serverSocket.getLocalPort();
	}

	// abstract
	public abstract void handleMessage(String message, InetAddress receivedFrom,
			int port);

	protected abstract void newClientConnected(TCPClient newClient);

	protected abstract void clientDisconnected(TCPClient client);

}
