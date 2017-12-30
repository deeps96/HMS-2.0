package de.deeps.hms.pcmanager.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Deeps
 */

public class ProgramDictionary {

	private HashMap<String, String> programDictionary;

	public ProgramDictionary() {
		programDictionary = new HashMap<>();
	}

	public List<String> listPrograms() {
		return new LinkedList<>(programDictionary.keySet());
	}

	public String getCommandForProgram(String program) {
		if (programDictionary.containsKey(program)) {
			return programDictionary.get(program);
		}
		return null;
	}

	public HashMap<String, String> getProgramDictionary() {
		return programDictionary;
	}

	public void setProgramDictionary(
			HashMap<String, String> programDictionary) {
		this.programDictionary = programDictionary;
	}

}
