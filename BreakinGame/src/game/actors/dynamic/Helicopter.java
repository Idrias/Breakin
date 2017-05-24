package game.actors.dynamic;

import java.util.ArrayList;
import game.actors.GameObject;
import network.utilities.NetworkEntity;
import other.G;
import other.Helper;
import processing.core.PVector;



public class Helicopter extends GameObject {

	public Helicopter(int networkID) {
		super(Helicopter.class, networkID);
		setDefaultValues();
	}



	public Helicopter(NetworkEntity ne) {
		super(ne);
		setDefaultValues();
	}
	
	
	
	public void update(ArrayList<GameObject> others) {
		simpleMove(others);
	}
	
	
	@Override
	public GameObject setDefaultValues() {
		
		float defaultWidth = 2f * 9 / 16;
		float defaultHeight = 2f;
		set_size(defaultWidth, defaultHeight);
		
		return this;
	}
	
	
	public void draw() {
		PVector pos = Helper.GameToDrawPos(get_pos());
		PVector size = Helper.GameToDrawSize(get_size());
		G.sprite.dispAnimation("Anim:Helicopter", (int) pos.x, (int) pos.y, size.x, size.y, 130, 4);
	}
}