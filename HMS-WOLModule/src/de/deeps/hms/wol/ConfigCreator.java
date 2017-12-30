package de.deeps.hms.wol;

import java.util.Arrays;

import de.deeps.hms.ModuleConfig;
import de.deeps.hms.ModuleType;
import de.deeps.hms.json.JsonConverter;
import de.deeps.hms.network.NetworkConfig;
import de.deeps.hms.webserver.Command;
import de.deeps.hms.webserver.HttpConfig;
import de.deeps.hms.wol.parameters.SendWOLParameter;

/**
 * @author Deeps
 */

public class ConfigCreator {
	public static void main(String[] args) {
		ModuleConfig config = new ModuleConfig();
		config.setModuleType(ModuleType.WOL);
		config.setNetworkConfig(new NetworkConfig(6666, 0));
		config.setHttpConfig(
			new HttpConfig(2316, Arrays.asList(
				new Command("sendWOL", SendWOLParameter.class.getName(),
						JsonConverter
								.objectToJsonString(new SendWOLParameter())))));
		JsonConverter.writeObjectToJsonFile(config, "wol_config.json");
	}
}
