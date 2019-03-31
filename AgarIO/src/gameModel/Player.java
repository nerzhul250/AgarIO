package gameModel;


public class Player extends GameObject implements Comparable<Player>{
	
	public final static double VELOCITYCONSTANT=10;
	public final static double INITIALWEIGHT=3;
	
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
		setWeight(INITIALWEIGHT);
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

	@Override
	public int compareTo(Player arg0) {
		Integer one=(int) getWeight();
		Integer two=(int)arg0.getWeight();
		return one.compareTo(two);
	}
}