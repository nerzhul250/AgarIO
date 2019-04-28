package client;

public class GUIStreamingUpdateRunnable implements Runnable {
	/**
	 * Streaming controller
	 */
	private StreamingController sc;
	/**
	 * Message of the streaming
	 */
	private String info;
	/**
	 * Constructor of the Streming runnable update GUI
	 */
	public GUIStreamingUpdateRunnable(StreamingController sc, String info) {
		this.sc=sc;
		this.info=info;
	}
/**
 * Starts the update
 */
	@Override
	public void run() {
		sc.updateGUI(info);
	}

}
