package de.deeps.hms.webserver;

import java.util.List;

/**
 * @author Deeps
 */

public class HttpConfig {

	private int httpServerPort;
	private List<Command> supportedCommands;

	public HttpConfig() {
	}

	public HttpConfig(int httpServerPort, List<Command> supportedCommands) {
		this.httpServerPort = httpServerPort;
		this.supportedCommands = supportedCommands;
	}

	// Getter
	public int getHttpServerPort() {
		return httpServerPort;
	}

	public List<Command> getSupportedCommands() {
		return supportedCommands;
	}

	public void setHttpServerPort(int httpServerPort) {
		this.httpServerPort = httpServerPort;
	}

	public void setSupportedCommands(List<Command> supportedCommands) {
		this.supportedCommands = supportedCommands;
	}

}
