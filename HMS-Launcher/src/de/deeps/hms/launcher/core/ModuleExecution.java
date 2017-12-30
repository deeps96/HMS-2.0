package de.deeps.hms.launcher.core;

/**
 * @author Deeps
 */

public class ModuleExecution {

	private String moduleID;
	private String commandDir;
	private String moduleStartCommand;

	public String getModuleID() {
		return moduleID;
	}

	public void setModuleID(String moduleID) {
		this.moduleID = moduleID;
	}

	public String getModuleStartCommand() {
		return moduleStartCommand;
	}

	public void setModuleStartCommand(String moduleStartCommand) {
		this.moduleStartCommand = moduleStartCommand;
	}

	public String getCommandDir() {
		return commandDir;
	}

	public void setCommandDir(String commandDir) {
		this.commandDir = commandDir;
	}

}
