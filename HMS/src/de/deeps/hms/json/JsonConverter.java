package de.deeps.hms.json;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Deeps
 */

public class JsonConverter {

	private static ObjectMapper mapper = new ObjectMapper();

	public static <T> T jsonStringToObject(String input, Class<T> classType) {
		try {
			return mapper.readValue(input, classType);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String objectToJsonString(Object input) {
		try {
			return mapper.writeValueAsString(input);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> T jsonFileToObject(String filePath, Class<T> classType) {
		return jsonFileToObject(new File(filePath), classType);
	}

	public static <T> T jsonFileToObject(File file, Class<T> classType) {
		try {
			return mapper.readValue(file, classType);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void writeObjectToJsonFile(Object input, String filePath) {
		writeObjectToJsonFile(input, new File(filePath));
	}

	public static void writeObjectToJsonFile(Object input, File file) {
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(file, input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
