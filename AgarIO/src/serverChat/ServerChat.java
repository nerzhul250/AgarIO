package serverChat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerChat {

	public final static int PORT = 1224;
	
	private ServerSocket server;
	
	private ChatMessages lastChatMessage;
	
	public ServerChat() {
		lastChatMessage = new ChatMessages();
		try {
			server = new ServerSocket(PORT);
			
			while (true) {
				Socket socketWithClient = server.accept();
				
				ClientConnection cc = new ClientConnection(socketWithClient, lastChatMessage);
				(new Thread(cc)).start();
			}
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
}
