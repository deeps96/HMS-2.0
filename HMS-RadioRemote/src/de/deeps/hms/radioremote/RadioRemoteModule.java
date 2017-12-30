package de.deeps.hms.radioremote;

import java.io.IOException;
import java.util.List;

import de.deeps.hms.FinalModule;
import de.deeps.hms.json.JsonConverter;
import de.deeps.hms.radioremote.core.RadioRemote;
import de.deeps.hms.radioremote.parameters.ListButtonsParameter;
import de.deeps.hms.radioremote.parameters.PressButtonParameter;
import de.deeps.hms.webserver.CommandExecutionInterface;

/**
 * @author Deeps
 */

public class RadioRemoteModule extends FinalModule {

	private final static String MODULE_CONFIG = "radioremote_config.json";

	private RadioRemote radioRemote;

	public RadioRemoteModule() throws IOException {
		super(MODULE_CONFIG);
	}

	@Override
	protected void initialize() throws IOException {
		radioRemote = new RadioRemote();
		super.initialize();
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
				List<String> remotes = radioRemote.listRemotes();
				return JsonConverter.objectToJsonString(remotes);
			}
		});
	}

	private void addListButtonsMethod() {
		methodMap.put("listButtons", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				ListButtonsParameter parameter = (ListButtonsParameter) rawParameter;
				List<String> buttons = radioRemote
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
				radioRemote.pressButton(
					parameter.getRemoteName(),
					parameter.getButtonName());
				return null;
			}
		});
	}

	public static void main(String[] args) throws IOException {
		new RadioRemoteModule();
	}

}
