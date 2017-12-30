package de.deeps.hms.clapdetector;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.sound.sampled.LineUnavailableException;

import de.deeps.hms.FinalModule;
import de.deeps.hms.clapdetector.core.AudioConfig;
import de.deeps.hms.clapdetector.core.ClapDetection;
import de.deeps.hms.clapdetector.core.ClapListener;
import de.deeps.hms.clapdetector.events.ClapEvent;
import de.deeps.hms.clapdetector.parameters.RegisterListenerParameter;
import de.deeps.hms.clapdetector.parameters.UnregisterListenerParameter;
import de.deeps.hms.json.JsonConverter;
import de.deeps.hms.webserver.CommandExecutionInterface;

/**
 * @author Deeps
 */

public class ClapDetectorModule extends FinalModule implements ClapListener {

	private final static String MODULE_CONFIG = "clapdetector_config.json",
			AUDIO_CONFIG = "audio_config.json";

	private AudioConfig audioConfig;
	private ClapDetection clapDetection;
	private List<String> clapListenerAddresses;

	public ClapDetectorModule() throws IOException {
		super(MODULE_CONFIG);
	}

	@Override
	protected void initialize() throws IOException {
		clapListenerAddresses = new LinkedList<>();
		loadAudioConfig();
		try {
			clapDetection = new ClapDetection(this, audioConfig.getSampleRate(),
					audioConfig.getBufferSize(), audioConfig.getSensitivity(),
					audioConfig.getThreshold(),
					audioConfig.getClapCounterTime());
		} catch (LineUnavailableException e) {
			throw new IOException(e.getMessage());
		}
		super.initialize();
	}

	private void loadAudioConfig() {
		audioConfig = JsonConverter
				.jsonFileToObject(AUDIO_CONFIG, AudioConfig.class);
	}

	@Override
	protected void fillMethodMap() {
		addRegisterListenerMethod();
		addUnregisterListenerMethod();
		addStartListenerMethod();
		addStopListenerMethod();
	}

	private void addStopListenerMethod() {
		methodMap.put("stop", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				clapDetection.stop();
				return null;
			}
		});
	}

	private void addStartListenerMethod() {
		methodMap.put("start", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				clapDetection.start();
				return null;
			}
		});
	}

	@Override
	protected void start() {
		clapDetection.start();
		super.start();
	}

	@Override
	protected void stop() {
		clapDetection.stop();
		super.stop();
	}

	private void addRegisterListenerMethod() {
		methodMap.put("registerListener", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				RegisterListenerParameter parameter = (RegisterListenerParameter) rawParameter;
				clapListenerAddresses.add(parameter.getResponseUrl());
				return null;
			}
		});
	}

	private void addUnregisterListenerMethod() {
		methodMap.put("unregisterListener", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				UnregisterListenerParameter parameter = (UnregisterListenerParameter) rawParameter;
				clapListenerAddresses.remove(parameter.getResponseUrl());
				return null;
			}
		});
	}

	@Override
	public void handleClap(int clapCount) {
		for (String address : clapListenerAddresses) {
			sendPost(address, new ClapEvent(clapCount));
		}
	}

	public static void main(String[] args) throws IOException {
		new ClapDetectorModule();
	}

}
