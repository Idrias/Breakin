package game.actors.bricks;

import network.utilities.NetworkEntity;
import other.G;
import other.Helper;
import processing.core.PVector;



public class IronBrick extends SimpleBrick {

	public IronBrick(int networkID) {
		// Server calls this
		super(IronBrick.class, networkID);
	}



	public IronBrick(NetworkEntity ne) {
		// Client calls this
		super(ne);
	}


	
	@Override
	public IronBrick setDefaultValues() {
		float defaultWidth = 1;
		float defaultHeight = 1;
		set_size(defaultWidth, defaultHeight);
		//c = new RectangularCollider(get_pos(), defaultWidth, defaultHeight);
		return this;
	}



	public void draw() {
		PVector pos = Helper.GameToDrawPos(get_pos());
		PVector size = Helper.GameToDrawSize(get_size());
		G.sprite.dispSprite("Static:IronBrick", (int) pos.x, (int) pos.y, (int) size.x, (int) size.y);
		G.p.fill(255, 0, 0);
	}
}
