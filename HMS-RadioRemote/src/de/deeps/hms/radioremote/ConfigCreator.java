package de.deeps.hms.radioremote;

import java.util.Arrays;

import de.deeps.hms.ModuleConfig;
import de.deeps.hms.ModuleType;
import de.deeps.hms.json.JsonConverter;
import de.deeps.hms.network.NetworkConfig;
import de.deeps.hms.radioremote.parameters.ListButtonsParameter;
import de.deeps.hms.radioremote.parameters.PressButtonParameter;
import de.deeps.hms.webserver.Command;
import de.deeps.hms.webserver.HttpConfig;

/**
 * @author Deeps
 */

public class ConfigCreator {
	public static void main(String[] args) {
		ModuleConfig config = new ModuleConfig();
		config.setModuleType(ModuleType.RADIO_REMOTE);
		config.setNetworkConfig(new NetworkConfig(6666, 0));
		config.setHttpConfig(
			new HttpConfig(1814, Arrays.asList(
				new Command("pressButton", PressButtonParameter.class.getName(),
						JsonConverter.objectToJsonString(
							new PressButtonParameter())),
				new Command("listRemotes"),
				new Command("listButtons", ListButtonsParameter.class.getName(),
						JsonConverter.objectToJsonString(
							new ListButtonsParameter())))));
		JsonConverter.writeObjectToJsonFile(config, "radioremote_config.json");
	}
}
