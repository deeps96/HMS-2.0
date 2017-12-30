package de.deeps.hms.delay;

import java.util.Arrays;

import de.deeps.hms.ModuleConfig;
import de.deeps.hms.ModuleType;
import de.deeps.hms.delay.parameters.DelayParameter;
import de.deeps.hms.delay.parameters.StopDelayParameter;
import de.deeps.hms.delay.parameters.TimerParameter;
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
		config.setModuleType(ModuleType.DELAY);
		config.setNetworkConfig(new NetworkConfig(6666, 0));
		config.setHttpConfig(
			new HttpConfig(4512,
					Arrays.asList(
						new Command("delay", DelayParameter.class.getName(),
								JsonConverter.objectToJsonString(
									new DelayParameter())),
						new Command("timer", TimerParameter.class.getName(),
								JsonConverter.objectToJsonString(
									new TimerParameter())),
						new Command("stopDelay",
								StopDelayParameter.class.getName(),
								JsonConverter.objectToJsonString(
									new StopDelayParameter())))));
		JsonConverter.writeObjectToJsonFile(config, "delay_config.json");
	}
}
