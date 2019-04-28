package gameServer;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class ObserverConnection {
	
	private StreamingService streamingService;
	/**
	 * nick of the oberserver
	 */
	private String nickname;
	/**
	 * Constructor of the class
	 */
	public ObserverConnection(String n){
		nickname=nickname;
	}
	/**
	 * initialize the streaming service
	 */
	public void initializeStreamingService(DatagramSocket ds, int port, InetAddress remoteIP) {
		streamingService =new StreamingService(ds,port,remoteIP);
	}
	/**
	 * return the streaming service
	 */
	public StreamingService getStreamingService() {
		return streamingService;
	}
}
