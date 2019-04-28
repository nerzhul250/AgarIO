package gameServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import gameModel.Game;
/**
 * represents a player connection
 * @author Steven
 *
 */
public class PlayerConnection implements Runnable {
	/**
	 * represents the wait message
	 */
	public final static String WAITMESSAGE="WA";
	/**
	 * represents the running message
	 */
	public final static String RUNNINGMESSAGE="R";
	/**
	 * represents the final message
	 */
	public final static String FINALMESSAGE="F";
	/**
	 * represents the lost message
	 */
	public final static String LOSTMESSAGE="L";
	/**
	 * represents the win message
	 */
	public final static String WINMESSAGE="WI";
	
	/**
	 * Socket that connects the player to the server
	 */
	private Socket socket;
	/**
	 * Buffered to write to the server
	 */
	private BufferedWriter out;
	/**
	 * buffered through the player listens
	 */
	private BufferedReader in;
	/**
	 * the game hoster that the player is connected
	 */
	private GameHoster gameHoster;
	/**
	 * nick of the player
	 */
	private String nickname;
	/**
	 * id of the plyaer
	 */
	private int id;
	/**
	 * state of the connection
	 */
	private boolean isPlayerConnected;
	/**
	 * Constructor of the player connection
	 * @param accept socket
	 * @param gh game hoster
	 * @param id id of the player
	 * @param nickName2 
	 * @throws IOException
	 */
	public PlayerConnection(Socket accept, GameHoster gh,int id, String nickName2) throws IOException {
		socket=accept;
		out=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		gameHoster=gh;
		this.id=id;
		isPlayerConnected=true;
		nickname=nickName2;
	}
	/**
	 * Starts the position of the players that is hosting
	 */
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
	/**
	 * sends the final message
	 * @param m1
	 * @param m2
	 * @param m3
	 * @throws IOException
	 */
	public void sendFinalMessage(String m1,String m2,String m3) throws IOException {
		sendMessage(m1);
		sendMessage(m2);
		sendMessage(m3);
	}
	/**
	 * returns the nickname
	 * @return
	 */
	public String getNickname() {
		return nickname;
	}
	/**
	 * sends a message to the server
	 * @param m
	 * @throws IOException
	 */
	public void sendMessage(String m) throws IOException {
		out.write(m+"\n");
		out.flush();
	}
	/**
	 * sends the state of the player
	 * @param data
	 * @throws IOException
	 */
	public void sendGameState(String data) throws IOException {
		int x=gameHoster.getGame().players.get(id).getPosition().x;
		int y=gameHoster.getGame().players.get(id).getPosition().y;
		double r=gameHoster.getGame().players.get(id).getRadius();
        out.write(x+":"+y+":"+r+":"+data+"\n");
        out.flush();
	}
	/**
	 * returns the id of the player
	 * @return
	 */
	public int getId() {
		return id;
	}
	/**
	 * sets the id 
	 *
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	
}
