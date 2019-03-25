package registrationManagement;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TreeSet;

import javax.net.ssl.SSLServerSocketFactory;

import gameServer.GameHoster;

public class Server {

	public static final int PORT_RECEIVE =8000;
	public static final int MAXPLAYERNUM=5;
	public static final int MAXGAMEHOSTERSNUM=1;
	public static final int MINPLAYERNUM=2;
	
	public static final String KEYSTORE_LOCATION = "..\\keystore.jks";
	public static final String KEYSTORE_PASSWORD = "shwq1998";
	
	private boolean serverIsOn;
	private static  ServerSocket serverSocketReceived;
	
	private TreeSet<GameHoster> gameHosters;
	private DataBaseManager dbm;
	
	public Server() throws IOException {
		gameHosters=new TreeSet<GameHoster>();
		dbm=new DataBaseManager();
		serverIsOn=true;
		
		System.setProperty("javax.net.ssl.keyStore", KEYSTORE_LOCATION);
		System.setProperty("javax.net.ssl.keyStorePassword", KEYSTORE_PASSWORD);
		SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		
		serverSocketReceived=ssf.createServerSocket(PORT_RECEIVE);
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

	public synchronized int getAvailableGameHoster() throws IOException {
		if(gameHosters.first().IsGameFull()) {
			gameHosters.add(new GameHoster(new ServerSocket(0),MAXPLAYERNUM,MINPLAYERNUM,this));
			(new Thread(gameHosters.first())).start();
			return gameHosters.first().getLocalPort();
		}else {
			return gameHosters.first().getLocalPort();
		}
	}
}