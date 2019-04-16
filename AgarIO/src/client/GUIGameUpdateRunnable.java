package client;
/**
 * the thread that updates the GUI
 * @author Steven
 *
 */
public class GUIGameUpdateRunnable implements Runnable{
	/**
	 * the controller of the user
	 */
	private Controller controller;
	/**
	 * info to be showed
	 */
	private String info;
	/**
	 * 
	 */
	private int op;
	/**
	 * construct
	 * @param c
	 * @param info
	 * @param op
	 */
	public GUIGameUpdateRunnable(Controller c, String info,int op) {
		controller=c;
		this.info=info;
		this.op=op;
	}
	
	@Override
	public void run() {
		if(op==1) {
			controller.updateGUI(info);			
		}else if(op==2){
			controller.showWinMessage(info);
		}else if(op==3){
			controller.showLoseMessage(info);
		}else if(op==4){
			controller.showDisconnectMessage(info);
		}
	}
}
