package gameServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import gameModel.Game;
import registrationManagement.Server;
/**
 * Represents a game
 * @author Steven
 *
 */
public class GameHoster implements Runnable, Comparable<GameHoster> {
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
	 * list of the connections of all players
	 */
	private ArrayList<PlayerConnection> playerConnections;
	/**
	 * the logical part of a game
	 */
	private Game gameState;
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
		
		playerConnections=new ArrayList<PlayerConnection>();
		server=s;
		
		gameState=new Game();
		serverSocket=ss;
	}
	
	@Override
	public void run() {
		gameStartTime=System.currentTimeMillis();
		try {
			(new Thread(new GameStateManager(this))).start();
			while(GameIsOpen()) {
				System.out.println("SERVERUP");
				PlayerConnection pc=new PlayerConnection(serverSocket.accept(),this,index++);
				System.out.println(pc.getId());
				if(!IsGameFull()) {
					addPlayer(pc);
				}else {
					pc.sendMessage(PlayerConnection.FINALMESSAGE);
					pc.sendFinalMessage(PlayerConnection.FINALMESSAGE,PlayerConnection.FINALMESSAGE,"Desconectado");
				}
			}
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
