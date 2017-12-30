package de.deeps.hms.irremote.core;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.lirc.LircClient;
import org.lirc.TcpLircClient;

/**
 * @author Deeps
 */

public class IRRemote {

	private LircClient client;
	private List<IRRemoteInterface> supportedRemotes;

	public IRRemote(String lircAddress, int lircPort) throws IOException {
		client = new TcpLircClient(lircAddress, lircPort);
		supportedRemotes = Arrays
				.asList(new IRRemoteLightCeiling(), new IRRemoteSubwoofer());
	}

	public void pressButton(String remoteName, String buttonName) {
		IRRemoteInterface remote = getRemoteByName(remoteName);
		if (remote == null) {
			return;
		}
		sendCommand(
			remote.getRemoteName(),
			remote.mapButtonToKeystring(buttonName));
	}

	public List<String> listRemotes() {
		List<String> remoteNames = new LinkedList<>();
		for (IRRemoteInterface remote : supportedRemotes) {
			remoteNames.add(remote.getRemoteName());
		}
		return remoteNames;
	}

	public List<String> listButtons(String remoteName) {
		IRRemoteInterface remote = getRemoteByName(remoteName);
		return (remote == null) ? null : remote.getButtonNameList();
	}

	private void sendCommand(String remoteName, String buttonName) {
		try {
			client.sendIrCommand(remoteName, buttonName, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private IRRemoteInterface getRemoteByName(String string) {
		for (IRRemoteInterface remote : supportedRemotes) {
			if (remote.getRemoteName().equals(string)) {
				return remote;
			}
		}
		return null;
	}

	public void shutdown() {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
