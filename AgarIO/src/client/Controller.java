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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.net.ssl.SSLSocketFactory;

import gameModel.Coordinate;
import gameModel.Game;
import gameModel.GameObject;
import gameModel.Player;
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
	
	private HashMap<Integer,Circle> circles;
	
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
		circles=new HashMap<Integer,Circle>();
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
		try {
			transmitMovements.write(w+":"+h+":"+W+":"+H+"\n");
			transmitMovements.flush();
		} catch (IOException e1) {
			//TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void updateGUI(Object infos) {
		Object[] info=(Object[]) infos;
		HashMap<Integer,Player> players=(HashMap<Integer, Player>) info[0];
		HashMap<Integer,GameObject> gameObjects=(HashMap<Integer, GameObject>) info[1];
		double W=gamePane.getWidth();
		double H=gamePane.getHeight();
		double x0=players.get(id).getPosition().x;
		double y0=players.get(id).getPosition().y;
		double r=players.get(id).getRadius();
		double X=2*r+Game.XPadding*2;
		double Y=2*r+Game.YPadding*2;
		Iterator<Integer> it=gameObjects.keySet().iterator();
		while(it.hasNext()) {
			GameObject go=gameObjects.get(it.next());
			double w=(W/X)*go.getPosition().x+(W/2)-(W/X)*x0;
			double h=(H/Y)*go.getPosition().y+(H/2)-(H/Y)*y0;
			if(circles.containsKey(go.getGlobalIndex())) {
				circles.get(go.getGlobalIndex()).setLayoutX(w);
				circles.get(go.getGlobalIndex()).setLayoutY(h);
			}else {
				Circle c = new Circle((W/X)*go.getRadius(),new Color(go.getColor().getRed()/256.0,go.getColor().getGreen()/256.0,go.getColor().getBlue()/256.0,1));
				c.setLayoutX(w);
				c.setLayoutY(h);
				c.setRadius((W/X)*go.getRadius());
				System.out.println("Circle Added!");
				circles.put(go.getGlobalIndex(),c);
				gamePane.getChildren().add(c);
			}
		}
		Iterator<Integer> it2=circles.keySet().iterator();
		ArrayList<Integer> toRemove=new ArrayList<Integer>();
		while(it2.hasNext()) {
			Integer tor=it2.next();
			if(!gameObjects.containsKey(tor))toRemove.add(tor);
		}
		
		for (int i = 0; i < toRemove.size(); i++) {
			gamePane.getChildren().remove(circles.get(toRemove.get(i)));
			circles.remove(toRemove.get(i));
		}
	}

	public Object getMessage() throws ClassNotFoundException, IOException{
		try {
			return receiveGame.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				receiveGame.readInt();
				return receiveGame.readObject();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return receiveGame.readObject();
	}

	public void setId(int id2) {
		id=id2;
	}
}
