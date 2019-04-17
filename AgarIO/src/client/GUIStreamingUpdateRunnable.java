package client;

public class GUIStreamingUpdateRunnable implements Runnable {
	
	private StreamingController sc;
	private String info;
	
	public GUIStreamingUpdateRunnable(StreamingController sc, String info) {
		this.sc=sc;
		this.info=info;
	}

	@Override
	public void run() {
		sc.updateGUI(info);
	}

}
