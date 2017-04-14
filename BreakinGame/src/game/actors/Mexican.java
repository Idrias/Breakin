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
		
		int deltaT = G.p.millis() - lastUpdate;
		lastUpdate = G.p.millis();
		
		PVector posBefore = get_pos();
		PVector speed = get_speed().copy();
		//G.println(speed);
		speed = speed.normalize();
		speed = speed.mult(G.playerspeed);
		speed.y += G.gravity;
		
		PVector posAfter = new PVector();
		posAfter.x = posBefore.x += speed.x*deltaT;
		posAfter.y = posBefore.y += speed.y*deltaT;
		
		set_pos(posAfter);
		//G.println(speed);
		
		//G.println("----------");
	}



	@Override
	public void draw() {
		// Draw mexican here
		PVector pos = Helper.GameToDrawPos(get_pos());
		G.sprite.dispAnimation("Anim:Mexican", (int)pos.x, (int)pos.y, 3, 4);
	}

}
