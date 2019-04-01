package gameModel;

import java.awt.Color;
/**
 * Represents one object of the game
 * @author Steven
 *
 */
public class GameObject implements Comparable<GameObject>{
	/**
	 * Is a constant value to normalize the raidius
	 */
	public final static double RADIUSCONSTANT=5;
	/**
	 * Is the global index
	 */
	private int globalIndex;
	/**
	 * Position of the object within the map
	 */
	private Coordinate position;
	/**
	 * The weight of the object
	 */
	private double weight;
	/**
	 * color of the object
	 */
	private Color color;
	/**
	 * Name of the object
	 */
	private String name;

	/**
	 * returns the name of the object
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**
	 * sets the name of the object
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * returns the global index
	 * @return
	 */
	public int getGlobalIndex() {
		return globalIndex;
	}
	/**
	 * sets the global index
	 * @param globalIndex
	 */
	public void setGlobalIndex(int globalIndex) {
		this.globalIndex = globalIndex;
	}
	/**
	 * Constructs an object
	 * @param x position of the object in the x-axis
	 * @param y position of the object in the y-axis
	 */
	public GameObject(int x, int y) {
		position=new Coordinate(x,y);
		color = new Color((int) (Math.random()*8388608));
	
	}
	/**
	 * Returns the position of the object
	 * @return
	 */
	public Coordinate getPosition() {
		return position;
	}
	/**
	 * returns the weight of the object
	 * @return
	 */
	public double getWeight() {
		return weight;
	}
	/**
	 * returns the radius of the object
	 * @return
	 */
	public double getRadius() {
		return Math.sqrt(getWeight()/Math.PI)*RADIUSCONSTANT;
	}
	/**
	 * sets the weight
	 * @param w
	 */
	public void setWeight(double w) {
		weight=w;
	}
	/**
	 * sets the position
	 * @param x
	 * @param y
	 */
	public void setPosition(int x,int y) {
		position.x=x;
		position.y=y;
	}
	/**
	 * returns the color
	 * @return
	 */
	public Color getColor() {
		return color;
	}

	@Override
	public int compareTo(GameObject o) {
		Integer one=(int) getWeight();
		Integer two=(int)o.getWeight();
		return one.compareTo(two);
	}
}