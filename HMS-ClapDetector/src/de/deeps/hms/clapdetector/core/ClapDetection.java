package de.deeps.hms.clapdetector.core;

import javax.sound.sampled.LineUnavailableException;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.onsets.OnsetHandler;
import be.tarsos.dsp.onsets.PercussionOnsetDetector;

/**
 * @author Deeps
 */

public class ClapDetection implements Runnable {

	private AudioDispatcher audioDispatcher;
	private boolean isClapCounterRunning;
	private ClapListener listener;
	private int sampleRate, bufferSize, sensitivity, threshold,
			clapCounterTimeInMs, claps;

	public ClapDetection(ClapListener listener, int sampleRate, int bufferSize,
			int sensitivity, int threshold, int clapCounterTimeInMs)
			throws LineUnavailableException {
		this.listener = listener;
		this.sampleRate = sampleRate;
		this.bufferSize = bufferSize;
		this.sensitivity = sensitivity;
		this.threshold = threshold;
		this.clapCounterTimeInMs = clapCounterTimeInMs;
		initialize();
	}

	private void initialize() throws LineUnavailableException {
		isClapCounterRunning = false;
		audioDispatcher = AudioDispatcherFactory
				.fromDefaultMicrophone(sampleRate, bufferSize, bufferSize / 2);
		audioDispatcher
				.addAudioProcessor(createDefaultPercussionOnsetDetector());
	}

	private PercussionOnsetDetector createDefaultPercussionOnsetDetector() {
		return new PercussionOnsetDetector(sampleRate, bufferSize,
				new OnsetHandler() {
					@Override
					public void handleOnset(double time, double salience) {
						claps++;
						new Thread(ClapDetection.this).start();
					}
				}, sensitivity, threshold);
	}

	@Override
	public void run() {
		if (isClapCounterRunning)
			return;
		isClapCounterRunning = true;
		try {
			Thread.sleep(clapCounterTimeInMs);
			if (claps > 0) {
				listener.handleClap(claps);
				claps = 0;
			}
			isClapCounterRunning = false;
		} catch (InterruptedException e) {
		}
	}

	public void start() {
		claps = 0;
		new Thread(audioDispatcher).start();
	}

	public void stop() {
		audioDispatcher.stop();
	}

}
