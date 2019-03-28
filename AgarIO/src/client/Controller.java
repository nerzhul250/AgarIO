package client;

import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javax.net.ssl.SSLSocketFactory;

import gameModel.Coordinate;
import gameModel.Game;
import gameModel.GameObject;
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
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import registrationManagement.ClientAttendant;
import registrationManagement.Server;

public class Controller implements Initializable{

	public static String IP_DIRECTION = "localhost";
	public static final String TRUSTTORE_LOCATION = "./keyStore/keystore.jks";
	
	private int id;
	
	private BufferedWriter transmitMovements;
	
	private ObjectInputStream receiveGame;
	
	private Socket socketToLoginSystem;
	
	private Socket socketGame;
	
	private Game gameState;
	
	@FXML
	private Pane gamePane;
	
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
				openPane();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				int portGameHoster=Integer.parseInt(br.readLine());
				String nickname=br.readLine();
				br.close();
				out.close();
				socketToLoginSystem.close();
				startGame(portGameHoster,nickname);
			} else if (ans.equals(ClientAttendant.ERROR)) {
				Alert al = new Alert(AlertType.WARNING);
				al.setTitle("Usuario no valido");
				al.setHeaderText("No se ha podido loguear en el sistema");
				al.setContentText(br.readLine());
				al.showAndWait();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			Alert al = new Alert(AlertType.ERROR);
			al.setTitle("Error en la conexión");
			al.setHeaderText("Problemas con el servidor");
			al.setContentText("No se ha podido establecer conexión con el servidor de logueo.");
			al.showAndWait();
		}
	}
	
	private void startGame(int portGameHoster, String nickname) {
		try {
			socketGame=new Socket(IP_DIRECTION,portGameHoster);
			System.out.println("here");
			receiveGame=new ObjectInputStream(socketGame.getInputStream());
			System.out.println("here2");
			transmitMovements=new BufferedWriter(new OutputStreamWriter(socketGame.getOutputStream()));
			System.out.println("got"); 
			transmitMovements.write(nickname+"\n");
			transmitMovements.flush();
			System.out.println("got2");
			new GUIUpdateControlThread(this).start();
			Thread.sleep(2000);
		} catch (InterruptedException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
			
			if (email.equals("") || nick.equals("") || pass1.equals("") ) {
				Alert al = new Alert(AlertType.WARNING);
				al.setTitle("Datos en blanco");
				al.setContentText("Por favor no deje datos en blanco");
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
	
	public void openPane() {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GamePanel.fxml"));
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
		System.out.println(gamePane==null);
	}
	
	@FXML 
	public void onMoved(MouseEvent e) {
		double w=e.getSceneX();
		double h=e.getSceneY();
		double W=gamePane.getWidth();
		double H=gamePane.getHeight();
		System.out.println("Y");
		try {
			System.out.println("E");
			transmitMovements.write(w+":"+h+":"+W+":"+H+"\n");
			System.out.println("S");
			transmitMovements.flush();
		} catch (IOException e1) {
			//TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void updateGUI() {
		gamePane.getChildren().clear();
		double W=gamePane.getWidth();
		double H=gamePane.getHeight();
		double x0=gameState.players.get(id).getPosition().x;
		double y0=gameState.players.get(id).getPosition().y;
		double r=gameState.players.get(id).getRadius();
		double X=2*r+Game.XPadding*2;
		double Y=2*r+Game.YPadding*2;
		for (int i = (int) (y0-(r+Game.YPadding)); i <= y0+r+Game.YPadding ; i++) {
			for (int j = (int) (x0-(r+Game.XPadding)); j < x0+r+Game.XPadding; j++) {
				if(gameState.gameObjects.containsKey(new Coordinate(j,i))) {
					GameObject go=gameState.gameObjects.get(new Coordinate(j,i));
					double w=(W/X)*j+(W/2)-(W/X)*x0;
					double h=(H/Y)*i+(H/2)-(H/Y)*y0;
					Circle c = new Circle((W/X)*go.getRadius(),new Color(go.getColor().getRed(),go.getColor().getGreen(),go.getColor().getBlue(),1));
			    	c.setLayoutX(w);
			    	c.setLayoutY(h);
			    	gamePane.getChildren().add(c);
				}
			}
		}
	}

	public Object getMessage() throws IOException, ClassNotFoundException {
		return receiveGame.readObject();
	}

	public void updateGame(Game info) {
		gameState=info;
	}

	public void setId(int id2) {
		id=id2;
	}

}
