package client;

import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class GameObjectVisualComponent {
	public int globalIndex;
	public Circle c;
	public Text name;
	public GameObjectVisualComponent(int gi,Circle c,Text n) {
		globalIndex=gi;
		this.c=c;
		name=n;
	}
}
