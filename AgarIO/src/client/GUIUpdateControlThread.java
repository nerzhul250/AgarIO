package client;

import java.io.IOException;

import gameModel.Game;
import gameServer.PlayerConnection;
import javafx.application.Platform;

public class GUIUpdateControlThread extends Thread{
	private final static long UPDATE_SLEEP_TIME = 5;
	private Controller controller;
	
	public GUIUpdateControlThread(Controller c) {
		controller=c;
	}
	
	public void run() {
		Object info;
		try {
			int id=(int) controller.getMessage();
			controller.setId(id);
			info = controller.getMessage();
			String m=(String)info;
			while(m.equals(PlayerConnection.WAITMESSAGE)) {sleep(UPDATE_SLEEP_TIME);m=(String)controller.getMessage();}
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true) {
			try {
				info=controller.getMessage();
				String m;
				try {
					m=(String)info;					
				}catch(Exception e) {
					m="";
				}
				if(m.equals(PlayerConnection.FINALMESSAGE))break;
				controller.updateGame((Game)info);
				GUIUpdateRunnable gur = new GUIUpdateRunnable(controller);
				Platform.runLater(gur);
				sleep(UPDATE_SLEEP_TIME);
			} catch (InterruptedException | IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}