package client;

import gameServer.PlayerConnection;
import javafx.application.Platform;
import registrationManagement.Server;

public class GUIStreamingUpdateControlThread extends Thread{
	
	/**
	 * Streaming controller
	 */
	private StreamingController sc;
	/**
	 * Constructor of the class that updates the GUI
	 */
	public GUIStreamingUpdateControlThread(StreamingController sc) {
		this.sc=sc;
	}

	/**
	 * Method that starts the streaming
	 */
	public void run() {
		try {
			String info;
			while (true) {
				Thread.sleep(Server.GAMEPACE);
				info=sc.getMessage();
				if(info.equals(PlayerConnection.FINALMESSAGE))break;
				GUIStreamingUpdateRunnable gur = new GUIStreamingUpdateRunnable(sc,info);
				Platform.runLater(gur);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
