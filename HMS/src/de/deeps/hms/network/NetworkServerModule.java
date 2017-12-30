package de.deeps.hms.network;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import de.deeps.hms.ModuleConfig;
import de.deeps.hms.json.JsonConverter;
import de.deeps.hms.network.NetworkMessage.ConfigAnswer;
import de.deeps.hms.network.NetworkMessage.ConfigRequest;
import de.deeps.hms.network.NetworkMessage.IsRunningAnswer;
import de.deeps.hms.network.NetworkMessage.IsRunningRequest;
import de.deeps.hms.network.NetworkMessage.StartRequest;
import de.deeps.hms.network.NetworkMessage.StartUp;
import de.deeps.hms.network.parameters.ShutdownModuleParameter;
import de.deeps.hms.webserver.CommandExecutionInterface;
import de.deeps.hms.webserver.WebserverModule;

/**
 * @author Deeps
 */

public class NetworkServerModule extends WebserverModule {

	private UDPReceiver udpReceiver;
	private HashMap<TCPClient, Boolean> clientsStatus;
	private HashMap<TCPClient, ModuleConfig> clientModuleConfigs;

	public NetworkServerModule(String configFilePath) throws IOException {
		super(JsonConverter.jsonFileToObject(
			new File(configFilePath),
			ModuleConfig.class));
		startModule();
	}

	@Override
	protected void initialize() throws IOException {
		super.initialize();
		clientsStatus = new HashMap<>();
		clientModuleConfigs = new HashMap<>();
		initializeUDPReceiver();
	}

	private void initializeUDPReceiver() throws SocketException {
		udpReceiver = new UDPReceiver(
				config.getNetworkConfig().getPortOfAssociatedNetworkServer()) {
			@Override
			public void handleMessage(String message, InetAddress receivedFrom,
					int port) {
				NetworkMessage networkMessage = JsonConverter
						.jsonStringToObject(message, NetworkMessage.class);
				if (networkMessage.getType()
						.equals(NetworkMessage.Type.STARTUP)) {
					connectToModule(
						receivedFrom,
						((StartUp) networkMessage).getTcpPort());
				}
			}
		};
	}

	protected void connectToModule(InetAddress receivedFrom, int tcpPort) {
		try {
			TCPClient newClient = new TCPClient(receivedFrom, tcpPort) {
				@Override
				public void handleMessage(String message,
						InetAddress receivedFrom, int port) {
					NetworkMessage networkMessage = JsonConverter
							.jsonStringToObject(message, NetworkMessage.class);
					switch (networkMessage.getType()) {
						case IS_RUNNING_ANSWER:
							clientsStatus.put(
								this,
								((IsRunningAnswer) networkMessage).isRunning());
							break;
						case CONFIG_ANSWER:
							clientModuleConfigs.put(
								this,
								((ConfigAnswer) networkMessage).getConfig());
							break;
						default:
					}
				}

				@Override
				public void disconnected() {
					clientsStatus.remove(this);
					clientModuleConfigs.remove(this);
				}
			};
			clientsStatus.put(newClient, false);
			newClient.sendMessage(
				JsonConverter.objectToJsonString(new ConfigRequest()),
				receivedFrom,
				tcpPort);
			newClient.sendMessage(
				JsonConverter.objectToJsonString(new StartRequest()),
				receivedFrom,
				tcpPort);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void shutdown() {
		udpReceiver.shutdown();
		for (TCPClient client : clientsStatus.keySet()) {
			client.shutdown();
		}
	}

	@Override
	protected void fillMethodMap() {
		addListRunningModulesMethod();
		addListStandbyModulesMethod();
		addShutdownModuleMethod();
	}

	private void addShutdownModuleMethod() {
		methodMap.put("shutdownModule", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				ShutdownModuleParameter parameter = (ShutdownModuleParameter) rawParameter;
				for (Entry<TCPClient, ModuleConfig> modules : clientModuleConfigs
						.entrySet()) {
					if (modules.getKey().getTargetInetAddress().getHostAddress()
							.equals(parameter.getIpAddress())
							&& modules.getValue().getModuleType().toString()
									.equals(parameter.getModuleID())) {
						modules.getKey().shutdown();
					}
				}
				return null;
			}
		});

	}

	private void updateAvailability() {
		for (TCPClient client : clientsStatus.keySet()) {
			client.sendMessage(
				JsonConverter.objectToJsonString(new IsRunningRequest()),
				client.getTargetInetAddress(),
				client.getTargetPort());
		}
		try {
			Thread.sleep(100L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void addListStandbyModulesMethod() {
		methodMap.put("listStandbyModules", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				updateAvailability();
				return JsonConverter
						.objectToJsonString(getConfigsByClientState(false));
			}
		});
	}

	private void addListRunningModulesMethod() {
		methodMap.put("listRunningModules", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				updateAvailability();
				return JsonConverter
						.objectToJsonString(getConfigsByClientState(true));
			}
		});
	}

	private List<ModuleConfig> getConfigsByClientState(
			boolean isRunningClient) {
		List<ModuleConfig> standbyModules = new LinkedList<>();
		for (Entry<TCPClient, Boolean> entry : clientsStatus.entrySet()) {
			if (entry.getValue() == isRunningClient) {
				standbyModules.add(clientModuleConfigs.get(entry.getKey()));
			}
		}
		return standbyModules;
	}

	public static void main(String[] args) throws IOException {
		new NetworkServerModule("network_server.json");
	}

}
