package gameServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class PlayerConnection implements Runnable {
	
	public final static int DELAY=100;
	
	private Socket socket;
	private GameHoster gameHoster;
	private String nickname;
	private int id;
	private boolean isPlayerConnected;
	
	public PlayerConnection(Socket accept, GameHoster gh,int id) throws IOException {
		socket=accept;
		gameHoster=gh;
		this.id=id;
		isPlayerConnected=true;
		setNickname();
	}
	
	@Override
	public void run() {
		DataInputStream in;
		try {
			in = new DataInputStream(socket.getInputStream());
			while(isPlayerConnected) {
				String[] coordinate=in.readUTF().split(":");
				int x=Integer.parseInt(coordinate[0]);
				int y=Integer.parseInt(coordinate[1]);
				gameHoster.idIsMovingTo(id,x,y);
				Thread.sleep(DELAY); //VERY DANGEROUSSS
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void rejectConnection() {
		// TODO Auto-generated method stub
		
	}
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname() throws IOException {
		DataInputStream in;
		in = new DataInputStream(socket.getInputStream());
		nickname=in.readUTF();
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
