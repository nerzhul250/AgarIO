package gameModel;

import java.awt.Color;

public class GameObject implements Comparable<GameObject>{
	
	public final static double RADIUSCONSTANT=5;
	
	private int globalIndex;
	private Coordinate position;
	private double weight;
	private Color color;
	private String name;

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGlobalIndex() {
		return globalIndex;
	}

	public void setGlobalIndex(int globalIndex) {
		this.globalIndex = globalIndex;
	}
	
	public GameObject(int x, int y) {
		position=new Coordinate(x,y);
		color = new Color((int) (Math.random()*8388608));
	
	}
	public Coordinate getPosition() {
		return position;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public double getRadius() {
		return Math.sqrt(getWeight()/Math.PI)*RADIUSCONSTANT;
	}
	
	public void setWeight(double w) {
		weight=w;
	}
	public void setPosition(int x,int y) {
		position.x=x;
		position.y=y;
	}

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