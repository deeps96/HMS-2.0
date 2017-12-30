package de.deeps.hms.voicerecognition.core;

import com.darkprograms.speech.recognizer.Recognizer;

/**
 * @author Deeps
 */

public class VoiceConfig {

	private int numberOfResponses;
	private Recognizer.Languages language;
	private String apiKey;

	public int getNumberOfResponses() {
		return numberOfResponses;
	}

	public void setNumberOfResponses(int numberOfResponses) {
		this.numberOfResponses = numberOfResponses;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public Recognizer.Languages getLanguage() {
		return language;
	}

	public void setLanguage(Recognizer.Languages language) {
		this.language = language;
	}

}
