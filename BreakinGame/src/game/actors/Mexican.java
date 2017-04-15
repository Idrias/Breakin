package game.actors;

import java.util.ArrayList;
import game.actors.colliders.Collider;
import game.actors.colliders.RectangularCollider;
import network.utilities.NetworkEntity;
import other.G;
import other.Helper;
import processing.core.PVector;



public class Mexican extends GameObject {

	public Mexican(NetworkEntity ne) {
		super(ne);
	}



	public Mexican(int networkID) {
		super(Mexican.class, networkID);
	}


	@Override
	public GameObject setDefaultValues() {
		set_size(0.9f*9/16, 0.9f);
		set_collider( new RectangularCollider(get_pos(), get_size().x*0.8f, get_size().y) );
		return this;
	}
	

	@Override
	public void update(ArrayList<GameObject> others) {

		int deltaT = G.p.millis() - lastUpdate;
		lastUpdate = G.p.millis();

		PVector posBefore = get_pos();
		PVector speed = get_speed().copy();
		// G.println(speed);
		speed = speed.normalize();
		speed = speed.mult(G.playerspeed);

		PVector posAfter = new PVector();
		posAfter.x = posBefore.x + speed.x * deltaT;
		posAfter.y = posBefore.y + speed.y * deltaT;

		set_pos(posAfter);
		c.set_center(posAfter);

		// COLLISION TEST
		boolean collided = Collider.checkCollision(this, others);

		if (collided) {
			set_pos(posBefore);
			c.set_center(posBefore);
		}


		// G.println(speed);

		// G.println("----------");

	}



	@Override
	public void draw() {
		// Draw mexican here
		PVector pos = Helper.GameToDrawPos(get_pos());
		PVector size = Helper.GameToDrawSize(get_size());

		G.sprite.dispAnimation("Anim:Mexican", (int) pos.x, (int) pos.y, size.x, size.y, 130, 4);

	}



	public int getOwnerID() {
		return (int) ne.getValue("OwnerID");
	}



	public void setOwnerID(int ownerID) {
		ne.addKeyValuePair("OwnerID", (float) ownerID);
	}


}
