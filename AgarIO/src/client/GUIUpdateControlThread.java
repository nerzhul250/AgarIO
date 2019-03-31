package client;


import gameServer.PlayerConnection;
import javafx.application.Platform;
import registrationManagement.Server;

public class GUIUpdateControlThread extends Thread{
	private Controller controller;
	
	
	public GUIUpdateControlThread(Controller c) {
		controller=c;
	}
	
	public void run() {
		try {
			String info="";
			info = controller.getMessage();
			while(info.equals(PlayerConnection.WAITMESSAGE)) {info=controller.getMessage();}
			while (true) {
				Thread.sleep(Server.GAMEPACE);
				info=controller.getMessage();
				if(info.equals(PlayerConnection.FINALMESSAGE))break;
				GUIUpdateRunnable gur = new GUIUpdateRunnable(controller,info,1);
				Platform.runLater(gur);
			}
			info=controller.getMessage();
			if(info.equals(PlayerConnection.WINMESSAGE)) {
				info=controller.getMessage();
				GUIUpdateRunnable gur = new GUIUpdateRunnable(controller,info,2);
				Platform.runLater(gur);
			}else if(info.equals(PlayerConnection.LOSTMESSAGE)) {
				info=controller.getMessage();
				GUIUpdateRunnable gur = new GUIUpdateRunnable(controller,info,3);
				Platform.runLater(gur);
			}else if(info.equals(PlayerConnection.FINALMESSAGE)) {
				info=controller.getMessage();
				GUIUpdateRunnable gur = new GUIUpdateRunnable(controller,info,4);
				Platform.runLater(gur);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}