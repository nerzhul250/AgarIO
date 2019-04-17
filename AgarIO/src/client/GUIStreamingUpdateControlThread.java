package client;

import gameServer.PlayerConnection;
import javafx.application.Platform;
import registrationManagement.Server;

public class GUIStreamingUpdateControlThread extends Thread{
	
	private StreamingController sc;
	
	public GUIStreamingUpdateControlThread(StreamingController sc) {
		this.sc=sc;
	}

	
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
