package gameServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class StreamingService {
	
	private DatagramSocket streamingEnd;
	private int remotePort;
	private InetAddress remoteIP;
	
	public StreamingService(DatagramSocket ds, int port, InetAddress remoteIP) {
		streamingEnd=ds;
		remotePort=port;
		this.remoteIP=remoteIP;
	}
	
	public void sendGameState(String data) {
		 byte[] enviarDatos =  new byte[20000];
		 enviarDatos=data.getBytes();
		 DatagramPacket enviarPaquete = new DatagramPacket(enviarDatos, enviarDatos.length,remoteIP,remotePort);
		 try {        
			 streamingEnd.send(enviarPaquete);
		 } catch (IOException e){
			 System.out.println("Error al recibir");
			 System.exit ( 0 );
		 }
	}

	public void sendMessage(String finalmessage) {
		 byte[] enviarDatos =  new byte[1024];
		 enviarDatos=finalmessage.getBytes();
		 DatagramPacket enviarPaquete = new DatagramPacket(enviarDatos, enviarDatos.length,remoteIP,remotePort);
		 try {        
			 streamingEnd.send(enviarPaquete);
		 } catch (IOException e){
			 System.out.println("Error al recibir");
			 System.exit ( 0 );
		 }
	}
}
