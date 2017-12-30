package de.deeps.hms.clapdetector.events;

/**
 * @author Deeps
 */

public class ClapEvent {

	private int clapCount;

	public ClapEvent() {
	}

	public ClapEvent(int clapCount) {
		this.clapCount = clapCount;
	}

	public int getClapCount() {
		return clapCount;
	}

	public void setClapCount(int clapCount) {
		this.clapCount = clapCount;
	}

}
