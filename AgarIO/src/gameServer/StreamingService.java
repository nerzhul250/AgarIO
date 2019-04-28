package gameServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class StreamingService {
	/**
	 * data gram socket
	 */
	private DatagramSocket streamingEnd;
	/**
	 * remote port
	 */
	private int remotePort;
	/**
	 * inetaddress
	 */
	private InetAddress remoteIP;
	/**
	 * Constructor of the class
	 */
	public StreamingService(DatagramSocket ds, int port, InetAddress remoteIP) {
		streamingEnd=ds;
		remotePort=port;
		this.remoteIP=remoteIP;
	}
	/**
	 * methot that send the state of the game
	 */
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
	/**
	 * method that send the information of the streaming
	 */
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
