package client;

public class GUIUpdateRunnable implements Runnable{
	private Controller controller;
	private Object info;
	public GUIUpdateRunnable(Controller c, Object info) {
		controller=c;
		this.info=info;
	}
	
	@Override
	public void run() {
		controller.updateGUI(info);
	}
}
