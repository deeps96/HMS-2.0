package de.deeps.hms.rules.core;

import de.deeps.hms.rules.core.Comparator.ComparatorType;
import de.deeps.hms.rules.core.Comparator.SupportedDataType;

/**
 * @author Deeps
 */

public class Condition {

	private ComparatorType comparator;
	private String inputClassType, jsonAttributePath, expectedValue, commandURI;
	private SupportedDataType attributeDataType;

	public ComparatorType getComparator() {
		return comparator;
	}

	public void setComparator(ComparatorType comparator) {
		this.comparator = comparator;
	}

	public String getInputClassType() {
		return inputClassType;
	}

	public void setInputClassType(String inputClassType) {
		this.inputClassType = inputClassType;
	}

	public String getJsonAttributePath() {
		return jsonAttributePath;
	}

	public void setJsonAttributePath(String jsonAttributePath) {
		this.jsonAttributePath = jsonAttributePath;
	}

	public String getExpectedValue() {
		return expectedValue;
	}

	public void setExpectedValue(String expectedValue) {
		this.expectedValue = expectedValue;
	}

	public SupportedDataType getAttributeDataType() {
		return attributeDataType;
	}

	public void setAttributeDataType(SupportedDataType attributeDataType) {
		this.attributeDataType = attributeDataType;
	}

	public String getCommandURI() {
		return commandURI;
	}

	public void setCommandURI(String commandURI) {
		this.commandURI = commandURI;
	}

}
