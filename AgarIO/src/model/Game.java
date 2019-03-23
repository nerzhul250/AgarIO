package model;

import java.io.Serializable;
import java.util.HashMap;

public class Game implements Serializable{
	
	public final static int Xlength=500;
	public final static int Ylength=500;
	
	HashMap<Coordinate,GameObject> gameObjects;
	HashMap<Integer,Player> players;
	
	public Game() {
		gameObjects=new HashMap<Coordinate,GameObject>(); 
		players=new HashMap<Integer,Player>();
		RandomizedFoodSpawning();
	}

	private void RandomizedFoodSpawning() {
		
	}

	public void movePlayerToCoordinate(int id, int x, int y) {
		Player p=players.get(id);
		Coordinate newCoordinate=p.ComputeNewCoordinate(x,y);
		if(!gameObjects.containsKey(newCoordinate)){
			gameObjects.remove(p.getPosition());
			gameObjects.put(newCoordinate,p);
			p.setPosition(newCoordinate.x,newCoordinate.y);
		}
	}

	public void addNewPlayer(int id) {
		Coordinate c=getCoordinateForPlayer();
		Player p=new Player(c.x,c.y,id);
		players.put(id, p);
		gameObjects.put(p.getPosition(),p);
	}

	private Coordinate getCoordinateForPlayer() {
		// TODO Auto-generated method stub
		return null;
	}
}

class Coordinate implements Serializable{
   	public int x;
	public int y;
	public Coordinate(int x, int y) {
		this.x=x;
		this.y=y;
	}
	@Override
	public int hashCode() {
		return x+y;
	}
	
	@Override
	public boolean equals(Object obj) {
		Coordinate co=(Coordinate)obj;
		return x==co.x && y==co.y;
	}
}

class GameObject implements Serializable{
	private Coordinate position;
	
	public GameObject(int x, int y) {
		position=new Coordinate(x,y);
	}
	
	public Coordinate getPosition() {
		return position;
	}
	
	public void setPosition(int x,int y) {
		position.x=x;
		position.y=y;
	}
}

class Food extends GameObject{

	public Food(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}
	
}

class Player extends GameObject{
	private int id;
	private int weight;
	private int color;
	
	public Player(int x, int y,int id) {
		super(x, y);
		this.id=id;
		weight=1;
		color=(int) (Math.random()*100);
	}

	public Coordinate ComputeNewCoordinate(int x, int y) {
		// TODO Auto-generated method stub
		return null;
	}
	
}