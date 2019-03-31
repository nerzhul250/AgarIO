package gameModel;

import java.util.HashMap;
import java.util.Iterator;

import registrationManagement.Server;

public class Game implements Runnable{
	public final static int XPadding=100;
	public final static int YPadding=100;
	
	public final static int Xlength=1000;
	public final static int Ylength=1000;
	public final static int MAXFOODNUMBER=100;
	
	private int gameObjectIdexes;
	
	private int numberOfPlayersAlive;
	private int amountOfFood;
	
	public HashMap<Coordinate,GameObject> gameObjects;
	public HashMap<Integer,Player> players;
	
	private String objectsState;
	
	public Game() {
		gameObjects=new HashMap<Coordinate,GameObject>(); 
		players=new HashMap<Integer,Player>();
		objectsState="0:0:1:0:";
	}

	private void RandomizedFoodSpawning() {
		while(MAXFOODNUMBER>amountOfFood) {
			int x=(int) (Math.random()*Xlength);
			int y=(int) (Math.random()*Ylength);
			Coordinate c=new Coordinate(x,y);
			while(gameObjects.containsKey(c)) {
				c.x=(int) (Math.random()*Xlength);
				c.y=(int) (Math.random()*Ylength);
			}
			Food f=new Food(c.x,c.y);
			f.setGlobalIndex(gameObjectIdexes++);
			f.setName(".");
			gameObjects.put(c,f);
			amountOfFood++;
		}
	}

	public void movePlayerToCoordinate(int id, int x, int y) {
		Player p=players.get(id);
		Coordinate newCoordinate=p.ComputeNewCoordinate(x,y);
		if(validCoordinate(newCoordinate)&&!gameObjects.containsKey(newCoordinate)){
			gameObjects.remove(p.getPosition());
			gameObjects.put(newCoordinate,p);
			p.setPosition(newCoordinate.x,newCoordinate.y);
		}
		for (int i = (int) -p.getRadius(); i < p.getRadius(); i++) {
			for (int j = (int) -p.getRadius(); j < p.getRadius(); j++) {
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

	public void addNewPlayer(int id,String name) {
		Coordinate c=getCoordinateForPlayer();
		Player p=new Player(c.x,c.y,id);
		p.setName(name);
		numberOfPlayersAlive++;
		p.setGlobalIndex(gameObjectIdexes++);
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
				//System.out.println(p.getPosition().x+" "+p.getPosition().y);
				if(p.isAlive()) {
					movePlayerToCoordinate(p.getId(),p.getDestination().x,p.getDestination().y);
					if(!p.foundPrey()) {p.setIdOfPrey(-1);}else {p.setFoundPrey(false);}
				}
			}
			StringBuilder sb=new StringBuilder();
			sb.append(gameObjects.size());
			sb.append(":");
			Coordinate[] coordinates=new Coordinate[1];
			coordinates=gameObjects.keySet().toArray(coordinates);
			for (int i = 0; i < coordinates.length; i++) {
				GameObject go=gameObjects.get(coordinates[i]);
				sb.append(go.getGlobalIndex());
				sb.append(":");
				sb.append(go.getPosition().x);
				sb.append(":");
				sb.append(go.getPosition().y);
				sb.append(":");
				sb.append(go.getRadius());
				sb.append(":");
				sb.append(go.getColor().getRGB());
				sb.append(":");
				sb.append(go.getName());
				sb.append(":");
			}
			objectsState=sb.toString();
			try {
				Thread.sleep(Server.GAMEPACE);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setPlayerMovingCoordinate(int id, int x, int y) {
		if(validCoordinate(new Coordinate(x,y))) {
			players.get(id).setMovingCoordinate(x,y);			
		}
	}

	public String getObjectsState() {
		return objectsState;		
	}

	public int getGreatestScorer() {
		Integer[] indexes=new Integer[1];
		indexes=players.keySet().toArray(indexes);
		int index=-1;
		int score=0;
		for (int i = 0; i < indexes.length; i++) {
			if(players.get(indexes[i])!=null &&players.get(indexes[i]).getWeight()>score && players.get(indexes[i]).isAlive() ) {
				score=(int) players.get(indexes[i]).getWeight();
				index=indexes[i];
			}
		}
		return index;
	}
}