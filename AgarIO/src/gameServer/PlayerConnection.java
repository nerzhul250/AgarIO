package gameServer;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import gameModel.Game;

public class PlayerConnection implements Runnable {
	
	public final static String WAITMESSAGE="W";
	public final static String RUNNINGMESSAGE="R";
	public final static String FINALMESSAGE="F";	
	
	public final static int DELAY=100;
	
	private Socket socket;
	private ObjectOutputStream oos;
	private BufferedReader in;
	
	private GameHoster gameHoster;
	private String nickname;
	private int id;
	private boolean isPlayerConnected;
	
	public PlayerConnection(Socket accept, GameHoster gh,int id) throws IOException {
		socket=accept;
		oos=new ObjectOutputStream(socket.getOutputStream());
		in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		gameHoster=gh;
		this.id=id;
		isPlayerConnected=true;
		setNickname();
	}
	
	@Override
	public void run() {
		try {
			while(isPlayerConnected) {
				String[] coordinate=in.readLine().split(":");
				double w=Double.parseDouble(coordinate[0]);
				double h=Double.parseDouble(coordinate[1]);
				double W=Double.parseDouble(coordinate[2]);
				double H=Double.parseDouble(coordinate[3]);
				double x0=gameHoster.getGame().players.get(id).getPosition().x;
				double y0=gameHoster.getGame().players.get(id).getPosition().y;
				double r=gameHoster.getGame().players.get(id).getRadius();
				double X=2*r+Game.XPadding*2;
				double Y=2*r+Game.YPadding*2;
				double x=(X/W)*(w+(W*x0/X)-(W/2));
				double y=(Y/H)*(h+(H*y0/Y)-(H/2));
				gameHoster.idIsMovingTo(id,(int)x,(int)y);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void rejectConnection() throws IOException {
		sendData(PlayerConnection.FINALMESSAGE);
	}
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname() throws IOException {
		System.out.println("USERRa");
		nickname=in.readLine();
		System.out.println("USERRd");
	}
	
	public void sendData(Object o) throws IOException {
        oos.writeObject(o);
        oos.reset();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
