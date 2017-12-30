package de.deeps.hms;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import de.deeps.hms.json.JsonConverter;
import de.deeps.hms.network.NetworkClientModule;

/**
 * @author Deeps
 */

public abstract class FinalModule extends NetworkClientModule {

	@Override
	protected void start() {
		super.start();
		System.out.println("STARTPROCESS COMPLETE");
	}

	public FinalModule(String configFilePath) throws IOException {
		super(JsonConverter.jsonFileToObject(
			new File(configFilePath),
			ModuleConfig.class));
	}

	protected String sendPost(String url, String jsonContent) {
		HttpPost httpPost = new HttpPost(url);
		if (jsonContent != null) {
			httpPost.setHeader("Content-Type", "application/json");
			HttpEntity contentEntity = new ByteArrayEntity(
					jsonContent.getBytes());
			httpPost.setEntity(contentEntity);
		}
		try {
			HttpResponse response = HttpClients.createDefault()
					.execute(httpPost);
			if (response.getEntity() == null) {
				return null;
			}
			String result = EntityUtils.toString(response.getEntity());
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	protected String sendPost(String url, Object content) {
		return sendPost(url, JsonConverter.objectToJsonString(content));
	}

}
