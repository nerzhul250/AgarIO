package gameModel;

/**
 * Represents an object of type food
 * @author Steven
 *
 */
class Food extends GameObject{
	/**
	 * Is the weight of the food
	 */
	public final static double INITIALWEIGHT=1;
	/**
	 * Constructs a food objects
	 * @param x coordinate x of the food
	 * @param y coordinate y of the food
	 */
	public Food(int x, int y) {
		super(x, y);
		setWeight(INITIALWEIGHT);
	}
}