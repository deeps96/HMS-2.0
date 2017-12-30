package de.deeps.hms.ledboard.core;

import java.io.IOException;
import java.util.HashMap;

import de.deeps.ledboard.HardwareDriver;
import de.deeps.ledboard.LedBoard;

/**
 * @author Deeps
 */

public class LEDBoardWrapper {

	public static enum BrightnessLevel {
		LOW, MEDIUM, HIGH
	}

	private int currentOrder;
	private HashMap<BrightnessLevel, Integer> mapBrightnessLevel;
	private LedBoard ledBoard;

	public LEDBoardWrapper() throws IOException {
		initialize();
	}

	private void initialize() throws IOException {
		currentOrder = 0;
		ledBoard = new LedBoard();
		mapBrightnessLevel = new HashMap<>();
		mapBrightnessLevel
				.put(BrightnessLevel.LOW, HardwareDriver.BRIGHTNESS_LEVEL_MIN);
		mapBrightnessLevel.put(
			BrightnessLevel.MEDIUM,
			HardwareDriver.BRIGHTNESS_LEVEL_MED);
		mapBrightnessLevel
				.put(BrightnessLevel.HIGH, HardwareDriver.BRIGHTNESS_LEVEL_MAX);

		setBrightness(BrightnessLevel.HIGH);
	}

	public void clearContent() {
		ledBoard.off();
		currentOrder++;
	}

	public void setBrightness(BrightnessLevel brightnessLevel) {
		ledBoard.setBrightness(mapBrightnessLevel.get(brightnessLevel));
	}

	public void showStaticContent(String content) {
		clearContent();
		ledBoard.printStaticMessage(content);
		ledBoard.display();
	}

	public void showStaticContent(String content, int seconds) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				showStaticContent(content);
				int orderID = currentOrder;
				try {
					Thread.sleep(seconds * 1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (orderID == currentOrder) {
					ledBoard.off();
				}
			}
		}).start();
	}

	public void showAnimatedContent(String content, int repeat) {
		clearContent();
		ledBoard.printAnimatedMessage(content, repeat);
		ledBoard.display();
	}

	public void shutdown() {
		clearContent();
	}

}
