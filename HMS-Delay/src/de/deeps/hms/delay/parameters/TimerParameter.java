package de.deeps.hms.delay.parameters;

import java.util.Date;

/**
 * @author Deeps
 */

public class TimerParameter {

	private Date executionDate;
	private long repeatAllMs;
	private String content, targetUrl;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getExecutionDate() {
		return executionDate;
	}

	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public long getRepeatAllMs() {
		return repeatAllMs;
	}

	public void setRepeatAllMs(long repeatAllMs) {
		this.repeatAllMs = repeatAllMs;
	}

}
