package game.actors;

import network.utilities.NetworkEntity;
import other.G;
import processing.core.PVector;



public class Dummy extends GameObject {

	public Dummy(int networkID) {
		super(G.ACTORTYPE_DUMMY, networkID);
	}



	public Dummy(NetworkEntity ne) {
		super(ne);
	}



	public void update() {
		int deltaT = G.p.millis() - lastUpdate;
		simpleMove(deltaT);
		lastUpdate = G.p.millis();
	}



	public void draw() {
		G.p.fill(0, 0, 255);
		PVector pos = ne.get_pos();
		G.p.ellipse(pos.x, pos.y, 20, 20);
	}
}
