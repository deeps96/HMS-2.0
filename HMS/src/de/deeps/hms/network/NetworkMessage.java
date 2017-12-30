package de.deeps.hms.network;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.deeps.hms.ModuleConfig;
import de.deeps.hms.network.NetworkMessage.ConfigAnswer;
import de.deeps.hms.network.NetworkMessage.ConfigRequest;
import de.deeps.hms.network.NetworkMessage.IsRunningAnswer;
import de.deeps.hms.network.NetworkMessage.IsRunningRequest;
import de.deeps.hms.network.NetworkMessage.RestartRequest;
import de.deeps.hms.network.NetworkMessage.StartRequest;
import de.deeps.hms.network.NetworkMessage.StartUp;
import de.deeps.hms.network.NetworkMessage.StopRequest;

/**
 * @author Deeps
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
		@JsonSubTypes.Type(value = StartRequest.class, name = "StartRequest"),
		@JsonSubTypes.Type(value = StopRequest.class, name = "StopRequest"),
		@JsonSubTypes.Type(value = RestartRequest.class, name = "RestartRequest"),
		@JsonSubTypes.Type(value = IsRunningRequest.class, name = "IsRunningRequest"),
		@JsonSubTypes.Type(value = IsRunningAnswer.class, name = "IsRunningAnswer"),
		@JsonSubTypes.Type(value = StartUp.class, name = "StartUp"),
		@JsonSubTypes.Type(value = ConfigRequest.class, name = "ConfigRequest"),
		@JsonSubTypes.Type(value = ConfigAnswer.class, name = "ConfigAnswer") })

public abstract class NetworkMessage {

	public static enum Type {
		START,
		STOP,
		RESTART,
		IS_RUNNING_REQUEST,
		IS_RUNNING_ANSWER,
		STARTUP,
		CONFIG_REQUEST,
		CONFIG_ANSWER
	}

	private Type type;

	public NetworkMessage(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public static class StartRequest extends NetworkMessage {

		public StartRequest() {
			super(Type.START);
		}

	}

	public static class StopRequest extends NetworkMessage {

		public StopRequest() {
			super(Type.STOP);
		}

	}

	public static class RestartRequest extends NetworkMessage {

		public RestartRequest() {
			super(Type.RESTART);
		}

	}

	public static class IsRunningRequest extends NetworkMessage {

		public IsRunningRequest() {
			super(Type.IS_RUNNING_REQUEST);
		}

	}

	public static class IsRunningAnswer extends NetworkMessage {

		private boolean isRunning;

		public IsRunningAnswer() {
			super(Type.IS_RUNNING_ANSWER);
		}

		public IsRunningAnswer(boolean isRunning) {
			this();
			this.isRunning = isRunning;
		}

		public boolean isRunning() {
			return isRunning;
		}

		public void setRunning(boolean isRunning) {
			this.isRunning = isRunning;
		}

	}

	public static class StartUp extends NetworkMessage {

		private int tcpPort;

		public StartUp() {
			super(Type.STARTUP);
		}

		public StartUp(int tcpPort) {
			this();
			this.tcpPort = tcpPort;
		}

		public int getTcpPort() {
			return tcpPort;
		}

		public void setTcpPort(int tcpPort) {
			this.tcpPort = tcpPort;
		}
	}

	public static class ConfigAnswer extends NetworkMessage {

		private ModuleConfig config;

		public ConfigAnswer() {
			super(Type.CONFIG_ANSWER);
		}

		public ConfigAnswer(ModuleConfig config) {
			this();
			this.config = config;
		}

		public ModuleConfig getConfig() {
			return config;
		}

		public void setConfig(ModuleConfig config) {
			this.config = config;
		}

	}

	public static class ConfigRequest extends NetworkMessage {

		public ConfigRequest() {
			super(Type.CONFIG_REQUEST);
		}

	}
}
