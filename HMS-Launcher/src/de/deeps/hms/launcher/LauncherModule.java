package de.deeps.hms.launcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.deeps.hms.FinalModule;
import de.deeps.hms.json.JsonConverter;
import de.deeps.hms.launcher.core.ModuleExecution;
import de.deeps.hms.launcher.core.ModulesConfig;
import de.deeps.hms.launcher.parameters.LaunchModuleParameter;
import de.deeps.hms.launcher.parameters.ShutdownModuleParameter;
import de.deeps.hms.network.NetworkUtils;
import de.deeps.hms.webserver.CommandExecutionInterface;

/**
 * @author Deeps
 */

public class LauncherModule extends FinalModule {

	private final static String MODULE_CONFIG = "launcher_config.json",
			MODULES_CONFIG = "modules_config.json";

	private HashMap<String, Process> processes;
	private ModulesConfig modulesConfig;

	public LauncherModule() throws IOException {
		super(MODULE_CONFIG);
	}

	@Override
	protected void initialize() throws IOException {
		processes = new HashMap<>();
		loadModulesConfig();
		super.initialize();
	}

	private void loadModulesConfig() {
		modulesConfig = JsonConverter
				.jsonFileToObject(MODULES_CONFIG, ModulesConfig.class);
	}

	@Override
	protected void fillMethodMap() {
		addListModulesMethod();
		addLaunchModuleMethod();
		addShutdownModuleMethod();
		addLaunchAllMethod();
		addShutdownAllMethod();
	}

	private void addShutdownAllMethod() {
		methodMap.put("shutdownAll", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				for (Iterator<String> moduleIDIterator = processes.keySet()
						.iterator(); moduleIDIterator.hasNext();) {
					stopProcess(moduleIDIterator.next());
				}
				return null;
			}
		});
	}

	private void addLaunchAllMethod() {
		methodMap.put("launchAll", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				launchAll();
				return null;
			}
		});
	}

	protected void launchAll() {
		for (ModuleExecution module : modulesConfig.getModules()) {
			runCommand(module);
		}
	}

	private void addShutdownModuleMethod() {
		methodMap.put("shutdownModule", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				ShutdownModuleParameter parameter = (ShutdownModuleParameter) rawParameter;
				stopProcess(parameter.getModuleID());
				return null;
			}
		});
	}

	private void addLaunchModuleMethod() {
		methodMap.put("listModules", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				List<String> moduleIDs = new LinkedList<>();
				for (ModuleExecution module : modulesConfig.getModules()) {
					moduleIDs.add(module.getModuleID());
				}
				return JsonConverter.objectToJsonString(moduleIDs);
			}
		});
	}

	private void addListModulesMethod() {
		methodMap.put("launchModule", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				LaunchModuleParameter parameter = (LaunchModuleParameter) rawParameter;
				for (ModuleExecution module : modulesConfig.getModules()) {
					if (module.getModuleID().equals(parameter.getModuleID())) {
						runCommand(module);
						break;
					}
				}
				return null;
			}
		});
	}

	protected void runCommand(ModuleExecution moduleExecution) {
		try {
			stopProcess(moduleExecution.getModuleID());
			processes.put(
				moduleExecution.getModuleID(),
				getProcessForModuleExecution(moduleExecution));
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					processes.get(moduleExecution.getModuleID())
							.getInputStream()));
			String line;
			while (processes.get(moduleExecution.getModuleID()).isAlive()
					&& (line = reader.readLine()) != null) {
				if (line.contains("STARTPROCESS COMPLETE")) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Process getProcessForModuleExecution(
			ModuleExecution moduleExecution) throws IOException {
		if (System.getProperty("os.name").contains("Windows")) {
			return Runtime.getRuntime().exec(
				moduleExecution.getModuleStartCommand(),
				null,
				new File(moduleExecution.getCommandDir()));
		} else {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command().add("script");
			builder.command().add("-c");
			builder.command().add(moduleExecution.getModuleStartCommand());
			builder.command().add("-f");
			builder.command().add("-q");
			builder.directory(new File(moduleExecution.getCommandDir()));
			return builder.start();
		}
	}

	private void stopProcess(String moduleID) {
		if (processes.containsKey(moduleID)) {
			if (System.getProperty("os.name").contains("Windows")) {
				try {
					processes.get(moduleID).destroyForcibly().waitFor();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				de.deeps.hms.network.parameters.ShutdownModuleParameter parameter = new de.deeps.hms.network.parameters.ShutdownModuleParameter();
				parameter.setModuleID(moduleID);
				parameter.setIpAddress(
					NetworkUtils.getLocalAddress().getHostAddress());
				sendPost(
					modulesConfig.getNetworkServerShutdownUrl(),
					parameter);
			}
			processes.remove(moduleID);
		}

	}

	public static void main(String[] args)
			throws IOException, InterruptedException {
		LauncherModule module = new LauncherModule();
		Thread.sleep(500L);
		module.launchAll();
	}

}
