package de.deeps.hms.launcher.core;

import java.util.List;

/**
 * @author Deeps
 */

public class ModulesConfig {

	private List<ModuleExecution> modules;
	private String networkServerShutdownUrl;

	public List<ModuleExecution> getModules() {
		return modules;
	}

	public void setModules(List<ModuleExecution> modules) {
		this.modules = modules;
	}

	public String getNetworkServerShutdownUrl() {
		return networkServerShutdownUrl;
	}

	public void setNetworkServerShutdownUrl(String networkServerShutdownUrl) {
		this.networkServerShutdownUrl = networkServerShutdownUrl;
	}

}
