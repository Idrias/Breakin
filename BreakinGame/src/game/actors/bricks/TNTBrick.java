package game.actors.bricks;

import java.util.ArrayList;
import game.actors.GameObject;
import network.utilities.NetworkEntity;
import other.G;
import other.Helper;
import processing.core.PVector;



public class TNTBrick extends GameObject {

	public TNTBrick(int networkID) {
		// Server calls this
		super(TNTBrick.class, networkID);
	}



	public TNTBrick(NetworkEntity ne) {
		// Client calls this
		super(ne);
	}


	
	@Override
	public TNTBrick setDefaultValues() {
		float defaultWidth = 1;
		float defaultHeight = 1;
		set_size(defaultWidth, defaultHeight);
		return this;
	}



	public void draw() {
		PVector pos = Helper.GameToDrawPos(get_pos());
		PVector size = Helper.GameToDrawSize(get_size());
		G.sprite.dispSprite("Static:TNTBrick", (int) pos.x, (int) pos.y, (int) size.x, (int) size.y);
		//Rakete anzeigen
		//G.sprite.dispAnimation("Anim:Rocket", (int) pos.x, (int) pos.y, size.x, size.y*2, 50, 4);
		G.p.fill(255, 0, 0);
	}



	public void update(ArrayList<GameObject> others) {
		simpleMove(others);
	}

}