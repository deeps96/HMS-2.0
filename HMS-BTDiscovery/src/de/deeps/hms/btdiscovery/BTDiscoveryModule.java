package de.deeps.hms.btdiscovery;

import java.io.IOException;

import de.deeps.hms.FinalModule;
import de.deeps.hms.btdiscovery.core.BTModuleWrapper;
import de.deeps.hms.btdiscovery.core.BluetoothConfig;
import de.deeps.hms.btdiscovery.parameters.CurrentStatusParameter;
import de.deeps.hms.btdiscovery.parameters.IsOnlineAnswer;
import de.deeps.hms.btdiscovery.parameters.RegisterListenerParameter;
import de.deeps.hms.btdiscovery.parameters.UnregisterListenerParameter;
import de.deeps.hms.json.JsonConverter;
import de.deeps.hms.webserver.CommandExecutionInterface;

/**
 * @author Deeps
 */

public class BTDiscoveryModule extends FinalModule {

	private final static String MODULE_CONFIG = "btdiscovery_config.json",
			BLUETOOTH_CONFIG = "bluetooth_config.json";

	private BluetoothConfig bluetoothConfig;
	private BTModuleWrapper btModule;

	public BTDiscoveryModule() throws IOException {
		super(MODULE_CONFIG);
	}

	@Override
	protected void initialize() throws IOException {
		loadBluetoothConfig();
		btModule = new BTModuleWrapper(bluetoothConfig.getBtModuleAddress());
		btModule.start();
		super.initialize();
	}

	private void loadBluetoothConfig() {
		bluetoothConfig = JsonConverter
				.jsonFileToObject(BLUETOOTH_CONFIG, BluetoothConfig.class);
	}

	@Override
	protected void stop() {
		btModule.stop();
		super.stop();
	}

	@Override
	protected void fillMethodMap() {
		addCurrentStatusMethod();
		addRegisterListenerMethod();
		addUnregisterListenerMethod();
	}

	@Override
	protected boolean isRunning() {
		String rawResponse = btModule.isOnline();
		if (rawResponse == null) {
			return false;
		}
		IsOnlineAnswer response = JsonConverter
				.jsonStringToObject(rawResponse, IsOnlineAnswer.class);
		return super.isRunning() && response.isOnline();
	}

	private void addUnregisterListenerMethod() {
		methodMap.put("unregisterListener", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				UnregisterListenerParameter parameter = (UnregisterListenerParameter) rawParameter;
				return btModule.unregisterDeviceListener(parameter);
			}
		});
	}

	private void addRegisterListenerMethod() {
		methodMap.put("registerListener", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				RegisterListenerParameter parameter = (RegisterListenerParameter) rawParameter;
				return btModule.registerDeviceListener(parameter);
			}
		});
	}

	private void addCurrentStatusMethod() {
		methodMap.put("currentStatus", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				CurrentStatusParameter parameter = (CurrentStatusParameter) rawParameter;
				return btModule.currentStatus(parameter);
			}
		});
	}

	public static void main(String[] args) throws IOException {
		new BTDiscoveryModule();
	}

}
