package serverChat;

import java.util.Observable;

public class ChatMessages extends Observable{
	/**
	 * Last message
	 */
	private String lastMessage;
	/**
	 * Constructor clas
	 */
	public ChatMessages() {
		lastMessage = "";
	}
	/**
	 *Set the message
	 */
	public void setMessage(String message) {
		lastMessage = message;
		
		this.setChanged();
		
		this.notifyObservers(lastMessage);
	}
}
