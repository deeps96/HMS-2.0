package de.deeps.hms.launcher;

import java.util.Arrays;

import de.deeps.hms.ModuleConfig;
import de.deeps.hms.ModuleType;
import de.deeps.hms.json.JsonConverter;
import de.deeps.hms.launcher.parameters.LaunchModuleParameter;
import de.deeps.hms.launcher.parameters.ShutdownModuleParameter;
import de.deeps.hms.network.NetworkConfig;
import de.deeps.hms.webserver.Command;
import de.deeps.hms.webserver.HttpConfig;

/**
 * @author Deeps
 */

public class ConfigCreator {
	public static void main(String[] args) {
		ModuleConfig config = new ModuleConfig();
		config.setModuleType(ModuleType.LAUNCHER);
		config.setNetworkConfig(new NetworkConfig(6666, 0));
		config.setHttpConfig(
			new HttpConfig(1212,
					Arrays.asList(
						new Command("launchModule",
								LaunchModuleParameter.class.getName(),
								JsonConverter.objectToJsonString(
									new LaunchModuleParameter())),
						new Command("listModules"),
						new Command("launchAll"),
						new Command("shutdownAll"),
						new Command("shutdownModule",
								ShutdownModuleParameter.class.getName(),
								JsonConverter.objectToJsonString(
									new ShutdownModuleParameter())))));
		JsonConverter.writeObjectToJsonFile(config, "launcher_config.json");

	}
}
