package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StreamingController implements Initializable {
	
	public static final String OBSERVERPANELOCATION = "/view/ObserverPane.fxml";
	
	
	/**
	 * buffer to transmit movements
	 */
	private BufferedWriter transmitMovements;
	/**
	 * buffer to receive the game
	 */
	private BufferedReader receiveGame;
	/**
	 * socket to conect
	 */
	private Socket socketGame;
	
	
	@FXML
	private Pane observerPane;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	public void openObserverPane() {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(OBSERVERPANELOCATION));
			loader.setController(this);
			root = loader.load();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.show();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public void startStreaming(int portGameHoster, String nickname) {
		try {
			socketGame=new Socket(Controller.IP_DIRECTION,portGameHoster);
			receiveGame=new BufferedReader(new InputStreamReader(socketGame.getInputStream()));
			transmitMovements=new BufferedWriter(new OutputStreamWriter(socketGame.getOutputStream()));
			transmitMovements.write(nickname+"\n");
			transmitMovements.flush();
			System.out.println("GameStarting");
			(new GUIStreamingUpdateControlThread(this)).start();
			Thread.sleep(2000);
		} catch (InterruptedException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
