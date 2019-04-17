package gameServer;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class ObserverConnection {
	
	private StreamingService streamingService;
	/**
	 * nick of the oberserver
	 */
	private String nickname;
	
	public ObserverConnection(String n){
		nickname=nickname;
	}
	
	public void initializeStreamingService(DatagramSocket ds, int port, InetAddress remoteIP) {
		streamingService =new StreamingService(ds,port,remoteIP);
	}
	public StreamingService getStreamingService() {
		return streamingService;
	}
}
