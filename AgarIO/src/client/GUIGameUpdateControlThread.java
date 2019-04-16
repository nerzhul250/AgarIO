package client;


import gameServer.PlayerConnection;
import javafx.application.Platform;
import registrationManagement.Server;
/**
 * is the control that updates the GUI
 * @author Steven
 *
 */
public class GUIGameUpdateControlThread extends Thread{
	/**
	 * controller of the user
	 */
	private Controller controller;
	
	/**
	 * constructor
	 * @param c
	 */
	public GUIGameUpdateControlThread(Controller c) {
		controller=c;
	}
	/**
	 * thread to update the gui
	 */
	public void run() {
		try {
			String info="";
			info = controller.getMessage();
			while(info.equals(PlayerConnection.WAITMESSAGE)) {info=controller.getMessage();}
			while (true) {
				Thread.sleep(Server.GAMEPACE);
				info=controller.getMessage();
				if(info.equals(PlayerConnection.FINALMESSAGE))break;
				GUIGameUpdateRunnable gur = new GUIGameUpdateRunnable(controller,info,1);
				Platform.runLater(gur);
			}
			info=controller.getMessage();
			if(info.equals(PlayerConnection.WINMESSAGE)) {
				info=controller.getMessage();
				GUIGameUpdateRunnable gur = new GUIGameUpdateRunnable(controller,info,2);
				Platform.runLater(gur);
			}else if(info.equals(PlayerConnection.LOSTMESSAGE)) {
				info=controller.getMessage();
				GUIGameUpdateRunnable gur = new GUIGameUpdateRunnable(controller,info,3);
				Platform.runLater(gur);
			}else if(info.equals(PlayerConnection.FINALMESSAGE)) {
				info=controller.getMessage();
				GUIGameUpdateRunnable gur = new GUIGameUpdateRunnable(controller,info,4);
				Platform.runLater(gur);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}