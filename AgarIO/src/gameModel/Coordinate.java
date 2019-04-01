package gameModel;

/**
 * Class that represents a coordinate within the map
 * @author Steven
 *
 */
public class Coordinate{
	/**
	 * Coordinate of the x-axis
	 */
   	public int x;
   	/**
   	 * Coordinate of the y-axis
   	 */
	public int y;
	/**
	 * Constructor
	 * @param x coordinate of the x-axis
	 * @param y coordinate of the y-axis
	 */
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
		return (x==co.x && y==co.y);
	}
}