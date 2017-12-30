package de.deeps.hms.clapdetector.core;

/**
 * @author Deeps
 */

public class AudioConfig {

	private int clapCounterTime, sampleRate, bufferSize, sensitivity, threshold;

	public int getClapCounterTime() {
		return clapCounterTime;
	}

	public void setClapCounterTime(int clapCounterTime) {
		this.clapCounterTime = clapCounterTime;
	}

	public int getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public int getSensitivity() {
		return sensitivity;
	}

	public void setSensitivity(int sensitivity) {
		this.sensitivity = sensitivity;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

}
