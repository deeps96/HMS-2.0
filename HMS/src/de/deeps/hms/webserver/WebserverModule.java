package de.deeps.hms.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import de.deeps.hms.ModuleConfig;
import de.deeps.hms.RootModule;
import de.deeps.hms.json.JsonConverter;

/**
 * @author Deeps
 */

public abstract class WebserverModule extends RootModule
		implements HttpHandler {

	protected HashMap<String, CommandExecutionInterface> methodMap;
	protected HttpServer httpServer;

	public WebserverModule(ModuleConfig config) throws IOException {
		super(config);
	}

	@Override
	protected void initialize() throws IOException {
		httpServer = HttpServer.create(
			new InetSocketAddress(config.getHttpConfig().getHttpServerPort()),
			0);

		methodMap = new HashMap<>();
		fillMethodMap();
		createContexts();
	}

	protected abstract void fillMethodMap();

	private void createContexts() {
		for (String commandUri : methodMap.keySet()) {
			httpServer.createContext("/" + commandUri, this);
		}
	}

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String commandUri = httpExchange.getRequestURI().toString()
				.substring(1);
		Command command = getCommandByUri(commandUri);

		Class<?> parameterClass = getParameterClass(command);

		Object parameter = (parameterClass == null)
				? getBodyContent(httpExchange)
				: JsonConverter.jsonStringToObject(
					getBodyContent(httpExchange),
					parameterClass);
		String response = methodMap.get(commandUri).run(parameter);

		if (response != null) {
			sendResponseWithContent(httpExchange, response.getBytes());
		} else {
			sendResponseWithoutContent(httpExchange);
		}
	}

	private Command getCommandByUri(String commandUri) {
		for (Command command : config.getHttpConfig().getSupportedCommands()) {
			if (command.getCommandUri().equals(commandUri)) {
				return command;
			}
		}
		return null;
	}

	private Class<?> getParameterClass(Command command) {
		if (command != null && command.getParameterClassName() != null) {
			try {
				return Class.forName(command.getParameterClassName());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void start() {
		httpServer.start();
		System.out.println(
			config.getModuleType() + " Webserver running on port "
					+ config.getHttpConfig().getHttpServerPort());
	}

	@Override
	protected void stop() {
		httpServer.stop(0);
	}

	private void manageCORS(HttpExchange httpExchange) {
		Headers responseHeader = httpExchange.getResponseHeaders();
		responseHeader.add("Access-Control-Allow-Origin", "*");
		responseHeader.add("Access-Control-Allow-Methods", "POST");
		responseHeader.add(
			"Access-Control-Allow-Headers",
			"X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
		responseHeader.add("Access-Control-Max-Age", "1728000");
	}

	private void sendResponseWithoutContent(HttpExchange httpExchange)
			throws IOException {
		manageCORS(httpExchange);
		httpExchange.sendResponseHeaders(204, -1);
		httpExchange.close();
	}

	private void sendResponseWithContent(HttpExchange httpExchange,
			byte[] content) throws IOException {
		manageCORS(httpExchange);
		httpExchange.getResponseHeaders()
				.add("Content-Type", "application/json");
		httpExchange.sendResponseHeaders(200, content.length);
		OutputStream os = httpExchange.getResponseBody();
		os.write(content, 0, content.length);
		os.flush();
		os.close();
		httpExchange.close();
	}

	private String getBodyContent(HttpExchange httpExchange)
			throws IOException {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(httpExchange.getRequestBody()));
		StringBuilder builder = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		return builder.toString();
	}
}
