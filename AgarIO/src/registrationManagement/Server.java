package registrationManagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TreeSet;

import javax.net.ssl.SSLServerSocketFactory;

import gameServer.GameHoster;
/**
 * Represents the server
 * @author Steven
 *
 */
public class Server {
	/**
	 * port through the server listens
	 */
	public static final int PORT_RECEIVE =8000;
	/**
	 * max number of players
	 */
	public static final int MAXPLAYERNUM=5;
	/**
	 * min number of players
	 */
	public static final int MINPLAYERNUM=2;
	/**
	 * maximun of lobbies
	 */
	public static final int MAXGAMEHOSTERSNUM=1;
	/**
	 * the maximun time of waiting for players
	 */
	public static final long MAXWAITTIME=120000;
	/**
	 * the maximun time of playing
	 */
	public static final long MAXPLAYTIME=300000;
	/**
	 * game space
	 */
	public static final long GAMEPACE=50;
	/**
	 * location of the key
	 */
	public static final String KEYSTORE_LOCATION = "./keyStore/keystore.jks";
	/**
	 * password of the key
	 */
	public static final String KEYSTORE_PASSWORD = "shwq1998";
	/**
	 * is the server on?
	 */
	private boolean serverIsOn;
	/**
	 * the server socket
	 */
	private static  ServerSocket serverSocketReceived;
	/**
	 * the lobbies
	 */
	private TreeSet<GameHoster> gameHosters;
	/**
	 * the data base manager
	 */
	private DataBaseManager dbm;
	/**
	 * Constructor
	 * @throws IOException
	 */
	public Server() throws IOException {
		gameHosters=new TreeSet<GameHoster>();
		dbm=new DataBaseManager();
		serverIsOn=true;
		
		System.setProperty("javax.net.ssl.keyStore", KEYSTORE_LOCATION);
		System.setProperty("javax.net.ssl.keyStorePassword", KEYSTORE_PASSWORD);
		SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		
		serverSocketReceived=ssf.createServerSocket(PORT_RECEIVE);
	}
	/**
	 * accepts a client
	 * @return
	 * @throws IOException
	 */
	private Socket getClientConnection() throws IOException {
		return serverSocketReceived.accept();
	}
	
	/**
	 * adds a game hoster
	 * @param gameHoster
	 */
	public synchronized void ReorderGameHoster(GameHoster gameHoster) {
		gameHosters.remove(gameHoster);
		gameHosters.add(gameHoster);
	}
	
	public static void main(String[] args) {
		try {
			Server server = new Server();
			while(server.serverIsOn) {
				Socket client=server.getClientConnection();
				(new Thread(new ClientAttendant(client,server))).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public DataBaseManager getDbm() {
		return dbm;
	}

	public void setDbm(DataBaseManager dbm) {
		this.dbm = dbm;
	}
	/**
	 * returns an available game hoster
	 * @return
	 * @throws Exception
	 */
	public synchronized int getAvailableGameHoster() throws Exception {
		if(gameHosters.isEmpty() || gameHosters.first().IsGameFull() ) {
			if(gameHosters.size()+1>MAXGAMEHOSTERSNUM)throw new Exception("Failed");
			gameHosters.add(new GameHoster(new ServerSocket(0),MAXPLAYERNUM,MINPLAYERNUM,this));
			(new Thread(gameHosters.first())).start();
			return gameHosters.first().getLocalPort();
		}else {
			return gameHosters.first().getLocalPort();
		}
	}
	public int getGameHoster(int i) {
		return gameHosters.first().getLocalPort();
	}
}