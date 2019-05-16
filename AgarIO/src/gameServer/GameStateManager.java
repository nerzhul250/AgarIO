package gameServer;

import java.io.IOException;
import java.util.Iterator;

import gameModel.Coordinate;
import gameModel.Game;
import gameModel.GameObject;
import gameModel.Player;
import registrationManagement.Server;

/**
 * Thread in charge of multicasting the current gameState to all connectedPlayers in
 * a gameHoster
 * @author Usuario
 */
public class GameStateManager implements Runnable {
	/**
	 * the game hoster associated
	 */
	private GameHoster gamehoster;
	/**
	 * Constructor
	 * @param gamehoster
	 */
	public GameStateManager(GameHoster gamehoster) {
		this.gamehoster=gamehoster;
	}
	
	@Override
	public void run() {
		try {
			sendAwaitMessage();
			sendGameState();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * sends the message to wait
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void sendAwaitMessage() throws IOException, InterruptedException {
		boolean awaitCompleted=true;
		long startTime=System.currentTimeMillis();
		while(!gamehoster.IsRunning()) {
			if(System.currentTimeMillis()-startTime>Server.MAXWAITTIME) {awaitCompleted=false;break;}
			for (int i = 0; i < gamehoster.getPlayerConnections().size(); i++) {
				gamehoster.getPlayerConnections().get(i).sendMessage(PlayerConnection.WAITMESSAGE);
			}
			
			Thread.sleep(Server.GAMEPACE);
		 }
		if(awaitCompleted) {
			for (int i = 0; i < gamehoster.getPlayerConnections().size(); i++) {
				gamehoster.getPlayerConnections().get(i).sendMessage(PlayerConnection.RUNNINGMESSAGE);
			}
			(new Thread(gamehoster.getGame())).start();
		}else {
			for (int i = 0; i < gamehoster.getPlayerConnections().size(); i++) {
				gamehoster.getPlayerConnections().get(i).sendMessage(PlayerConnection.FINALMESSAGE);
				gamehoster.getPlayerConnections().get(i).sendFinalMessage(PlayerConnection.FINALMESSAGE,PlayerConnection.FINALMESSAGE,"you are off");
			}
			gamehoster.powerOff();
		}
	}
	/**
	 * sends the state of the game to the player
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private void sendGameState() throws InterruptedException, IOException {
		long startTime=System.currentTimeMillis();
		System.out.println("SENDING GAMESTATE");
		while(gamehoster.IsRunning()) {
			if(System.currentTimeMillis()-startTime>Server.MAXPLAYTIME) {break;}
			for (int i = 0; i < gamehoster.getPlayerConnections().size(); i++) {
				gamehoster.getPlayerConnections().get(i).sendGameState(gamehoster.getGame().getObjectsState());
				if(!gamehoster.getGame().players.get(gamehoster.getPlayerConnections().get(i).getId()).isAlive()) {
					PlayerConnection con=gamehoster.getPlayerConnections().remove(i);
					
					i--;
					con.sendFinalMessage(PlayerConnection.FINALMESSAGE,PlayerConnection.LOSTMESSAGE,"Te han comido");
				}
			}
			for (int i = 0; i < gamehoster.getObserverConnections().size(); i++) {
				gamehoster.getObserverConnections().get(i).getStreamingService().sendGameState(gamehoster.getGame().getObjectsState());
			}
			Thread.sleep(Server.GAMEPACE);
		}
		gamehoster.getGame().guardarListaPodium();
		System.out.println("STOP SENDING GAMESTATE");
		for (int i = 0; i < gamehoster.getPlayerConnections().size(); i++) {
			if(gamehoster.getGame().getGreatestScorer()==gamehoster.getPlayerConnections().get(i).getId()) {
				gamehoster.getPlayerConnections().get(i).sendFinalMessage(PlayerConnection.FINALMESSAGE,PlayerConnection.WINMESSAGE,
						"El ganador ha sido "+gamehoster.getPlayerConnections().get(i).getNickname());	
			}else {
				gamehoster.getPlayerConnections().get(i).sendFinalMessage(PlayerConnection.FINALMESSAGE,PlayerConnection.LOSTMESSAGE,
						"El ganador ha sido "+gamehoster.getPlayerConnections().get(gamehoster.getGame().getGreatestScorer()).getNickname());								
			}
		}
		for (int i = 0; i < gamehoster.getObserverConnections().size(); i++) {
			gamehoster.getObserverConnections().get(i).getStreamingService().sendMessage(PlayerConnection.FINALMESSAGE);
		}
		gamehoster.powerOff();
	}

}
