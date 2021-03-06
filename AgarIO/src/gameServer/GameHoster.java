package gameServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import gameModel.Game;
import registrationManagement.Server;
import serverChat.ServerChat;
/**
 * Represents a game
 * @author Steven
 *
 */
public class GameHoster implements Runnable, Comparable<GameHoster> {
	public final static String OBSERVER="Ob";
	public final static String PLAYER="Pl";
	/**
	 * the maxium number of players
	 */
	private int max_player_number;
	/**
	 * the minimun number of players
	 */
	private int min_player_number;
	/**
	 * the time that the game started
	 */
	private long gameStartTime;
	/**
	 * index of the lobby
	 */
	private int index;
	/**
	 * represents if the game is open
	 */
	private boolean gameIsOpen;
	/**
	 * is the server of this game
	 */
	private Server server;
	/**
	 * the server socket where the server listens
	 */
	private ServerSocket serverSocket;
	/**
	 * the DatagramSocket where the server transmits the udpStreaming
	 */
	private  DatagramSocket udpStreaming;
	/**
	 * list of the connections of all players
	 */
	private ArrayList<PlayerConnection> playerConnections;
	/**
	 * list of the connections of all Observers
	 */
	private ArrayList<ObserverConnection> observersConnections;
	/**
	 * the logical part of a game
	 */
	private Game gameState;
	
	private ServerChat chat;
	//TODO
//	private HashMap<Integer, ThreadAudioServerUDP> threadsMusicPlayers;
	/**
	 * Constructor of a game hoster
	 * @param ss
	 * @param maxPlayerNumber
	 * @param minPlayerNumber
	 * @param s server
	 * @throws IOException
	 */
	public GameHoster(ServerSocket ss,int maxPlayerNumber,int minPlayerNumber,Server s) throws IOException {
		max_player_number=maxPlayerNumber;
		min_player_number=minPlayerNumber;
		gameIsOpen=true;
		
//		threadsMusicPlayers= new HashMap<Integer, ThreadAudioServerUDP>();
		
		playerConnections=new ArrayList<PlayerConnection>();
		observersConnections=new ArrayList<ObserverConnection>();
		server=s;
		chat = new ServerChat();
		chat.start();
		
		gameState=new Game();
		serverSocket=ss;
		udpStreaming=new DatagramSocket(ss.getLocalPort());
	}
	/**
	 * method that inicializate the server
	 */
	@Override
	public void run() {
		gameStartTime=System.currentTimeMillis();
		try {
			(new Thread(new GameStateManager(this))).start();
			while(GameIsOpen()) {
				System.out.println("SERVERUP");
				Socket s=serverSocket.accept();
				
				BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
				BufferedWriter bw= new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				//STEVEN
				String[] ports = br.readLine().split(" ");
//				System.out.println(ports[0]+" "+ ports[1]);
				
				//STEVEN
				String type= br.readLine();
				String nickName=br.readLine();
				if(type.equals(PLAYER)) {
					PlayerConnection pc=new PlayerConnection(s,this,index++,nickName,Integer.parseInt(ports[0]),Integer.parseInt(ports[1]));
					//STEVEN
					bw.write(pc.getChangingSocketPort()+"\n");
					System.out.println("changing: "+pc.getChangingSocketPort());
					bw.flush();
					//STEVEN
					System.out.println(pc.getId());
					if(!IsGameFull()) {
						addPlayer(pc);
					}else {
						pc.sendMessage(PlayerConnection.FINALMESSAGE);
						pc.sendFinalMessage(PlayerConnection.FINALMESSAGE,PlayerConnection.FINALMESSAGE,"Desconectado");
					}
				}else if(type.equals(OBSERVER)) {
					ObserverConnection oc=new ObserverConnection(nickName);
					oc.initializeStreamingService(udpStreaming,s.getPort(),s.getInetAddress());
					observersConnections.add(oc);
				}
				
			}
			chat.takeDownServer();
		}catch(IOException ioe) {
			
		}
	}
	/**
	 * adds one player to the lobbys
	 * @param pc
	 */
	private void addPlayer(PlayerConnection pc) {
		playerConnections.add(pc);
		gameState.addNewPlayer(pc.getId(),pc.getNickname());
		(new Thread(pc)).start();
		server.ReorderGameHoster(this);
	}
	/**
	 * returns if the game is full
	 * @return
	 */
	public boolean IsGameFull() {
		return playerConnections.size()==max_player_number || System.currentTimeMillis()-gameStartTime>=Server.MAXWAITTIME;
	}
	/**
	 * returns if the game is open
	 * @return
	 */
	private boolean GameIsOpen() {
		return gameIsOpen;
	}
	
	@Override
	public int compareTo(GameHoster o) {
		Integer i1=playerConnections.size();
		Integer i2=o.playerConnections.size();
		return i1.compareTo(i2);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}
	/**
	 * the game fulfill the minimun number of players
	 * @return
	 */
	public boolean IsRunning() {
		return playerConnections.size()>=min_player_number;
	}
	/**
	 * returns the state of the game (logical part)
	 * @return
	 */
	public Game getGame() {
		return gameState;
	}
	/**
	 * returns the list of the connections
	 * @return
	 */
	public ArrayList<PlayerConnection> getPlayerConnections() {
		return playerConnections;
	}
	public ArrayList<ObserverConnection> getObserverConnections() {
		return observersConnections;
	}
	/**
	 * sets the list of connections
	 * @param playerConnections
	 */
	public void setPlayerConnections(ArrayList<PlayerConnection> playerConnections) {
		this.playerConnections = playerConnections;
	}
	/**
	 * returns the local port
	 * @return
	 */
	public int getLocalPort() {
		return serverSocket.getLocalPort();
	}
	/**
	 * moves one player
	 * @param id
	 * @param x
	 * @param y
	 */
	public synchronized void idIsMovingTo(int id, int x, int y) {
		gameState.setPlayerMovingCoordinate(id,x,y);
	}
	/**
	 * powers off the game
	 */
	public void powerOff() {
		gameIsOpen=false;
	}
}
