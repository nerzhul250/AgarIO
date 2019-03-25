package client;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import registrationManagement.Server;

public class RegisterAndLoginController implements Initializable{

	public static String IP_DIRECTION = "localhost";
	public static final String TRUSTTORE_LOCATION = "../../keyStore/keystore.jks";
	
	private Socket socketToLoginSystem;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			socketToLoginSystem = new Socket(IP_DIRECTION, Server.PORT_RECEIVE);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
