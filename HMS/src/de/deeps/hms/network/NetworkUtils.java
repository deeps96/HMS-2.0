package de.deeps.hms.network;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @author Deeps
 */

public class NetworkUtils {

	public static InetAddress getLocalAddress() {
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
					.getNetworkInterfaces();
			InetAddress address = null;
			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaces
						.nextElement();
				Enumeration<InetAddress> addresses = networkInterface
						.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress internetAddress = addresses.nextElement();
					if (!internetAddress.isLinkLocalAddress()
							&& !internetAddress.isLoopbackAddress()
							&& internetAddress instanceof Inet4Address) {
						address = internetAddress;
					}
				}
			}
			return address;
		} catch (SocketException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static InetAddress getBroadcastAddress() {
		try {
			byte[] address = getLocalAddress().getAddress();
			address[3] = (byte) 255;
			return InetAddress.getByAddress(address);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}

}
