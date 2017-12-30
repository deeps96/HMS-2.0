package de.deeps.hms.delay.parameters;

/**
 * @author Deeps
 */

public class DelayParameter {

	private long delayInMS;
	private String content, targetUrl;

	public long getDelayInMS() {
		return delayInMS;
	}

	public void setDelayInMS(long delayInMS) {
		this.delayInMS = delayInMS;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

}
