package de.deeps.hms.wol.parameters;

/**
 * @author Deeps
 */

public class SendWOLParameter {

	private String targetMAC, targetAddress;

	public String getTargetMAC() {
		return targetMAC;
	}

	public void setTargetMAC(String targetMAC) {
		this.targetMAC = targetMAC;
	}

	public String getTargetAddress() {
		return targetAddress;
	}

	public void setTargetAddress(String targetAddress) {
		this.targetAddress = targetAddress;
	}

}
