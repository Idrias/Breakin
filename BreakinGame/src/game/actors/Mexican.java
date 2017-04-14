package game.actors;

import network.utilities.NetworkEntity;
import other.G;
import other.Helper;
import processing.core.PApplet;
import processing.core.PVector;


public class Mexican extends GameObject {
	
	public Mexican(NetworkEntity ne) {
		super(ne);
	}



	public Mexican(int networkID) {
		super(Mexican.class, networkID);
	}



	@Override
	public void update() {
		PVector posBefore = get_pos();
		PVector speed = get_speed().copy();
		//G.println(speed);
		speed.x = PApplet.constrain(speed.x, -1, 1);
		speed.y = PApplet.constrain(speed.y, -1, 1);
		speed = speed.mult(G.playerspeed);
		
		PVector posAfter = posBefore.add(speed);
		set_pos(posAfter);
		//G.println(speed);
		
		//G.println("----------");
	}



	@Override
	public void draw() {
		// Draw mexican here
		PVector pos = Helper.GameToDrawPos(get_pos());
		G.sprite.dispAnimation("Anim:Mexican", (int)pos.x, (int)pos.y, 3, 2);
	}

}
