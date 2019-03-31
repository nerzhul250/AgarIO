package gameServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import gameModel.Game;
import registrationManagement.Server;

public class GameHoster implements Runnable, Comparable<GameHoster> {
	private int max_player_number;
	private int min_player_number;
	private long gameStartTime;
	private int index;
	
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

	private void addPlayer(PlayerConnection pc) {
		playerConnections.add(pc);
		gameState.addNewPlayer(pc.getId(),pc.getNickname());
		(new Thread(pc)).start();
		server.ReorderGameHoster(this);
	}

	public boolean IsGameFull() {
		return playerConnections.size()==max_player_number || System.currentTimeMillis()-gameStartTime>=Server.MAXWAITTIME;
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

	public Game getGame() {
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
		gameState.setPlayerMovingCoordinate(id,x,y);
	}

	public void powerOff() {
		gameIsOpen=false;
	}
}
