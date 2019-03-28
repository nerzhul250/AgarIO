package gameModel;

import java.io.Serializable;

class Food extends GameObject{
	public Food(int x, int y) {
		super(x, y);
		setWeight(1);
	}
}