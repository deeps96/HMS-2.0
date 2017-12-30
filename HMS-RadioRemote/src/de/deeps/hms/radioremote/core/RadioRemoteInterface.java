package de.deeps.hms.radioremote.core;

import java.util.List;

/**
 * @author Deeps
 */

public interface RadioRemoteInterface {
	public String mapButtonToKeystring(String button);

	public String getRemoteName();

	public List<String> getButtonNameList();

	public String getRemoteKey();
}
