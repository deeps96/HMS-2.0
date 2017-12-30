package de.deeps.hms.rules.core;

/**
 * @author Deeps
 */

public class Comparator {

	public static enum SupportedDataType {
		STRING, INTEGER, BOOLEAN
	};

	public static enum ComparatorType {
		EQUALS, GREATER
	};

	public static Object convert(SupportedDataType dataType, String value) {
		switch (dataType) {
			case INTEGER:
				return Integer.parseInt(value);
			case BOOLEAN:
				return Boolean.parseBoolean(value);
			default:
				return value;
		}
	}

	public static boolean compare(ComparatorType comparator,
			SupportedDataType dataType, String actualValue,
			String expectedValue) {
		Object actual = convert(dataType, actualValue);
		Object expected = convert(dataType, expectedValue);
		switch (dataType) {
			case BOOLEAN:
				return compare(
					comparator,
					(Boolean) actual,
					(Boolean) expected);
			case INTEGER:
				return compare(
					comparator,
					(Integer) actual,
					(Integer) expected);
			case STRING:
				return compare(comparator, (String) actual, (String) expected);
			default:
				return false;

		}
	}

	private static boolean compare(ComparatorType comparator, int expectedValue,
			int value) {
		switch (comparator) {
			case EQUALS:
				return expectedValue == value;
			case GREATER:
				return expectedValue > value;
			default:
				return false;
		}
	}

	private static boolean compare(ComparatorType comparator,
			String expectedValue, String value) {
		switch (comparator) {
			case EQUALS:
				return expectedValue.equals(value);
			case GREATER:
				return expectedValue.length() > value.length();
			default:
				return false;
		}
	}

	private static boolean compare(ComparatorType comparator,
			boolean expectedValue, boolean value) {
		switch (comparator) {
			case EQUALS:
				return expectedValue == value;
			default:
				return false;
		}
	}

}
