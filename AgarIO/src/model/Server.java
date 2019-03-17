package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TreeSet;

public class Server {

	public static final int PORT_RECEIVE = 8000;
	public static final int MAXPLAYERNUM=5;
	public static final int MAXGAMEHOSTERSNUM=1;
	public static final int MINPLAYERNUM=2;
	
	private boolean serverIsOn;
	private static  ServerSocket serverSocketReceived;
	
	private TreeSet<GameHoster> gameHosters;
	private DataBaseManager dbm;
	
	public Server() throws IOException {
		gameHosters=new TreeSet<GameHoster>();
		dbm=new DataBaseManager();
		serverIsOn=true;
		serverSocketReceived=new ServerSocket(PORT_RECEIVE);
	}
	
	private Socket getClientConnection() throws IOException {
		return serverSocketReceived.accept();
	}


	public synchronized void ReorderGameHoster(GameHoster gameHoster) {
		gameHosters.remove(gameHoster);
		gameHosters.add(gameHoster);
	}
	
	public static void main(String[] args) {
		try {
			Server server = new Server();
			while(server.serverIsOn) {
				Socket client=server.getClientConnection();
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
}