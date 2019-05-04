package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;

import gameModel.Game;
import gameServer.GameHoster;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StreamingController implements Initializable {
	
	public static final String OBSERVERPANELOCATION = "/view/ObserverPane.fxml";
	
	
	/**
	 * buffer to transmit
	 */
	private BufferedWriter transmit;
	/**
	 * buffer to receive
	 */
	private BufferedReader receive;
	/**
	 * socket to conect
	 */
	private Socket socket;
	/**
	 * datagramSocket
	 */
	private DatagramSocket streamingEnd;
	/**
	 * position of the player that are watching
	 */
	private int posx,posy;
	/**
	 * score of the players
	 */
	private ArrayList<Text> podium;
	/**
	 * objects in the game
	 */
	private HashMap<Integer,GameObjectVisualComponent> gameObjects;
	
	@FXML
	private Pane observerPane;
	/**
	 * Area when he can watch all the messages
	 */
	@FXML
	private TextArea txtAreaAllChat;
	/**
	 * Watch the user who sends the message
	 */
	@FXML
	private TextField txtUserMessage;
	/**
	 * Socket for the chat
	 */
	private Socket chatSocket;
	/**
	 * DataInputStream to read the message
	 */
	private DataInputStream inputChat;
	/**
	 * DataOutputStream to send the message
	 */
	private DataOutputStream outputChat;
	/**
	 * Chat controller
	 */
	private ChatController chat;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		posx=(int) (Game.Xlength*Math.random());
		posy=(int) (Game.Ylength*Math.random());
	}
	/**
	 * method that open a pane for the observer
	 */
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
		observerPane.setBackground(new Background(new BackgroundFill(new Color(195/256.0,222/256.0,250/256.0,1),CornerRadii.EMPTY, Insets.EMPTY)));
		gameObjects=new HashMap<Integer,GameObjectVisualComponent>();
		podium=new ArrayList<Text>();
		podium.add(new Text(observerPane.getWidth()-100,15,"TOP"));
		observerPane.getChildren().add(podium.get(0));
	}
	/**
	 * method that starts to streaming
	 */
	public void startStreaming(int portGameHoster, String nickname) {
		try {
			socket=new Socket(Controller.IP_DIRECTION,portGameHoster);
			receive=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			transmit=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			transmit.write(GameHoster.OBSERVER+"\n");
			transmit.write(nickname+"\n");
			transmit.flush();
			System.out.println("StreamingStarting");
			streamingEnd=new DatagramSocket(socket.getLocalPort());
			startChat(nickname);
			(new GUIStreamingUpdateControlThread(this)).start();
			Thread.sleep(2000);
		} catch (InterruptedException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	/**
	 * method that get the message of the observers
	 */
	public String getMessage() {
		byte[] recibirDatos =  new byte[20000];
		DatagramPacket recibirPaquete = new DatagramPacket(recibirDatos, recibirDatos.length);
		try {        
			 streamingEnd.receive(recibirPaquete);
		 } catch (IOException e) {
			 System.out.println("Error al recibir");
			 System.exit ( 0 );
		 }	
		 String frase = new String(recibirPaquete.getData());
		 return frase;
	}
	/**
	 * Method that permit the observer follow the person who is streaming
	 */
	@FXML 
	public void onClicked(MouseEvent e) {
		double w=e.getSceneX();
		double h=e.getSceneY();
		double W=observerPane.getWidth();
		double H=observerPane.getHeight();
		double x0=posx;
		double y0=posy;
		double X=Game.XPadding*5;
		double Y=Game.YPadding*5;
		posx=(int) ((X/W)*(w+(W*x0/X)-(W/2)));
		posy=(int) ((Y/H)*(h+(H*y0/Y)-(H/2)));
	}
	/**
	 * method that update the GUI
	 */
	public void updateGUI(String infos) {
		String[] splitted=infos.substring(0,infos.lastIndexOf(':')).split(":");
		double W=observerPane.getWidth();
		double H=observerPane.getHeight();
		double x0=posx;
		double y0=posy;
		double X=Game.XPadding*4;
		double Y=Game.YPadding*4;
		int n=Integer.parseInt(splitted[0]);
		HashSet<Integer> globalIndexes=new HashSet<Integer>();
		for (int i = 0; i <n; i++) {
			int index=i*6+1;
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
				observerPane.getChildren().remove(gameObjects.get(globIndex).c);
				observerPane.getChildren().remove(gameObjects.get(globIndex).name);
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
				observerPane.getChildren().add(c);
				observerPane.getChildren().add(t);
//			}
		}
		//Updates the podium
		int podiumSize=Integer.parseInt(splitted[(n-1)*6+7]);
		podium.get(0).setLayoutX(observerPane.getWidth()-100-podium.get(0).getLayoutBounds().getMinX());
		podium.get(0).setLayoutY(15-podium.get(0).getLayoutBounds().getMinY());
		for (int i = 0; i < podiumSize; i++) {
			if(podium.size()<2+i) {
				podium.add(new Text(observerPane.getWidth()-100,30+i*15,splitted[(n-1)*6+8+i]));
				observerPane.getChildren().add(podium.get(i+1));
			}else {
				podium.get(i+1).setText(splitted[(n-1)*6+8+i]);
				podium.get(i+1).setLayoutX(observerPane.getWidth()-100-podium.get(i+1).getLayoutBounds().getMinX());
				podium.get(i+1).setLayoutY(30+i*15-podium.get(i+1).getLayoutBounds().getMinY());
			}
		}
		//Removes gameObjects no longer in the game
		Iterator<Integer> it2=gameObjects.keySet().iterator();
		ArrayList<Integer> toRemove=new ArrayList<Integer>();
		while(it2.hasNext()) {
			Integer tor=it2.next();
			if(!globalIndexes.contains(tor))toRemove.add(tor);
		}
		for (int i = 0; i < toRemove.size(); i++) {
			observerPane.getChildren().remove(gameObjects.get(toRemove.get(i)).c);
			observerPane.getChildren().remove(gameObjects.get(toRemove.get(i)).name);
			gameObjects.remove(toRemove.get(i));
		}
	}
	/**
	 * Method that starts the chat
	 */
	public void startChat(String userNick) {
		chat = new ChatController(userNick, txtAreaAllChat);
		chat.startReceivingMessages();
	}
	/**
	 * Method that send the message
	 */
	public void sendMessage(ActionEvent e) {
		String toSend = txtUserMessage.getText();
		chat.sendMessage(toSend);
	}

}
