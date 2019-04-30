package serverChat;

import java.util.Observable;

public class ChatMessages extends Observable{

	private String lastMessage;
	
	public ChatMessages() {
		lastMessage = "";
	}
	
	public void setMessage(String message) {
		lastMessage = message;
		
		this.setChanged();
		
		this.notifyObservers(lastMessage);
	}
}
