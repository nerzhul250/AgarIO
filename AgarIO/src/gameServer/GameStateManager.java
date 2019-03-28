package gameServer;

import java.io.IOException;

/**
 * Thread in charge of multicasting the current gameState to all connectedPlayers in
 * a gameHoster
 * @author Usuario
 *
 */
public class GameStateManager implements Runnable {
	
	public final static int REFRESHDELAY=40;
	
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
		boolean awaitCompleted=true;
		long startTime=System.currentTimeMillis();
		while(!gamehoster.IsRunning()) {
			if(System.currentTimeMillis()-startTime>120000) {awaitCompleted=false;break;}
			for (int i = 0; i < gamehoster.getPlayerConnections().size(); i++) {
				gamehoster.getPlayerConnections().get(i).sendData(PlayerConnection.WAITMESSAGE);
			}
			Thread.sleep(REFRESHDELAY);
		 }
		if(awaitCompleted) {
			for (int i = 0; i < gamehoster.getPlayerConnections().size(); i++) {
				gamehoster.getPlayerConnections().get(i).sendData(PlayerConnection.RUNNINGMESSAGE);
			}
			(new Thread(gamehoster.getGame())).start();
		}else {
			for (int i = 0; i < gamehoster.getPlayerConnections().size(); i++) {
				gamehoster.getPlayerConnections().get(i).sendData(PlayerConnection.FINALMESSAGE);
			}
		}
	}

	private void sendGameState() throws InterruptedException, IOException {
		long startTime=System.currentTimeMillis();
		while(gamehoster.IsRunning()) {
			if(System.currentTimeMillis()-startTime>300000) {break;}
			Object o=gamehoster.getGame();
			for (int i = 0; i < gamehoster.getPlayerConnections().size(); i++) {
				gamehoster.getPlayerConnections().get(i).sendData(o);
			}
			Thread.sleep(REFRESHDELAY);
		}
		for (int i = 0; i < gamehoster.getPlayerConnections().size(); i++) {
			gamehoster.getPlayerConnections().get(i).sendData(PlayerConnection.FINALMESSAGE);
		}
		gamehoster.powerOff();
	}

}
