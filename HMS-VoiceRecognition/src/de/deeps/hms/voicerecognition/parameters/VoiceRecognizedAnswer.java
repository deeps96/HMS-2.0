package de.deeps.hms.voicerecognition.parameters;

import java.util.List;

/**
 * @author Deeps
 */

public class VoiceRecognizedAnswer {

	private String response;
	private List<String> otherPossibleResponses;

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public List<String> getOtherPossibleResponses() {
		return otherPossibleResponses;
	}

	public void setOtherPossibleResponses(List<String> otherPossibleResponses) {
		this.otherPossibleResponses = otherPossibleResponses;
	}

}
