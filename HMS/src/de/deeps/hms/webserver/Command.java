package de.deeps.hms.webserver;

/**
 * @author Deeps
 */

public class Command {

	private String commandUri, parameterJson, parameterClassName;

	public Command() {

	}

	public Command(String commandUri) {
		this(commandUri, null, null);
	}

	public Command(String commandUri, String parameterClassName,
			String parameterJson) {
		this.commandUri = commandUri;
		this.parameterClassName = parameterClassName;
		this.parameterJson = parameterJson;
	}

	// Getters
	public String getCommandUri() {
		return commandUri;
	}

	public String getParameterClassName() {
		return parameterClassName;
	}

	public String getParameterJson() {
		return parameterJson;
	}

	public void setCommandUri(String commandUri) {
		this.commandUri = commandUri;
	}

	public void setParameterClassName(String parameterClassName) {
		this.parameterClassName = parameterClassName;
	}

	public void setParameterJson(String parameterJson) {
		this.parameterJson = parameterJson;
	}
}
