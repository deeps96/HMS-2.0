package de.deeps.hms.voicerecognition.core;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GoogleResponse;
import com.darkprograms.speech.recognizer.Recognizer;

import de.deeps.hms.voicerecognition.parameters.VoiceRecognizedAnswer;
import net.sourceforge.javaflacencoder.FLACFileWriter;

/**
 * @author Deeps
 */

public class VoiceRecognition {

	private final String AUDIO_TMP_FLAC = "voicerecord.flac";

	private boolean isRecording;
	private File tmpAudioFile;
	private int numberOfResponses;
	private Microphone microphone;
	private Recognizer recognizer;
	private Recognizer.Languages language;
	private String apiKey;

	public VoiceRecognition(int numberOfResponses, String apiKey,
			Recognizer.Languages language) {
		this.numberOfResponses = numberOfResponses;
		this.apiKey = apiKey;
		this.language = language;
		initialize();
	}

	private void initialize() {
		tmpAudioFile = new File(AUDIO_TMP_FLAC);
		recognizer = new Recognizer(language, apiKey);
		isRecording = false;
	}

	public VoiceRecognizedAnswer startRecording(int duration) {
		if (isRecording) {
			return null;
		}
		isRecording = true;
		VoiceRecognizedAnswer result = null;
		try {
			microphone = new Microphone(FLACFileWriter.FLAC);
			microphone.captureAudioToFile(tmpAudioFile);
			Thread.sleep(duration * 1000L);
			microphone.close();
			result = analyseFile();
		} catch (LineUnavailableException | InterruptedException e) {
			e.printStackTrace();
		}
		isRecording = false;
		return result;
	}

	private VoiceRecognizedAnswer analyseFile() {
		VoiceRecognizedAnswer result = null;
		try {
			GoogleResponse googleResponse = recognizer.getRecognizedDataForFlac(
				tmpAudioFile,
				numberOfResponses,
				(int) microphone.getAudioFormat().getSampleRate());
			result = new VoiceRecognizedAnswer();
			result.setResponse(googleResponse.getResponse());
			result.setOtherPossibleResponses(
				googleResponse.getOtherPossibleResponses());
		} catch (IOException e) {
			e.printStackTrace();
		}
		tmpAudioFile.delete();
		return result;
	}

	public boolean isRecording() {
		return isRecording;
	}
}
