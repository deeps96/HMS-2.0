package de.deeps.hms.ledboard;

import java.util.Arrays;

import de.deeps.hms.ModuleConfig;
import de.deeps.hms.ModuleType;
import de.deeps.hms.json.JsonConverter;
import de.deeps.hms.ledboard.parameters.SetBrightnessParameter;
import de.deeps.hms.ledboard.parameters.ShowAnimatedContentParameter;
import de.deeps.hms.ledboard.parameters.ShowStaticContentParameter;
import de.deeps.hms.network.NetworkConfig;
import de.deeps.hms.webserver.Command;
import de.deeps.hms.webserver.HttpConfig;

/**
 * @author Deeps
 */

public class ConfigCreator {
	public static void main(String[] args) {
		ModuleConfig config = new ModuleConfig();
		config.setModuleType(ModuleType.LED_BOARD);
		config.setNetworkConfig(new NetworkConfig(6666, 0));
		config.setHttpConfig(
			new HttpConfig(1254,
					Arrays.asList(
						new Command("clear"),
						new Command("setBrightness",
								SetBrightnessParameter.class.getName(),
								JsonConverter.objectToJsonString(
									new SetBrightnessParameter())),
						new Command("showStaticContent",
								ShowStaticContentParameter.class.getName(),
								JsonConverter.objectToJsonString(
									new ShowStaticContentParameter())),
						new Command("showAnimatedContent",
								ShowAnimatedContentParameter.class.getName(),
								JsonConverter.objectToJsonString(
									new ShowAnimatedContentParameter())))));
		JsonConverter.writeObjectToJsonFile(config, "ledboard_config.json");
	}
}
