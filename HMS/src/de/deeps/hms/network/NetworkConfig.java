package de.deeps.hms.network;

/**
 * @author Deeps
 */

public class NetworkConfig {

	private int portOfAssociatedNetworkServer, tcpServerPort;

	public NetworkConfig() {
	}

	public NetworkConfig(int portOfAssociatedNetworkServer, int tcpServerPort) {
		this.portOfAssociatedNetworkServer = portOfAssociatedNetworkServer;
		this.tcpServerPort = tcpServerPort;
	}

	// Getter
	public int getPortOfAssociatedNetworkServer() {
		return portOfAssociatedNetworkServer;
	}

	public int getTcpServerPort() {
		return tcpServerPort;
	}

	public void setPortOfAssociatedNetworkServer(
			int portOfAssociatedNetworkServer) {
		this.portOfAssociatedNetworkServer = portOfAssociatedNetworkServer;
	}

	public void setTcpServerPort(int tcpServerPort) {
		this.tcpServerPort = tcpServerPort;
	}

}
