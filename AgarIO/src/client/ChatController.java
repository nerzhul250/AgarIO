package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import serverChat.ServerChat;

public class ChatController {
	/**
	 * Socket for the server
	 */
	private Socket socketToServer;
	/**
	 * DataInputStream to read
	 */
	private DataInputStream inputStream;
	/**
	 * DataOutputStream to send
	 */
	private DataOutputStream outputStream;
	/**
	 * Name of the user
	 */
	private String userName;
	/**
	 * Chat area
	 */
	private TextArea areaOfChat;
	/**
	 * Constructor
	 */
	public ChatController (String userNickName, TextArea area) {
		try {
			socketToServer = new Socket(Controller.IP_DIRECTION, ServerChat.PORT);
			userName = userNickName;
			inputStream = new DataInputStream(socketToServer.getInputStream());
			outputStream = new DataOutputStream(socketToServer.getOutputStream());
			areaOfChat = area;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Alert al = new Alert(AlertType.ERROR);
			al.setTitle("Error en la conexión");
			al.setHeaderText("Problemas con el servidor");
			al.setContentText("No se ha podido establecer conexión con el servidor de chat.");
			al.showAndWait();
		}
	}
	/**
	 * Method that send the message
	 */
	public void sendMessage (String message) {
		try {
			outputStream.writeUTF(userName + ": " +message + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Alert al = new Alert(AlertType.ERROR);
			al.setTitle("Error en la conexión");
			al.setHeaderText("Problemas con el servidor");
			al.setContentText("Se ha perdido conexión con el servidor de chat.");
			al.showAndWait();
		}
	}
	/**
	 * Method that recive the message
	 */
	public void startReceivingMessages () {
		Thread threadReceive = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					try {
						String messageReceived = inputStream.readUTF();
						areaOfChat.appendText(messageReceived);
					} catch (IOException e) {
						Alert al = new Alert(AlertType.ERROR);
						al.setTitle("Error en la conexión");
						al.setHeaderText("Problemas con el servidor");
						al.setContentText("Se ha perdido conexión con el servidor de chat.");
						al.showAndWait();
						break;
					}
				}
				
			}
		});
		
		threadReceive.start();
	}
}
