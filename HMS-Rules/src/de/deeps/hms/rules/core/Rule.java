package de.deeps.hms.rules.core;

import java.util.List;

/**
 * @author Deeps
 */

public class Rule {

	private boolean isActive;
	private Condition condition;
	private List<ActionEvent> events;
	private long timeout;
	private String name;

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public List<ActionEvent> getEvents() {
		return events;
	}

	public void setEvents(List<ActionEvent> events) {
		this.events = events;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

}
