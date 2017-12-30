package de.deeps.hms;

import java.io.IOException;

/**
 * @author Deeps
 */

public abstract class RootModule {

	protected boolean isRunning, isShutdownInitiated;
	protected ModuleConfig config;

	public RootModule(ModuleConfig config) throws IOException {
		this.config = config;
		this.isRunning = false;
		init();
		System.out.println(config.getModuleType().toString() + " ready");
	}

	private void init() throws IOException {
		isShutdownInitiated = false;
		initialize();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				shutdownModule();
			}
		});
	}

	protected boolean isRunning() {
		return isRunning;
	}

	public void startModule() {
		if (isRunning) {
			return;
		}
		isRunning = true;
		start();
	}

	public void stopModule() {
		if (!isRunning) {
			return;
		}
		isRunning = false;
		stop();
	}

	public void shutdownModule() {
		if (isShutdownInitiated) {
			return;
		}
		isShutdownInitiated = true;
		RootModule.this.stopModule();
		shutdown();
		System.out.println(config.getModuleType().toString() + " shutdown.");
	}

	// abstract
	protected abstract void initialize() throws IOException;

	protected abstract void shutdown();

	protected abstract void start();

	protected abstract void stop();

}
