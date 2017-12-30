package de.deeps.hms.btdiscovery;

import java.util.Arrays;

import de.deeps.hms.ModuleConfig;
import de.deeps.hms.ModuleType;
import de.deeps.hms.btdiscovery.parameters.CurrentStatusParameter;
import de.deeps.hms.btdiscovery.parameters.RegisterListenerParameter;
import de.deeps.hms.btdiscovery.parameters.UnregisterListenerParameter;
import de.deeps.hms.json.JsonConverter;
import de.deeps.hms.network.NetworkConfig;
import de.deeps.hms.webserver.Command;
import de.deeps.hms.webserver.HttpConfig;

/**
 * @author Deeps
 */

public class ConfigCreator {
	public static void main(String[] args) {
		ModuleConfig config = new ModuleConfig();
		config.setModuleType(ModuleType.BT_DISCOVERY);
		config.setNetworkConfig(new NetworkConfig(6666, 0));
		config.setHttpConfig(
			new HttpConfig(2204,
					Arrays.asList(
						new Command("registerListener",
								RegisterListenerParameter.class.getName(),
								JsonConverter.objectToJsonString(
									new RegisterListenerParameter())),
						new Command("unregisterListener",
								UnregisterListenerParameter.class.getName(),
								JsonConverter.objectToJsonString(
									new UnregisterListenerParameter())),
						new Command("currentStatus",
								CurrentStatusParameter.class.getName(),
								JsonConverter.objectToJsonString(
									new CurrentStatusParameter())))));
		JsonConverter.writeObjectToJsonFile(config, "btdiscovery_config.json");
	}
}
