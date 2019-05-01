package serverChat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerChat extends Thread{

	public final static int PORT = 1224;
	
	private ServerSocket server;
	
	private ChatMessages lastChatMessage;
	
	boolean serverStatus;
	
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
	
	public void takeDownServer() {
		serverStatus = false;
	}
}
