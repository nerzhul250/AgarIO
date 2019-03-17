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
		// TODO Auto-generated method stub
		
	}
}

class Coordinate implements Serializable{
   	private int x;
	private int y;
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
}

class Food extends GameObject{

	public Food(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}
	
}

class Player extends GameObject{
	private int id;
	private int velocity;
	private int weigth;
	public Player(int x, int y,int id) {
		super(x, y);
		this.id=id;
	}
	
}