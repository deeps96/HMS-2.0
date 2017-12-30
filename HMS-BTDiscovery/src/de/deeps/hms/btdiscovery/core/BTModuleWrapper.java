package de.deeps.hms.btdiscovery.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;

import de.deeps.hms.btdiscovery.parameters.CurrentStatusParameter;
import de.deeps.hms.btdiscovery.parameters.RegisterListenerParameter;
import de.deeps.hms.btdiscovery.parameters.UnregisterListenerParameter;
import de.deeps.hms.json.JsonConverter;

/**
 * @author Deeps
 */

public class BTModuleWrapper {

	private final HttpClient httpClient = HttpClients.createDefault();

	private Process pythonProcess;
	private String btModuleAddress;

	public BTModuleWrapper(String btModuleAddress) {
		this.btModuleAddress = btModuleAddress;
	}

	public void start() throws IOException {
		if (pythonProcess != null && pythonProcess.isAlive()) {
			return;
		}

		ProcessBuilder builder = new ProcessBuilder();
		builder.command().add("script");
		builder.command().add("-c");
		builder.command().add("sudo python BTModule.py");
		builder.command().add("-f");
		builder.command().add("-q");
		pythonProcess = builder.start();

		BufferedReader reader = new BufferedReader(
				new InputStreamReader(pythonProcess.getInputStream()));
		String line;

		while (pythonProcess.isAlive() && (line = reader.readLine()) != null) {
			if (line.contains("STARTPROCESS COMPLETE")) {
				break;
			}
		}
	}

	public void stop() {
		if (pythonProcess != null && pythonProcess.isAlive()) {
			pythonProcess.destroyForcibly();
		}
		ProcessBuilder builder = new ProcessBuilder();
		builder.command().add("script");
		builder.command().add("-c");
		builder.command().add("sudo killall python");
		builder.command().add("-f");
		builder.command().add("-q");
		try {
			builder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String isOnline() {
		return sendPost("ping", null);
	}

	public String unregisterDeviceListener(
			UnregisterListenerParameter parameter) {
		return sendPost("unregisterListenerForDevice", parameter);
	}

	public String registerDeviceListener(RegisterListenerParameter parameter) {
		return sendPost("registerListenerForDevice", parameter);
	}

	public String currentStatus(CurrentStatusParameter parameter) {
		return sendPost("currentStatusOfDevice", parameter);
	}

	private String sendPost(String uri, Object content) {
		HttpPost httpPost = new HttpPost(btModuleAddress + "/" + uri);
		if (content != null) {
			httpPost.setHeader("Content-Type", "application/json");
			String jsonContent = JsonConverter.objectToJsonString(content);
			HttpEntity contentEntity = new ByteArrayEntity(
					jsonContent.getBytes());
			httpPost.setEntity(contentEntity);
		}
		try {
			HttpResponse response = httpClient.execute(httpPost);
			return new BasicResponseHandler().handleResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
