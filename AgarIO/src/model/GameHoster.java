package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class GameHoster implements Runnable, Comparable<GameHoster> {
	private int max_player_number;
	private int min_player_number;
	private boolean gameIsOpen;
	
	private Server server;
	
	private ServerSocket serverSocket;
	private ArrayList<PlayerConnection> playerConnections;
	
	private Game gameState;

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
		try {
			(new Thread(new GameStateRefresher(this))).start();
			while(GameIsOpen()) {
				PlayerConnection pc=new PlayerConnection(serverSocket.accept(),this,playerConnections.size()+1);
				if(!IsGameFull()) {
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
		gameState.addNewPlayer(pc.getId());
		(new Thread(pc)).start();
		server.ReorderGameHoster(this);
	}

	public boolean IsGameFull() {
		return playerConnections.size()==max_player_number;
	}

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

	public int getLocalPort() {
		return serverSocket.getLocalPort();
	}

	public synchronized void idIsMovingTo(int id, int x, int y) {
		gameState.movePlayerToCoordinate(id,x,y);
	}
}
