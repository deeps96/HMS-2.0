package de.deeps.hms.audioplayer;

import java.util.Arrays;

import de.deeps.hms.ModuleConfig;
import de.deeps.hms.ModuleType;
import de.deeps.hms.audioplayer.parameters.PlayAudioParameter;
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
		config.setModuleType(ModuleType.AUDIO_PLAYER);
		config.setNetworkConfig(new NetworkConfig(6666, 0));
		config.setHttpConfig(
			new HttpConfig(1214, Arrays.asList(
				new Command("playAudio", PlayAudioParameter.class.getName(),
						JsonConverter
								.objectToJsonString(new PlayAudioParameter())),
				new Command("listAudioFiles"),
				new Command("stopPlayingAudio"))));
		JsonConverter.writeObjectToJsonFile(config, "audioplayer_config.json");
	}
}
