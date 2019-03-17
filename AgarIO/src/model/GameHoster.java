package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class GameHoster implements Runnable, Comparable<GameHoster> {
	private int max_player_number;
	private int min_player_number;
	
	private Server server;
	
	private ServerSocket serverSocket;
	private ArrayList<PlayerConnection> playerConnections;
	
	private Game gameState;

	public GameHoster(int port,int maxPlayerNumber,int minPlayerNumber,Server s) throws IOException {
		max_player_number=maxPlayerNumber;
		min_player_number=minPlayerNumber;
		
		playerConnections=new ArrayList<PlayerConnection>();
		server=s;
		
		gameState=new Game();
		serverSocket = new ServerSocket(port);
	}
	
	@Override
	public void run() {
		try {
			(new Thread(new GameStateRefresher(this))).start();
			while(GameIsOpen()) {
				PlayerConnection pc=new PlayerConnection(serverSocket.accept(),this);
				if(!GameIsFull()) {
					addPlayer(pc);
				}else {
					pc.rejectConnection();
				}
			}
		}catch(IOException ioe) {
			
		}
	}

	private void addPlayer(PlayerConnection pc) {
		playerConnections.add(pc);
		server.ReorderGameHoster(this);
	}

	private boolean GameIsFull() {
		return playerConnections.size()==max_player_number;
	}

	private boolean GameIsOpen() {
		// TODO Auto-generated method stub
		return false;
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

	public boolean IsRunning() {
		return playerConnections.size()>=min_player_number;
	}

	public Object getGame() {
		return gameState;
	}
	
	public ArrayList<PlayerConnection> getPlayerConnections() {
		return playerConnections;
	}

	public void setPlayerConnections(ArrayList<PlayerConnection> playerConnections) {
		this.playerConnections = playerConnections;
	}
}
