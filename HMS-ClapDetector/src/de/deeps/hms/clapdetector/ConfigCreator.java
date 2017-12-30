package de.deeps.hms.clapdetector;

import java.util.Arrays;

import de.deeps.hms.ModuleConfig;
import de.deeps.hms.ModuleType;
import de.deeps.hms.clapdetector.parameters.RegisterListenerParameter;
import de.deeps.hms.clapdetector.parameters.UnregisterListenerParameter;
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
		config.setModuleType(ModuleType.CLAP_DETECTOR);
		config.setNetworkConfig(new NetworkConfig(6666, 0));
		config.setHttpConfig(
			new HttpConfig(3121,
					Arrays.asList(
						new Command("registerListener",
								RegisterListenerParameter.class.getName(),
								JsonConverter.objectToJsonString(
									new RegisterListenerParameter())),
						new Command("unregisterListener",
								UnregisterListenerParameter.class.getName(),
								JsonConverter.objectToJsonString(
									new UnregisterListenerParameter())),
						new Command("stop"),
						new Command("start"))));
		JsonConverter.writeObjectToJsonFile(config, "clapdetector_config.json");
	}
}
