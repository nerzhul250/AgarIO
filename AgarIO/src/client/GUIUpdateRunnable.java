package client;

public class GUIUpdateRunnable implements Runnable{
	private Controller controller;
	private String info;
	private int op;
	public GUIUpdateRunnable(Controller c, String info,int op) {
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
