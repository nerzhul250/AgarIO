package gameModel;


public class Coordinate{
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
		return (x==co.x && y==co.y);
	}
}