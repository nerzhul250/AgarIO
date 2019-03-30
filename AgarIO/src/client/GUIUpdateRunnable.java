package client;

public class GUIUpdateRunnable implements Runnable{
	private Controller controller;
	private String info;
	public GUIUpdateRunnable(Controller c, String info) {
		controller=c;
		this.info=info;
	}
	
	@Override
	public void run() {
		controller.updateGUI(info);
	}
}
