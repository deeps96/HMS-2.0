package de.deeps.hms.pcmanager.core;

import java.io.IOException;

/**
 * @author Deeps
 */

public class WindowsPCManager extends PCManager {

	private final static String PROGRAM_DICTIONARY_PATH = "windowsProgramDictionary.json";

	public WindowsPCManager() throws IOException {
		super(PROGRAM_DICTIONARY_PATH);
		runNIRCMDCommand("");
	}

	@Override
	public boolean isCompatible() {
		return WindowsPCManager.couldBeCompatible();
	}

	public static boolean couldBeCompatible() {
		return System.getProperty("os.name").contains("Windows");
	}

	@Override
	public void shutdown() {
		try {
			runNIRCMDCommand("initshutdown force");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void standby() {
		try {
			runNIRCMDCommand("standby force");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setVolume(String device, int volume) {
		assert volume >= 0 && volume <= 100;
		try {
			runSoundCommand("/SetVolume \"" + device + "\" " + volume);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void changeOutputDevice(String outputDevice) {
		try {
			runNIRCMDCommand("setdefaultsounddevice \"" + outputDevice + "\"");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void runNIRCMDCommand(String command) throws IOException {
		runCommand("./nircmdc " + command);
	}

	private void runSoundCommand(String command) throws IOException {
		runCommand("./SoundVolumeView.exe " + command);
	}

}
