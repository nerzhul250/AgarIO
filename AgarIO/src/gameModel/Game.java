package gameModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javafx.scene.paint.Color;

public class Game implements Serializable, Runnable{
	
	public final static int REFRESHDELAY=100;
	
	public final static int XPadding=100;
	public final static int YPadding=100;
	
	public final static int Xlength=15000;
	public final static int Ylength=15000;
	public final static int MAXFOODNUMBER=500;
	
	private int numberOfPlayersAlive;
	private int amountOfFood;
	
	public HashMap<Coordinate,GameObject> gameObjects;
	public HashMap<Integer,Player> players;
	
	
	public Game() {
		gameObjects=new HashMap<Coordinate,GameObject>(); 
		players=new HashMap<Integer,Player>();
	}

	private void RandomizedFoodSpawning() {
		if(MAXFOODNUMBER>amountOfFood) {
			int x=(int) (Math.random()*Xlength);
			int y=(int) (Math.random()*Ylength);
			Coordinate c=new Coordinate(x,y);
			while(gameObjects.containsKey(c)) {
				c.x=(int) (Math.random()*Xlength);
				c.y=(int) (Math.random()*Ylength);
			}
			gameObjects.put(c,new Food(c.x,c.y));
			amountOfFood++;	
		}
	}

	public void movePlayerToCoordinate(int id, int x, int y) {
		Player p=players.get(id);
		Coordinate newCoordinate=p.ComputeNewCoordinate(x,y);
		if(!gameObjects.containsKey(newCoordinate)){
			gameObjects.remove(p.getPosition());
			gameObjects.put(newCoordinate,p);
			p.setPosition(newCoordinate.x,newCoordinate.y);
		}
		for (int i = -p.getRadius(); i < p.getRadius(); i++) {
			for (int j = -p.getRadius(); j < p.getRadius(); j++) {
				Coordinate c=new Coordinate(p.getPosition().x+j,p.getPosition().y+i);
				if(validCoordinate(c)) {
					if(gameObjects.containsKey(c)) {
						if(gameObjects.get(c) instanceof Player) {
							Player p2 =(Player) gameObjects.get(c);
							if(p.getWeight()>p2.getWeight()) {
								p.setFoundPrey(true);
								if(p.getIdOfPrey()==-1 || p.getIdOfPrey()!=p2.getId()) {
									p.setIdOfPrey(p2.getId());
									p.setConsumptionStartTime(System.currentTimeMillis());
								}else if(p.getIdOfPrey()==p2.getId() && (System.currentTimeMillis()-p.getConsumptionStartTime())>=2000) {
									p.grow(p2.getWeight());
									p2.setAlive(false);
									numberOfPlayersAlive--;
									gameObjects.remove(p2.getPosition());
								}
							}
						}else if(gameObjects.get(c) instanceof Food) {
							Food f=(Food) gameObjects.get(c);
							p.grow(f.getWeight());
							gameObjects.remove(c);
							amountOfFood--;
						}
					}
				}
			}
		}
	}

	private boolean validCoordinate(Coordinate c) {
		return c.x>=0 && c.y>=0 && c.x<=Xlength && c.y<=Ylength;
	}

	public void addNewPlayer(int id) {
		Coordinate c=getCoordinateForPlayer();
		Player p=new Player(c.x,c.y,id);
		numberOfPlayersAlive++;
		players.put(id, p);
		gameObjects.put(p.getPosition(),p);
	}

	private Coordinate getCoordinateForPlayer() {
		int x=(int) (Math.random()*Xlength);
		int y=(int) (Math.random()*Ylength);
		Coordinate c=new Coordinate(x,y);
		while(gameObjects.containsKey(c)) {
			c.x=(int) (Math.random()*Xlength);
			c.y=(int) (Math.random()*Ylength);
		}
		return c;
	}

	@Override
	public void run() {
		while(numberOfPlayersAlive>1) {
			RandomizedFoodSpawning();
			Iterator<Integer> it=players.keySet().iterator();
			while(it.hasNext()) {
				Player p=players.get(it.next());
				if(p.isAlive()) {
					movePlayerToCoordinate(p.getId(),p.getDestination().x,p.getDestination().y);
					if(!p.foundPrey()) {p.setIdOfPrey(-1);}else {p.setFoundPrey(false);}
				}
			}
			try {
				Thread.sleep(REFRESHDELAY);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setPlayerMovingCoordinate(int id, int x, int y) {
		players.get(id).setMovingCoordinate(x,y);
	}
}