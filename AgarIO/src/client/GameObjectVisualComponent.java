package client;

import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
/**
 * represents a game object
 * @author Steven
 *
 */
public class GameObjectVisualComponent {
	/**
	 * the index of the object
	 */
	public int globalIndex;
	/**
	 * the shape of the object
	 */
	public Circle c;
	/**
	 * name of the object
	 */
	public Text name;
	/**
	 * construct a game object
	 * @param gi
	 * @param c
	 * @param n
	 */
	public GameObjectVisualComponent(int gi,Circle c,Text n) {
		globalIndex=gi;
		this.c=c;
		name=n;
	}
}
