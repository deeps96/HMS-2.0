package de.deeps.hms.ledboard;

import java.io.IOException;

import de.deeps.hms.FinalModule;
import de.deeps.hms.ledboard.core.LEDBoardWrapper;
import de.deeps.hms.ledboard.parameters.SetBrightnessParameter;
import de.deeps.hms.ledboard.parameters.ShowAnimatedContentParameter;
import de.deeps.hms.ledboard.parameters.ShowStaticContentParameter;
import de.deeps.hms.webserver.CommandExecutionInterface;

/**
 * @author Deeps
 */

public class LEDBoardModule extends FinalModule {

	private LEDBoardWrapper ledBoard;

	public LEDBoardModule() throws IOException {
		super("ledboard_config.json");
	}

	@Override
	protected void initialize() throws IOException {
		super.initialize();
		ledBoard = new LEDBoardWrapper();
	}

	@Override
	protected void fillMethodMap() {
		addClearMethod();
		addSetBrightnessMethod();
		addShowStaticContentMethod();
		addShowAnimatedContentMethod();
	}

	private void addShowAnimatedContentMethod() {
		methodMap.put("showAnimatedContent", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				ShowAnimatedContentParameter parameter = (ShowAnimatedContentParameter) rawParameter;
				ledBoard.showAnimatedContent(
					parameter.getContent(),
					parameter.getRepeat());
				return null;
			}
		});
	}

	private void addShowStaticContentMethod() {
		methodMap.put("showStaticContent", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				ShowStaticContentParameter parameter = (ShowStaticContentParameter) rawParameter;
				if (parameter.getDurationInSeconds() > 0) {
					ledBoard.showStaticContent(
						parameter.getContent(),
						parameter.getDurationInSeconds());
				} else {
					ledBoard.showStaticContent(parameter.getContent());
				}
				return null;
			}
		});
	}

	private void addSetBrightnessMethod() {
		methodMap.put("setBrightness", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				ledBoard.setBrightness(
					((SetBrightnessParameter) rawParameter)
							.getBrightnessLevel());
				return null;
			}
		});
	}

	private void addClearMethod() {
		methodMap.put("clear", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				ledBoard.clearContent();
				return null;
			}
		});
	}

	@Override
	protected void shutdown() {
		ledBoard.shutdown();
		super.shutdown();
	}

	public static void main(String[] args) throws IOException {
		new LEDBoardModule();
	}

}
