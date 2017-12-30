package de.deeps.hms.voicerecognition;

import java.io.IOException;

import de.deeps.hms.FinalModule;
import de.deeps.hms.json.JsonConverter;
import de.deeps.hms.voicerecognition.core.VoiceConfig;
import de.deeps.hms.voicerecognition.core.VoiceRecognition;
import de.deeps.hms.voicerecognition.parameters.VoiceRecognitionRequest;
import de.deeps.hms.webserver.CommandExecutionInterface;

/**
 * @author Deeps
 */

public class VoiceRecognitionModule extends FinalModule {

	private final static String MODULE_CONFIG = "voicerecognition_config.json",
			VOICE_CONFIG = "voice_config.json";

	private VoiceConfig voiceConfig;
	private VoiceRecognition voiceRecognition;

	public VoiceRecognitionModule() throws IOException {
		super(MODULE_CONFIG);
	}

	@Override
	protected void initialize() throws IOException {
		loadVoiceConfig();
		voiceRecognition = new VoiceRecognition(
				voiceConfig.getNumberOfResponses(), voiceConfig.getApiKey(),
				voiceConfig.getLanguage());
		super.initialize();
	}

	private void loadVoiceConfig() {
		voiceConfig = JsonConverter
				.jsonFileToObject(VOICE_CONFIG, VoiceConfig.class);
	}

	@Override
	protected void fillMethodMap() {
		addVoiceRecognitionMethod();
	}

	private void addVoiceRecognitionMethod() {
		methodMap.put("voiceRecognition", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				VoiceRecognitionRequest parameter = (VoiceRecognitionRequest) rawParameter;
				if (voiceRecognition.isRecording()) {
					return "{ \"status\": \"isRunning\"}";
				}
				return JsonConverter.objectToJsonString(
					voiceRecognition.startRecording(parameter.getDurationInS()));
			}
		});
	}

	public static void main(String[] args) throws IOException {
		new VoiceRecognitionModule();
	}

}
