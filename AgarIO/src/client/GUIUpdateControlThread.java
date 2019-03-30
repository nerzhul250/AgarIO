package client;

import java.io.IOException;

import gameModel.Game;
import gameServer.PlayerConnection;
import javafx.application.Platform;

public class GUIUpdateControlThread extends Thread{
	private Controller controller;
	
	
	public GUIUpdateControlThread(Controller c) {
		controller=c;
	}
	
	public void run() {
		try {
			String info="";
			int id=Integer.parseInt(controller.getMessage());
			controller.setId(id);
			info = controller.getMessage();
			String m=info;
			while(m.equals(PlayerConnection.WAITMESSAGE)) {m=controller.getMessage();}
			while (true) {
				Thread.sleep(50);
				info=controller.getMessage();
				if(m.equals(PlayerConnection.FINALMESSAGE))break;
				GUIUpdateRunnable gur = new GUIUpdateRunnable(controller,info);
				Platform.runLater(gur);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}