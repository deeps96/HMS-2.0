package de.deeps.hms.pcmanager;

import java.util.Arrays;

import de.deeps.hms.ModuleConfig;
import de.deeps.hms.ModuleType;
import de.deeps.hms.json.JsonConverter;
import de.deeps.hms.network.NetworkConfig;
import de.deeps.hms.pcmanager.parameters.ChangeOutputDeviceParameter;
import de.deeps.hms.pcmanager.parameters.SetVolumeParameter;
import de.deeps.hms.pcmanager.parameters.StartProgramParameter;
import de.deeps.hms.webserver.Command;
import de.deeps.hms.webserver.HttpConfig;

/**
 * @author Deeps
 */

public class ConfigCreator {
	public static void main(String[] args) {
		ModuleConfig config = new ModuleConfig();
		config.setModuleType(ModuleType.PC_MANAGER);
		config.setNetworkConfig(new NetworkConfig(6666, 0));
		config.setHttpConfig(
			new HttpConfig(1631, Arrays.asList(
				new Command("listPrograms"),
				new Command("startProgram",
						StartProgramParameter.class.getName(),
						JsonConverter.objectToJsonString(
							new StartProgramParameter())),
				new Command("shutdown"),
				new Command("standby"),
				new Command("setVolume", SetVolumeParameter.class.getName(),
						JsonConverter
								.objectToJsonString(new SetVolumeParameter())),
				new Command("changeOutputDevice",
						ChangeOutputDeviceParameter.class.getName(),
						JsonConverter.objectToJsonString(
							new ChangeOutputDeviceParameter())))));
		JsonConverter.writeObjectToJsonFile(config, "pcmanager_config.json");
	}
}
