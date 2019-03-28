package gameModel;

import java.awt.Color;
import java.io.Serializable;

public class GameObject implements Serializable{
	
	public final static double RADIUSCONSTANT=1;
	
	private Coordinate position;
	private double weight;
	private Color color;

	
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
	
	public int getRadius() {
		return (int) (Math.sqrt(getWeight()/Math.PI)*RADIUSCONSTANT);
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
}