package game.actors;

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
	public void update() {
		// TODO Auto-generated method stub
		super.simpleMove();
	}



	@Override
	public void draw() {
		// TODO Auto-generated method stub
		PVector pos = Helper.GameToDrawPos( get_pos() );
		G.p.ellipse(pos.x, pos.y, 10, 10);
	}

}
