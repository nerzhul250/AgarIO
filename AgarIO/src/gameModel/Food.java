package gameModel;


class Food extends GameObject{
	
	public final static double INITIALWEIGHT=1;
	
	public Food(int x, int y) {
		super(x, y);
		setWeight(INITIALWEIGHT);
	}
}