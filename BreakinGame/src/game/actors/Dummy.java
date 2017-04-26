package game.actors;

import java.util.ArrayList;
import network.utilities.NetworkEntity;
import other.G;
import other.Helper;
import processing.core.PVector;



public class Dummy extends GameObject {

	public Dummy(int networkID) {
		super(Dummy.class, networkID);
	}



	public Dummy(NetworkEntity ne) {
		super(ne);
	}



	public void update(ArrayList<GameObject> others) {
		simpleMove(others);
	}



	public void draw() {
		G.p.fill(0, 0, 255);
		PVector pos = Helper.GameToDrawPos(ne.get_pos());
		G.p.ellipse(pos.x, pos.y, 20, 20);
	}
}
