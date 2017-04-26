package game.actors;

import java.util.ArrayList;
import network.utilities.NetworkEntity;
import other.G;
import other.Helper;
import processing.core.PVector;



public class EndIndicator extends GameObject {

	public EndIndicator(NetworkEntity ne) {
		super(ne);
		// TODO Auto-generated constructor stub
	}



	public EndIndicator(int networkID) {
		super(EndIndicator.class, networkID);
		// TODO Auto-generated constructor stub
	}



	@Override
	public void update(ArrayList<GameObject> others) {
		// TODO Auto-generated method stub
		super.simpleMove(others);
	}



	@Override
	public void draw() {
		// Client currently doesn't add EndIndicator to gameObjects -> no draw!
		PVector pos = Helper.GameToDrawPos(get_pos());
		G.p.fill(255, 0, 0);
		G.p.ellipse(pos.x, pos.y, 10, 10);
	}

}
