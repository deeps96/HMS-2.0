package de.deeps.hms.pcmanager.core;

import java.io.IOException;

/**
 * @author Deeps
 */

public class LinuxPCManager extends PCManager {

	private final static String PROGRAM_DICTIONARY_PATH = "linuxProgramDictionary.json";

	public LinuxPCManager() throws IOException {
		super(PROGRAM_DICTIONARY_PATH);
	}

	@Override
	public boolean isCompatible() {
		return LinuxPCManager.couldBeCompatible();
	}

	public static boolean couldBeCompatible() {
		return System.getProperty("os.name").equals("Linux");
	}

	@Override
	public void shutdown() {
		try {
			runCommand("sudo halt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void standby() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setVolume(String device, int volume) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void changeOutputDevice(String outputDevice) {
		throw new UnsupportedOperationException();
	}

}
