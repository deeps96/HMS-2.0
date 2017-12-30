package de.deeps.hms;

import java.util.Arrays;

import de.deeps.hms.json.JsonConverter;
import de.deeps.hms.network.NetworkConfig;
import de.deeps.hms.network.parameters.ShutdownModuleParameter;
import de.deeps.hms.webserver.Command;
import de.deeps.hms.webserver.HttpConfig;

/**
 * @author Deeps
 */

public class ConfigCreator {

	public static void main(String[] args) {
		ModuleConfig config = new ModuleConfig();
		config.setModuleType(ModuleType.NETWORK_SERVER);
		config.setNetworkConfig(new NetworkConfig(6666, 0));
		config.setHttpConfig(
			new HttpConfig(1451,
					Arrays.asList(
						new Command("listRunningModules"),
						new Command("listStandbyModules"),
						new Command("shutdownModule",
								ShutdownModuleParameter.class.getName(),
								JsonConverter.objectToJsonString(
									new ShutdownModuleParameter())))));
		JsonConverter.writeObjectToJsonFile(config, "network_server.json");
	}

}
