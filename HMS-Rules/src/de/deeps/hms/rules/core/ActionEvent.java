package de.deeps.hms.rules.core;

/**
 * @author Deeps
 */

public class ActionEvent {

	private String targetUrl;
	private Object content;

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

}
