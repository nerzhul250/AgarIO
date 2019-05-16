package gameModel;

import java.util.Date;

/**
 * Represents a player
 * @author Steven
 *
 */
public class Player extends GameObject{
	/**
	 * Represents the velocity of the player
	 */
	public final static double VELOCITYCONSTANT=10;
	/**
	 * Represents the initial weigth
	 */
	public final static double INITIALWEIGHT=3;
	/**
	 * Attribute that tells if the player found a prey
	 */
	private boolean foundPrey;
	/**
	 * represents the state of the plyaer
	 */
	private boolean isAlive;
	/**
	 * id of the player
	 */
	private int id;
	/**
	 * id of the prey
	 */
	private int idOfPrey;
	/**
	 * the starting time to consumption
	 */
	private long consumptionStartTime; 
	/**
	 * the objective position
	 */
	private Coordinate destination;
	/**
	 * Constructs a player
	 * @param x
	 * @param y
	 * @param id
	 */
	private int gamesplayed;
	private int wons;
	private int perdidas;
	private int alimentosComidos;
	public Player(int x, int y,int id) {
		super(x, y);
		idOfPrey=-1;
		foundPrey=false;
		isAlive=true;
		destination=new Coordinate(getPosition().x,getPosition().y);
		this.id=id;
		alimentosComidos=0;
		setWeight(INITIALWEIGHT);
	}
	public void aumentarAlimentos() {
		alimentosComidos++;
	}
	
	public int getAlimentosComidos() {
		return alimentosComidos;
	}
	public void setAlimentosComidos(int alimentosComidos) {
		this.alimentosComidos = alimentosComidos;
	}
	/**
	 * sets if the player is alive
	 * @param b
	 */
	public void setAlive(boolean b) {
		isAlive=b;
	}
	/**
	 * grows the player
	 * @param weight2
	 */
	public void grow(double weight2) {
		setWeight(getWeight()+weight2);
	}
	/**
	 * returns the consumption start time
	 * @return
	 */
	public long getConsumptionStartTime() {
		return consumptionStartTime;
	}
	/**
	 * sets the consumption start time
	 * @param currentTimeMillis
	 */
	public void setConsumptionStartTime(long currentTimeMillis) {
		consumptionStartTime=currentTimeMillis;
	}
	/**
	 * founds the prey
	 * @param b
	 */
	public void setFoundPrey(boolean b) {
		foundPrey=b;
	}
	/*+
	 * returns the id of the prey
	 */
	public int getIdOfPrey() {
		return idOfPrey;
	}
	/**
	 * sets the id of the prey
	 * @param id2
	 */
	public void setIdOfPrey(int id2) {
		idOfPrey=id2;
	}
	/**
	 * the player found a prey?
	 * @return
	 */
	public boolean foundPrey() {
		return foundPrey;
	}
	/**
	 * returns if the player is alive
	 * @return
	 */
	public boolean isAlive() {
		return isAlive;
	}
	/**
	 * returns the destination position
	 * @return
	 */
	public Coordinate getDestination() {
		return destination;
	}

	/**
	 * returns the id of the player
	 * @return
	 */
	public int getId() {
		return id;
	}
	/**
	 * the destination coordinate
	 * @param x
	 * @param y
	 */
	public void setMovingCoordinate(int x, int y) {
		destination.x=x;
		destination.y=y;
	}
	/**
	 * Computes a new coordinate
	 * @param x
	 * @param y
	 * @return
	 */
	public Coordinate ComputeNewCoordinate(int x, int y){
		double deltax=x-getPosition().x;
		double deltay=y-getPosition().y;
		double norm=Math.sqrt(deltax*deltax+deltay*deltay);
		Coordinate c=null;
		if(norm<=getRadius()) {
			c=new Coordinate(getPosition().x,getPosition().y);
		}else {
			double odeltay=deltay*VELOCITYCONSTANT/(norm*Math.log(getWeight()));
			double odeltax =deltax*VELOCITYCONSTANT/(norm*Math.log(getWeight()));
			c=new Coordinate((int)(Math.round(odeltax)+deltax/norm)+getPosition().x,(int)(Math.round(odeltay)+deltay/norm)+getPosition().y);			
		}
		return c;
	}
	public void aumentarPartidas() {
		gamesplayed++;
	}
	
	public String retornarFecha() {
		Date objdate=new Date();
		String fecha=objdate.toString();
		return fecha;
	}
	public int getGamesplayed() {
		return gamesplayed;
	}
	public void setGamesplayed(int gamesplayed) {
		this.gamesplayed = gamesplayed;
	}
	public int getWons() {
		if(this.isAlive) {
			wons++;
		}
		return wons;
	}
	public void setWons(int wons) {
		this.wons = wons;
	}
	public int getPerdidas() {
		if(this.isAlive==false) {
			perdidas++;
		}
		return perdidas;
	}
	public void setPerdidas(int perdidas) {
		this.perdidas = perdidas;
	}
	

}