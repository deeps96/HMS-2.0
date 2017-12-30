package de.deeps.hms.audioplayer;

import java.io.IOException;

import de.deeps.hms.FinalModule;
import de.deeps.hms.audioplayer.core.AudioPlayer;
import de.deeps.hms.audioplayer.core.PlayerConfig;
import de.deeps.hms.audioplayer.parameters.PlayAudioParameter;
import de.deeps.hms.json.JsonConverter;
import de.deeps.hms.webserver.CommandExecutionInterface;

/**
 * @author Deeps
 */

public class AudioPlayerModule extends FinalModule {

	private final static String MODULE_CONFIG = "audioplayer_config.json",
			PLAYER_CONFIG = "player_config.json";

	private AudioPlayer audioPlayer;
	private PlayerConfig playerConfig;

	public AudioPlayerModule() throws IOException {
		super(MODULE_CONFIG);
	}

	@Override
	protected void initialize() throws IOException {
		loadPlayerConfig();
		audioPlayer = new AudioPlayer(playerConfig.getAudioFileDir());
		super.initialize();
	}

	private void loadPlayerConfig() {
		playerConfig = JsonConverter
				.jsonFileToObject(PLAYER_CONFIG, PlayerConfig.class);
	}

	@Override
	protected void fillMethodMap() {
		addPlayAudioMethod();
		addListAudioFilesMethod();
		addStopPlayingAudioMethod();
	}

	@Override
	protected void stop() {
		audioPlayer.stopPlayingAudio();
		super.stop();
	}

	private void addStopPlayingAudioMethod() {
		methodMap.put("stopPlayingAudio", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				audioPlayer.stopPlayingAudio();
				return null;
			}
		});
	}

	private void addListAudioFilesMethod() {
		methodMap.put("listAudioFiles", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				return JsonConverter
						.objectToJsonString(audioPlayer.listAudioFiles());
			}
		});
	}

	private void addPlayAudioMethod() {
		methodMap.put("playAudio", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				PlayAudioParameter parameter = (PlayAudioParameter) rawParameter;
				return "{\"isPlaying\": "
						+ audioPlayer.playAudio(parameter.getFileName()) + "}";
			}
		});
	}

	public static void main(String[] args) throws IOException {
		new AudioPlayerModule();
	}

}
