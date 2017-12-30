package de.deeps.hms.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

import de.deeps.hms.ModuleConfig;
import de.deeps.hms.json.JsonConverter;
import de.deeps.hms.network.NetworkMessage.ConfigAnswer;
import de.deeps.hms.network.NetworkMessage.IsRunningAnswer;
import de.deeps.hms.webserver.WebserverModule;

/**
 * @author Deeps
 */

public abstract class NetworkClientModule extends WebserverModule {

	private final int TIMEOUT_IN_SEC = 5;

	private InetAddress networkServerAddress;
	private TCPServer tcpServer;
	private UDPSender udpSender;

	public NetworkClientModule(ModuleConfig config) throws IOException {
		super(config);
		sendStartUpMessage();
		startTimeOut();
	}

	private void startTimeOut() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(TIMEOUT_IN_SEC * 1000L);
					if (tcpServer.getConnectedClients() == 0) {
						System.out.println(
							config.getModuleType().toString()
									+ " No Network-Server found (TIMEOUT).");
						shutdownModule();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	protected void initialize() throws IOException {
		super.initialize();
		initUDPSender();
		initTCPServer();
	}

	private void initUDPSender() throws SocketException {
		udpSender = new UDPSender();
	}

	private void initTCPServer() throws IOException {
		tcpServer = new TCPServer(
				config.getNetworkConfig().getTcpServerPort()) {

			@Override
			protected void newClientConnected(TCPClient newClient) {
				networkServerAddress = newClient.getTargetInetAddress();
			}

			@Override
			public void handleMessage(String message, InetAddress inetAddress,
					int port) {
				handleNetworkMessage(
					JsonConverter
							.jsonStringToObject(message, NetworkMessage.class),
					inetAddress,
					port);
			}

			@Override
			protected void clientDisconnected(TCPClient client) {
				if (getConnectedClients() == 0) {
					shutdownModule();
				}
			}
		};
	}

	protected void handleNetworkMessage(NetworkMessage message,
			InetAddress inetAddress, int port) {
		switch (message.getType()) {
			case IS_RUNNING_REQUEST:
				tcpServer
						.sendMessage(
							JsonConverter.objectToJsonString(
								new IsRunningAnswer(isRunning())),
							inetAddress,
							port);
				break;
			case RESTART:
				restart();
				break;
			case START:
				startModule();
				break;
			case STOP:
				stopModule();
				break;
			case CONFIG_REQUEST:
				tcpServer.sendMessage(
					JsonConverter.objectToJsonString(new ConfigAnswer(config)),
					inetAddress,
					port);
				break;
			default:
				break;
		}
	}

	private void restart() {
		stopModule();
		startModule();
	}

	private void sendStartUpMessage() {
		udpSender.sendMessage(
			JsonConverter.objectToJsonString(
				new NetworkMessage.StartUp(tcpServer.getPort())),
			NetworkUtils.getLocalAddress(),
			config.getNetworkConfig().getPortOfAssociatedNetworkServer());

	}

	@Override
	protected void shutdown() {
		udpSender.shutdown();
		tcpServer.shutdown();
	}

	public InetAddress getNetworkServerAddress() {
		return networkServerAddress;
	}
}
