package game.actors;

import network.utilities.NetworkEntity;
import other.G;
import other.Helper;
import processing.core.PVector;



public class SimpleBrick extends GameObject {

	public SimpleBrick(int networkID) {
		// Server calls this
		super(SimpleBrick.class, networkID);
	}



	public SimpleBrick(NetworkEntity ne) {
		// Client calls this
		super(ne);
	}



	public void draw() {
		PVector pos = Helper.GameToDrawPos(ne.get_pos());
		G.sprite.dispSprite("Static:SimpleBrick", (int) pos.x, (int) pos.y);
	}



	public void update() {
		simpleMove();
	}

}
