package serverChat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerChat extends Thread{
	/**
	 * Port of the server chat
	 */
	public final static int PORT = 1224;
	/**
	 * Server socket
	 */
	private ServerSocket server;
	/**
	 * Chat messages
	 */
	private ChatMessages lastChatMessage;
	/**
	 * Status of the server
	 */
	boolean serverStatus;
	/**
	 * Constructor of the class
	 */
	public ServerChat() {
		lastChatMessage = new ChatMessages();
		
		serverStatus = true;
		try {
			server = new ServerSocket(PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Server Down");
			try {
				server.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	/**
	 * Method that initiate the server chat
	 */
	@Override
	public void run() {
		while (serverStatus) {
			Socket socketWithClient;
			try {
				socketWithClient = server.accept();
				ClientConnection cc = new ClientConnection(socketWithClient, lastChatMessage);
				(new Thread(cc)).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Server down");
				try {
					server.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		}
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Turn down the server
	 */
	public void takeDownServer() {
		serverStatus = false;
	}
}
