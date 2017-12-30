package de.deeps.hms;

import de.deeps.hms.network.NetworkConfig;
import de.deeps.hms.webserver.HttpConfig;

/**
 * @author Deeps
 */

public class ModuleConfig {

	private HttpConfig httpConfig;
	private ModuleType moduleType;
	private NetworkConfig networkConfig;

	public ModuleConfig() {
	}

	public ModuleConfig(HttpConfig httpConfig, ModuleType moduleType,
			NetworkConfig networkConfig) {
		this.httpConfig = httpConfig;
		this.moduleType = moduleType;
		this.networkConfig = networkConfig;
	}

	// Getter
	public NetworkConfig getNetworkConfig() {
		return networkConfig;
	}

	public ModuleType getModuleType() {
		return moduleType;
	}

	public HttpConfig getHttpConfig() {
		return httpConfig;
	}

	public void setHttpConfig(HttpConfig httpConfig) {
		this.httpConfig = httpConfig;
	}

	public void setModuleType(ModuleType moduleType) {
		this.moduleType = moduleType;
	}

	public void setNetworkConfig(NetworkConfig networkConfig) {
		this.networkConfig = networkConfig;
	}

}
