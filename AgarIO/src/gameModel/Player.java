package gameModel;

import javafx.scene.paint.Color;

public class Player extends GameObject{
	
	public final static double MAGICALCONSTANT=4;
	
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
		destination=new Coordinate(getPosition().x,getPosition().y);
		this.id=id;
		setWeight(3);
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

	public Coordinate ComputeNewCoordinate(int x, int y){
		double deltax=x-getPosition().x;
		double deltay=y-getPosition().y;
		double norm=Math.sqrt(deltax*deltax+deltay*deltay);
		deltax =deltax*MAGICALCONSTANT/(norm*getWeight());
		deltay=deltay*MAGICALCONSTANT/(norm*getWeight());
		int addx=0,addy=0;
		if(deltax<0) {addx=-1;}else {addx=1;}
		if(deltay<0) {addy=-1;}else {addy=1;}
		Coordinate c=new Coordinate((int)(Math.round(deltax)+addx)+getPosition().x,(int)(Math.round(deltay)+addy)+getPosition().y);
		return c;
	}
}