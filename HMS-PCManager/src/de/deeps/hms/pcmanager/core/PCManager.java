package de.deeps.hms.pcmanager.core;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.deeps.hms.json.JsonConverter;

/**
 * @author Deeps
 */

public abstract class PCManager {

	private ProgramDictionary programDictionary;

	public PCManager(String programDictionaryPath) throws IOException {
		if (!isCompatible()) {
			throw new IOException("Wrong OS!");
		}
		if (new File(programDictionaryPath).exists()) {
			programDictionary = JsonConverter.jsonFileToObject(
				programDictionaryPath,
				ProgramDictionary.class);
		} else {
			programDictionary = new ProgramDictionary();
		}
	}

	public List<String> listPrograms() {
		return programDictionary.listPrograms();
	}

	public void startProgram(String program) {
		String command = programDictionary.getCommandForProgram(program);
		if (command != null) {
			try {
				runCommand(command);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void runCommand(String command) throws IOException {
		Runtime.getRuntime().exec(command);
	}

	// abstract

	public abstract boolean isCompatible();

	public abstract void shutdown();

	public abstract void standby();

	public abstract void setVolume(String device, int volume);

	public abstract void changeOutputDevice(String outputDevice);
}
