package de.deeps.hms.voicerecognition;

import java.util.Arrays;

import de.deeps.hms.ModuleConfig;
import de.deeps.hms.ModuleType;
import de.deeps.hms.json.JsonConverter;
import de.deeps.hms.network.NetworkConfig;
import de.deeps.hms.voicerecognition.parameters.VoiceRecognitionRequest;
import de.deeps.hms.webserver.Command;
import de.deeps.hms.webserver.HttpConfig;

/**
 * @author Deeps
 */

public class ConfigCreator {
	public static void main(String[] args) {
		ModuleConfig config = new ModuleConfig();
		config.setModuleType(ModuleType.VOICE_RECOGNITION);
		config.setNetworkConfig(new NetworkConfig(6666, 0));
		config.setHttpConfig(
			new HttpConfig(2115,
					Arrays.asList(
						new Command("voiceRecognition",
								VoiceRecognitionRequest.class.getName(),
								JsonConverter.objectToJsonString(
									new VoiceRecognitionRequest())))));
		JsonConverter
				.writeObjectToJsonFile(config, "voicerecognition_config.json");
	}
}
