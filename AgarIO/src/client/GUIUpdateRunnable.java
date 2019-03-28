package client;

public class GUIUpdateRunnable implements Runnable{
	private Controller controller;
	public GUIUpdateRunnable(Controller c) {
		controller=c;
	}
	
	@Override
	public void run() {
		controller.updateGUI();
	}
}
