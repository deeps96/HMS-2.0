package de.deeps.hms.voicerecognition.parameters;

/**
 * @author Deeps
 */

public class VoiceRecognitionRequest {

	private int durationInS;
	private String responseUrl;

	public int getDurationInS() {
		return durationInS;
	}

	public void setDurationInS(int duration) {
		this.durationInS = duration;
	}

	public String getResponseUrl() {
		return responseUrl;
	}

	public void setResponseUrl(String responseUrl) {
		this.responseUrl = responseUrl;
	}

}
