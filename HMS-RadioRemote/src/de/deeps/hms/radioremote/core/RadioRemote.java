package de.deeps.hms.radioremote.core;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Deeps
 */

public class RadioRemote {

	private List<RadioRemoteInterface> supportedRemotes;

	public RadioRemote() throws IOException {
		supportedRemotes = Arrays.asList(new RadioRemoteDesktop());
		sendCommand("00000", "0 1"); // send test command
	}

	public void pressButton(String remoteName, String buttonName) {
		RadioRemoteInterface remote = getRemoteByName(remoteName);
		if (remote == null) {
			return;
		}
		try {
			sendCommand(
				remote.getRemoteKey(),
				remote.mapButtonToKeystring(buttonName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String> listRemotes() {
		List<String> remoteNames = new LinkedList<>();
		for (RadioRemoteInterface remote : supportedRemotes) {
			remoteNames.add(remote.getRemoteName());
		}
		return remoteNames;
	}

	public List<String> listButtons(String remoteName) {
		RadioRemoteInterface remote = getRemoteByName(remoteName);
		return (remote == null) ? null : remote.getButtonNameList();
	}

	private void sendCommand(String remoteKey, String buttonKey)
			throws IOException {
		Runtime.getRuntime().exec(
			"sudo /home/pi/raspberry-remote/send " + remoteKey + " "
					+ buttonKey);
	}

	private RadioRemoteInterface getRemoteByName(String string) {
		for (RadioRemoteInterface remote : supportedRemotes) {
			if (remote.getRemoteName().equals(string)) {
				return remote;
			}
		}
		return null;
	}

}
