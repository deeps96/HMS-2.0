package de.deeps.hms.radioremote.core;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Deeps
 */

public class RadioRemoteDesktop implements RadioRemoteInterface {

	public static final String REMOTE_NAME = "desktop", REMOTE_KEY = "11111";

	public static enum Button {
		A_ON, A_OFF, B_ON, B_OFF, C_ON, C_OFF, D_ON, D_OFF
	}

	@Override
	public String mapButtonToKeystring(String keyName) {
		Button button = Button.valueOf(keyName);
		switch (button) {
			case A_OFF:
				return "1 0";
			case A_ON:
				return "1 1";
			case B_OFF:
				return "2 0";
			case B_ON:
				return "2 1";
			case C_OFF:
				return "3 0";
			case C_ON:
				return "3 1";
			case D_OFF:
				return "4 0";
			case D_ON:
				return "4 1";
			default:
				return null;
		}
	}

	@Override
	public String getRemoteName() {
		return REMOTE_NAME;
	}

	@Override
	public String getRemoteKey() {
		return REMOTE_KEY;
	}

	@Override
	public List<String> getButtonNameList() {
		List<String> buttonNames = new LinkedList<>();
		for (Button button : Button.values()) {
			buttonNames.add(button.toString());
		}
		return buttonNames;
	}

}
