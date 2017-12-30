package de.deeps.hms.wol;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import de.deeps.hms.FinalModule;
import de.deeps.hms.network.UDPSender;
import de.deeps.hms.webserver.CommandExecutionInterface;
import de.deeps.hms.wol.parameters.SendWOLParameter;

/**
 * @author Deeps
 */

public class WOLModule extends FinalModule {

	private final static String MODULE_CONFIG = "wol_config.json";

	private final int WOL_PORT = 9;

	private UDPSender udpSender;

	public WOLModule() throws IOException {
		super(MODULE_CONFIG);
	}

	@Override
	protected void initialize() throws IOException {
		udpSender = new UDPSender();
		super.initialize();
	}

	@Override
	protected void fillMethodMap() {
		addSendWolMethod();
	}

	private void addSendWolMethod() {
		methodMap.put("sendWOL", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				SendWOLParameter parameter = (SendWOLParameter) rawParameter;
				sendWOLCommand(
					parameter.getTargetMAC(),
					parameter.getTargetAddress());
				return null;
			}
		});
	}

	@Override
	protected void shutdown() {
		udpSender.shutdown();
		super.shutdown();
	}

	private void sendWOLCommand(String macAddress, String targetAddress) {
		byte[] mac = getMacBytes(macAddress);
		byte[] bytes = new byte[6 + 16 * mac.length];
		for (int i = 0; i < 6; i++) {
			bytes[i] = (byte) 0xff;
		}
		for (int i = 6; i < bytes.length; i += mac.length) {
			System.arraycopy(mac, 0, bytes, i, mac.length);
		}
		try {
			udpSender.sendMessage(
				bytes,
				InetAddress.getByName(targetAddress),
				WOL_PORT);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	private byte[] getMacBytes(String macAddress)
			throws IllegalArgumentException {
		byte[] bytes = new byte[6];
		String[] hex = macAddress.split("(\\:|\\-)");
		if (hex.length != 6) {
			throw new IllegalArgumentException("Invalid MAC address.");
		}
		try {
			for (int i = 0; i < 6; i++) {
				bytes[i] = (byte) Integer.parseInt(hex[i], 16);
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Invalid hex digit in MAC address.");
		}
		return bytes;
	}

	public static void main(String[] args) throws IOException {
		new WOLModule();
	}
}
