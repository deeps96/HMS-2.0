package de.deeps.hms.pcmanager;

import java.io.IOException;

import de.deeps.hms.FinalModule;
import de.deeps.hms.json.JsonConverter;
import de.deeps.hms.pcmanager.core.LinuxPCManager;
import de.deeps.hms.pcmanager.core.PCManager;
import de.deeps.hms.pcmanager.core.WindowsPCManager;
import de.deeps.hms.pcmanager.parameters.ChangeOutputDeviceParameter;
import de.deeps.hms.pcmanager.parameters.SetVolumeParameter;
import de.deeps.hms.pcmanager.parameters.StartProgramParameter;
import de.deeps.hms.webserver.CommandExecutionInterface;

/**
 * @author Deeps
 */

public class PCManagerModule extends FinalModule {

	private final static String MODULE_CONFIG = "pcmanager_config.json";

	private PCManager pcManager;

	public PCManagerModule() throws IOException {
		super(MODULE_CONFIG);
	}

	@Override
	protected void initialize() throws IOException {
		if (WindowsPCManager.couldBeCompatible()) {
			pcManager = new WindowsPCManager();
		} else if (LinuxPCManager.couldBeCompatible()) {
			pcManager = new LinuxPCManager();
		} else {
			throw new IOException("OS not supported!");
		}
		super.initialize();
	}

	@Override
	protected void fillMethodMap() {
		addListProgramsMethod();
		addStartProgramMethod();
		addShutdownMethod();
		addStandbyMethod();
		addSetVolumeMethod();
		addChangeOutputDeviceMethod();
	}

	private void addChangeOutputDeviceMethod() {
		methodMap.put("changeOutputDevice", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				ChangeOutputDeviceParameter parameter = (ChangeOutputDeviceParameter) rawParameter;
				pcManager.changeOutputDevice(parameter.getOutputDevice());
				return null;
			}
		});
	}

	private void addSetVolumeMethod() {
		methodMap.put("setVolume", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				SetVolumeParameter parameter = (SetVolumeParameter) rawParameter;
				pcManager.setVolume(
					parameter.getDevice(),
					parameter.getVolume());
				return null;
			}
		});
	}

	private void addStandbyMethod() {
		methodMap.put("standby", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				pcManager.standby();
				return null;
			}
		});
	}

	private void addShutdownMethod() {
		methodMap.put("shutdown", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				pcManager.shutdown();
				return null;
			}
		});
	}

	private void addStartProgramMethod() {
		methodMap.put("startProgram", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				StartProgramParameter parameter = (StartProgramParameter) rawParameter;
				pcManager.startProgram(parameter.getProgramName());
				return null;
			}
		});
	}

	private void addListProgramsMethod() {
		methodMap.put("listPrograms", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				return JsonConverter
						.objectToJsonString(pcManager.listPrograms());
			}
		});
	}

	public static void main(String[] args) throws IOException {
		new PCManagerModule();
	}

}
