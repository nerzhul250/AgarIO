package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.net.ssl.SSLSocketFactory;

import gameModel.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import registrationManagement.ClientAttendant;
import registrationManagement.Server;

public class Controller implements Initializable{

	public static final String IP_DIRECTION = "localhost";
	public static final String TRUSTTORE_LOCATION = "./keyStore/keystore.jks";
	public static final String LOGINPANELOCATION="/view/Login.fxml";
	public static final String REGISTERPANELOCATION="/view/Register.fxml";
	public static final String GAMEPANELOCATION="/view/GamePanel.fxml";
	
	
	private BufferedWriter transmitMovements;
	
	private BufferedReader receiveGame;
	
	private Socket socketToLoginSystem;
	
	private Socket socketGame;
	
	private HashMap<Integer,GameObjectVisualComponent> gameObjects;
	
	private ArrayList<Text> podium;
	
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
			receiveGame=new BufferedReader(new InputStreamReader(socketGame.getInputStream()));
			transmitMovements=new BufferedWriter(new OutputStreamWriter(socketGame.getOutputStream()));
			transmitMovements.write(nickname+"\n");
			transmitMovements.flush();
			System.out.println("GameStarting");
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
			root = FXMLLoader.load(getClass().getResource(REGISTERPANELOCATION));
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
			FXMLLoader loader = new FXMLLoader(getClass().getResource(GAMEPANELOCATION));
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
		gamePane.setBackground(new Background(new BackgroundFill(new Color(195/256.0,222/256.0,250/256.0,1),CornerRadii.EMPTY, Insets.EMPTY)));
		gameObjects=new HashMap<Integer,GameObjectVisualComponent>();
		podium=new ArrayList<Text>();
		podium.add(new Text(gamePane.getWidth()-100,15,"TOP"));
		gamePane.getChildren().add(podium.get(0));
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

	public void updateGUI(String infos) {
		String[] splitted=infos.substring(0,infos.length()-1).split(":");
		double W=gamePane.getWidth();
		double H=gamePane.getHeight();
		double x0=Double.parseDouble(splitted[0]);
		double y0=Double.parseDouble(splitted[1]);
		double r=Double.parseDouble(splitted[2]);
		double X=2*r+Game.XPadding*2;
		double Y=2*r+Game.YPadding*2;
		int n=Integer.parseInt(splitted[3]);
		HashSet<Integer> globalIndexes=new HashSet<Integer>();
		for (int i = 0; i <n; i++) {
			int index=i*6+4;
			int globIndex=Integer.parseInt(splitted[index]);
			globalIndexes.add(globIndex);
			int x=Integer.parseInt(splitted[index+1]);
			int y=Integer.parseInt(splitted[index+2]);
			int color=Integer.parseInt(splitted[index+4]);
			double radius=Double.parseDouble(splitted[index+3]);
			double w=(W/X)*x+(W/2)-(W/X)*x0;
			double h=(H/Y)*y+(H/2)-(H/Y)*y0;
			String name=splitted[index+5];
			if(gameObjects.containsKey(globIndex)) {
				gamePane.getChildren().remove(gameObjects.get(globIndex).c);
				gamePane.getChildren().remove(gameObjects.get(globIndex).name);
//				gameObjects.get(globIndex).c.setLayoutX(w);
//				gameObjects.get(globIndex).c.setLayoutY(h);
//				gameObjects.get(globIndex).c.setRadius((W/X)*radius);
//				gameObjects.get(globIndex).name.setLayoutX(w-10-gameObjects.get(globIndex).name.getLayoutBounds().getMinX());
//				gameObjects.get(globIndex).name.setLayoutY(h-10-gameObjects.get(globIndex).name.getLayoutBounds().getMinY());
			}
//			else {
				int basic=(1<<8)-1;
				Circle c = new Circle((W/X)*radius,new Color((color&basic)/256.0,((color&(basic<<8))>>8)/256.0,((color&(basic<<16))>>16)/256.0,1));
				c.setLayoutX(w);
				c.setLayoutY(h);
				Text t=new Text(w,h,name);
				GameObjectVisualComponent g=new GameObjectVisualComponent(globIndex, c,t);
				gameObjects.put(globIndex,g);
				gamePane.getChildren().add(c);
				gamePane.getChildren().add(t);
//			}
		}
		int podiumSize=Integer.parseInt(splitted[(n-1)*6+10]);
		podium.get(0).setLayoutX(gamePane.getWidth()-100-podium.get(0).getLayoutBounds().getMinX());
		podium.get(0).setLayoutY(15-podium.get(0).getLayoutBounds().getMinY());
		for (int i = 0; i < podiumSize; i++) {
			if(podium.size()<2+i) {
				podium.add(new Text(gamePane.getWidth()-100,30+i*15,splitted[(n-1)*6+11+i]));
				gamePane.getChildren().add(podium.get(i+1));
			}else {
				podium.get(i+1).setText(splitted[(n-1)*6+11+i]);
				podium.get(i+1).setLayoutX(gamePane.getWidth()-100-podium.get(i+1).getLayoutBounds().getMinX());
				podium.get(i+1).setLayoutY(30+i*15-podium.get(i+1).getLayoutBounds().getMinY());
			}
		}
		Iterator<Integer> it2=gameObjects.keySet().iterator();
		ArrayList<Integer> toRemove=new ArrayList<Integer>();
		while(it2.hasNext()) {
			Integer tor=it2.next();
			if(!globalIndexes.contains(tor))toRemove.add(tor);
		}
		for (int i = 0; i < toRemove.size(); i++) {
			gamePane.getChildren().remove(gameObjects.get(toRemove.get(i)).c);
			gamePane.getChildren().remove(gameObjects.get(toRemove.get(i)).name);
			gameObjects.remove(toRemove.get(i));
		}
	}

	public String getMessage() throws IOException {
		return receiveGame.readLine();
	}

	public void showWinMessage(String info) {
		Alert al = new Alert(AlertType.WARNING);
		al.setTitle("Se termino el juego");
		al.setHeaderText("Ganaste");
		al.setContentText(info);
		al.showAndWait();
	}

	public void showLoseMessage(String info) {
		Alert al = new Alert(AlertType.WARNING);
		al.setTitle("Se termino el juego");
		al.setHeaderText("Perdiste");
		al.setContentText(info);
		al.showAndWait();
	}

	public void showDisconnectMessage(String info) {
		Alert al = new Alert(AlertType.WARNING);
		al.setTitle("Conexion rechazada");
		al.setHeaderText("Tu conexion fue rechazada");
		al.setContentText(info);
		al.showAndWait();
	}

}
