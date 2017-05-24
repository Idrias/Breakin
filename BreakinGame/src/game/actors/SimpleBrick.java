package game.actors;

import java.util.ArrayList;
import game.actors.colliders.PolygonCollider;
import network.utilities.NetworkEntity;
import other.G;
import other.Helper;
import processing.core.PVector;



public class SimpleBrick extends GameObject {

	public SimpleBrick(int networkID) {
		// Server calls this
		super(SimpleBrick.class, networkID);
	}



	public SimpleBrick(NetworkEntity ne) {
		// Client calls this
		super(ne);
	}



	@Override
	public SimpleBrick setDefaultValues() {
		float defaultWidth = 1;
		float defaultHeight = 1;
		set_size(defaultWidth, defaultHeight);
		
		System.out.println(get_pos());
		set_collider(
				new PolygonCollider(get_pos().copy())
				.addPointRelative(-0.5f, 0.5f)
				.addPointRelative(0.5f, 0.5f)
				.addPointRelative(0.5f, -0.5f)
				.addPointRelative(-0.5f, -0.5f)
		);
		
		return this;
	}



	public void draw() {
		PVector pos = Helper.GameToDrawPos(get_pos());
		PVector size = Helper.GameToDrawSize(get_size());
		c.draw();
		G.sprite.dispSprite("Static:SimpleBrick", (int) pos.x, (int) pos.y, (int) size.x, (int) size.y);
		G.p.fill(255, 0, 0);
	}



	public void update(ArrayList<GameObject> others) {
		simpleMove(others);
	}

}
