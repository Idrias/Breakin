package game.actors.bricks;

import network.utilities.NetworkEntity;
import other.G;
import other.Helper;
import processing.core.PVector;



public class TNTBrick extends SimpleBrick {

	public TNTBrick(int networkID) {
		// Server calls this
		super(TNTBrick.class, networkID);
	}



	public TNTBrick(NetworkEntity ne) {
		// Client calls this
		super(ne);
	}



	public void draw() {
		PVector pos = Helper.GameToDrawPos(get_pos());
		PVector size = Helper.GameToDrawSize(get_size());
		G.sprite.dispSprite("Static:TNTBrick", (int) pos.x, (int) pos.y, (int) size.x, (int) size.y);
		G.p.fill(255, 0, 0);
	}

}
