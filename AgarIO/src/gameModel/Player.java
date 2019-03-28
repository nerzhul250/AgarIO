package gameModel;

import javafx.scene.paint.Color;

public class Player extends GameObject{
	
	public final static double MAGICALCONSTANT=0.5;
	
	private boolean foundPrey;
	private boolean isAlive;
	private int id;
	private int idOfPrey;
	private long consumptionStartTime; 
	private Coordinate destination;
	
	public Player(int x, int y,int id) {
		super(x, y);
		idOfPrey=-1;
		foundPrey=false;
		isAlive=true;
		destination=new Coordinate(0,0);
		this.id=id;
		setWeight(2);
	}

	public void setAlive(boolean b) {
		isAlive=b;
	}

	public void grow(double weight2) {
		setWeight(getWeight()+weight2);
	}

	public long getConsumptionStartTime() {
		return consumptionStartTime;
	}

	public void setConsumptionStartTime(long currentTimeMillis) {
		consumptionStartTime=currentTimeMillis;
	}

	public void setFoundPrey(boolean b) {
		foundPrey=b;
	}

	public int getIdOfPrey() {
		return idOfPrey;
	}

	public void setIdOfPrey(int id2) {
		idOfPrey=id2;
	}

	public boolean foundPrey() {
		return foundPrey;
	}

	public boolean isAlive() {
		return isAlive;
	}
	public Coordinate getDestination() {
		return destination;
	}

	public int getId() {
		return id;
	}

	public void setMovingCoordinate(int x, int y) {
		destination.x=x;
		destination.y=y;
	}

	public Coordinate ComputeNewCoordinate(int x, int y) {
		double deltax=x-getPosition().x;
		double deltay=y-getPosition().y;
		Coordinate c=new Coordinate((int)(deltax*MAGICALCONSTANT/getWeight()),(int)(deltay*MAGICALCONSTANT/getWeight()));
		return c;
	}
}