package de.deeps.hms.irremote.core;

import java.util.List;

/**
 * @author Deeps
 */

public interface IRRemoteInterface {
	public String mapButtonToKeystring(String button);

	public String getRemoteName();

	public List<String> getButtonNameList();
}
