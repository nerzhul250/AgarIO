package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javax.net.ssl.SSLSocketFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import registrationManagement.ClientAttendant;
import registrationManagement.Server;

public class RegisterAndLoginController implements Initializable{

	public static String IP_DIRECTION = "localhost";
	public static final String TRUSTTORE_LOCATION = "./keyStore/keystore.jks";
	
	private Socket socketToLoginSystem;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private PasswordField passPassword;
	
	@FXML
	private TextField txtNewEmail;
	
	@FXML
	private PasswordField passFirstPass;
	
	@FXML
	private PasswordField passSecondPass;
	
	@FXML
	private TextField txtNickName;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
	}
	
	@FXML
	public void login (ActionEvent e) {
		System.setProperty("javax.net.ssl.trustStore", TRUSTTORE_LOCATION);
		SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
		
		try {
			socketToLoginSystem = sf.createSocket(IP_DIRECTION, Server.PORT_RECEIVE);
			BufferedReader br = new BufferedReader(new InputStreamReader(socketToLoginSystem.getInputStream()));
			PrintWriter out = new PrintWriter(socketToLoginSystem.getOutputStream(), true);
			String email = txtEmail.getText();
			String pass = passPassword.getText();
			out.println(ClientAttendant.LOGIN);
			out.println(email);
			out.println(pass);
			
			String ans = br.readLine();
			if (ans.equals(ClientAttendant.ACCEPTED)) {
				
			} else if (ans.equals(ClientAttendant.ERROR)) {
				Alert al = new Alert(AlertType.WARNING);
				al.setTitle("Usuario no valido");
				al.setHeaderText("No se ha podido loguear en el sistema");
				al.setContentText(br.readLine());
				al.showAndWait();
			}
		} catch (IOException e1) {
			Alert al = new Alert(AlertType.ERROR);
			al.setTitle("Error en la conexión");
			al.setHeaderText("Problemas con el servidor");
			al.setContentText("No se ha podido establecer conexión con el servidor de logueo.");
			al.showAndWait();
		}
	}
	
	
	@FXML
	public void register (ActionEvent e) {
		System.setProperty("javax.net.ssl.trustStore", TRUSTTORE_LOCATION);
		SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
		try {
			String email = txtNewEmail.getText();
			String nick = txtNickName.getText();
			String pass1 = passFirstPass.getText();
			String pass2 = passSecondPass.getText();
			if (!pass1.equals(pass2)) {
				Alert al = new Alert(AlertType.WARNING);
				al.setTitle("Contraseña erronea");
				al.setContentText("Las contraseñas no coinciden");
				al.showAndWait();
				return;
			}
			
			socketToLoginSystem = sf.createSocket(IP_DIRECTION, Server.PORT_RECEIVE);
			BufferedReader br = new BufferedReader(new InputStreamReader(socketToLoginSystem.getInputStream()));
			PrintWriter out = new PrintWriter(socketToLoginSystem.getOutputStream(), true);
			out.println(ClientAttendant.REGISTER);
			out.println(email);
			out.println(nick);
			out.println(pass1);
			
			String ans = br.readLine();
			if (ans.equals(ClientAttendant.ACCEPTED)) {
				Alert al = new Alert(AlertType.CONFIRMATION);
				al.setTitle("Usuario registrado exitosamente");
				al.setContentText("Se ha creado el usuario con las credenciales indicadas.");
				al.showAndWait();
			} else if (ans.equals(ClientAttendant.ERROR)) {
				Alert al = new Alert(AlertType.WARNING);
				al.setTitle("Error al registrar usuario");
				al.setHeaderText("No se puede crear el usuario");
				al.setContentText(br.readLine());
				al.showAndWait();
			}
		} catch (IOException e1) {
			Alert al = new Alert(AlertType.ERROR);
			al.setTitle("Error en la conexión");
			al.setHeaderText("Problemas con el servidor");
			al.setContentText("No se ha podido establecer conexión con el servidor de logueo.");
			al.showAndWait();
		}
	}
	
	
	@FXML
	public void changeFrame(ActionEvent e) {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/view/Register.fxml"));
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.show();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
