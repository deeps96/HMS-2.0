package de.deeps.hms.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author Deeps
 */

public abstract class TCPClient implements Runnable {

	private BufferedReader reader;
	private PrintWriter writer;
	private Socket socket;

	public TCPClient(InetAddress targetAddress, int targetPort)
			throws IOException {
		this(new Socket(targetAddress, targetPort));
	}

	public TCPClient(Socket clientSocket) throws IOException {
		this.socket = clientSocket;
		initialize();
		new Thread(this).start();
	}

	private void initialize() throws IOException {
		reader = new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
		writer = new PrintWriter(socket.getOutputStream());
	}

	@Override
	public void run() {
		try {
			while (!socket.isClosed()) {
				String message = reader.readLine();
				if (message == null) {
					break;
				}
				handleMessage(
					message,
					socket.getInetAddress(),
					socket.getPort());
			}
		} catch (IOException e) {
		}
		shutdown();
	}

	public boolean sendMessage(String message, InetAddress targetAddress,
			int targetPort) {
		if (getTargetInetAddress().equals(targetAddress)
				&& getTargetPort() == targetPort) {
			writer.println(message);
			writer.flush();
			return true;
		}
		return false;
	}

	public void shutdown() {
		try {
			writer.close();
			reader.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		disconnected();
	}

	// abstract
	public abstract void handleMessage(String message, InetAddress receivedFrom,
			int port);

	public abstract void disconnected();

	// Getter
	public InetAddress getTargetInetAddress() {
		return socket.getInetAddress();
	}

	public int getTargetPort() {
		return socket.getPort();
	}
}
