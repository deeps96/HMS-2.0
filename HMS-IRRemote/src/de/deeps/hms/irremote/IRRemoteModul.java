package de.deeps.hms.irremote;

import java.io.IOException;
import java.util.List;

import de.deeps.hms.FinalModule;
import de.deeps.hms.irremote.core.IRRemote;
import de.deeps.hms.irremote.core.LircConfig;
import de.deeps.hms.irremote.parameters.ListButtonsParameter;
import de.deeps.hms.irremote.parameters.PressButtonParameter;
import de.deeps.hms.json.JsonConverter;
import de.deeps.hms.webserver.CommandExecutionInterface;

/**
 * @author Deeps
 */

public class IRRemoteModul extends FinalModule {

	private final static String MODULE_CONFIG = "irremote_config.json",
			LIRC_CONFIG = "lirc_config.json";

	private IRRemote irRemote;
	private LircConfig lircConfig;

	public IRRemoteModul() throws IOException {
		super(MODULE_CONFIG);
	}

	@Override
	protected void initialize() throws IOException {
		super.initialize();
		loadLircConfig();
		irRemote = new IRRemote(lircConfig.getLircAddress(),
				lircConfig.getLircPort());
	}

	private void loadLircConfig() {
		lircConfig = JsonConverter
				.jsonFileToObject(LIRC_CONFIG, LircConfig.class);
	}

	@Override
	protected void shutdown() {
		super.shutdown();
		irRemote.shutdown();
	}

	@Override
	protected void fillMethodMap() {
		addPressButtonMethod();
		addListButtonsMethod();
		addListRemotesMethod();
	}

	private void addListRemotesMethod() {
		methodMap.put("listRemotes", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				List<String> remotes = irRemote.listRemotes();
				return JsonConverter.objectToJsonString(remotes);
			}
		});
	}

	private void addListButtonsMethod() {
		methodMap.put("listButtons", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				ListButtonsParameter parameter = (ListButtonsParameter) rawParameter;
				List<String> buttons = irRemote
						.listButtons(parameter.getRemoteName());
				return JsonConverter.objectToJsonString(buttons);
			}
		});
	}

	private void addPressButtonMethod() {
		methodMap.put("pressButton", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				PressButtonParameter parameter = (PressButtonParameter) rawParameter;
				irRemote.pressButton(
					parameter.getRemoteName(),
					parameter.getButtonName());
				return null;
			}
		});
	}

	public static void main(String[] args) throws IOException {
		new IRRemoteModul();
	}

}
