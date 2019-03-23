package model;

import java.io.IOException;

/**
 * Thread in charge of multicasting the current gameState to all connectedPlayers in
 * a gameHoster
 * @author Usuario
 *
 */
public class GameStateManager implements Runnable {
	
	public final static int REFRESHDELAY=1000;
	
	private GameHoster gamehoster;
	
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
	
	private void sendAwaitMessage() throws IOException, InterruptedException {
		while(!gamehoster.IsRunning()) {
			for (int i = 0; i < gamehoster.getPlayerConnections().size(); i++) {
				gamehoster.getPlayerConnections().get(i).sendMessage("W");
			}
			Thread.sleep(REFRESHDELAY);
		}	
		for (int i = 0; i < gamehoster.getPlayerConnections().size(); i++) {
			gamehoster.getPlayerConnections().get(i).sendMessage("R");
		}
	}

	private void sendGameState() throws InterruptedException, IOException {
		while(gamehoster.IsRunning()) {
			Object o=gamehoster.getGame();
			for (int i = 0; i < gamehoster.getPlayerConnections().size(); i++) {
				gamehoster.getPlayerConnections().get(i).sendData(o);
			}
			Thread.sleep(REFRESHDELAY);
		}
		for (int i = 0; i < gamehoster.getPlayerConnections().size(); i++) {
			gamehoster.getPlayerConnections().get(i).sendMessage("E");
		}
	}

}
