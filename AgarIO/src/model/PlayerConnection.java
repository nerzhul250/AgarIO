package model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class PlayerConnection implements Runnable {
	
	private Socket socket;
	private GameHoster gameHoster;
	
	public PlayerConnection(Socket accept, GameHoster gh) {
		socket=accept;
		gameHoster=gh;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public void rejectConnection() {
		// TODO Auto-generated method stub
		
	}

	public void sendMessage(String string) throws IOException {
		DataOutputStream out;
		out = new DataOutputStream(socket.getOutputStream());
		out.writeUTF(string);
	}

	public void sendData(Object o) throws IOException {
		// get the output stream from the socket.
        OutputStream outputStream = socket.getOutputStream();
        // create an object output stream from the output stream so we can send an object through it
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(o);
	}

}
