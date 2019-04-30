package serverChat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observer;

import javafx.beans.Observable;

public class ClientConnection implements Runnable, Observer{

	private ChatMessages messages;
	Socket conection;
	DataInputStream inputData;
	DataOutputStream outputData;
	
	public ClientConnection(Socket socket, ChatMessages messages) {
		conection = socket;
		this.messages = messages;
		
		try {
			inputData = new DataInputStream(socket.getInputStream());
			outputData = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void update(java.util.Observable arg0, Object arg1) {
		try {
			outputData.writeUTF(arg1.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		String receivedMessage;	
		messages.addObserver(this);
		
		while (true) {
			try {
				receivedMessage = inputData.readUTF();
				
				messages.setMessage(receivedMessage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Client disconected");
				
				try {
					inputData.close();
					outputData.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
	}
}
