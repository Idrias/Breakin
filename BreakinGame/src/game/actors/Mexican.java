package game.actors;

import java.util.ArrayList;
import game.actors.colliders.Collider;
import game.actors.colliders.CollisionReport;
import game.actors.colliders.RectangularCollider;
import network.utilities.NetworkEntity;
import other.G;
import other.Helper;
import processing.core.PVector;



public class Mexican extends GameObject {

	public Mexican(NetworkEntity ne) {
		super(ne);
		setDefaultValues();
	}



	public Mexican(int networkID) {
		super(Mexican.class, networkID);
		setDefaultValues();
	}



	@Override
	public GameObject setDefaultValues() {
		float defaultWidth = 0.7f * 9 / 16;
		float defaultHeight = 0.7f;
		float hitBoxWidthScale = 0.8f;
		float hitBoxHeightScale = 1.0f;
		//float hitBoxOffsetX = 500.0f;
		//float hitBoxOffsetY = 140.0f;

		set_size(defaultWidth, defaultHeight);

		PVector hitBoxPos = get_pos().copy();
		PVector hitBoxSize = get_size().copy();
		hitBoxSize.x *= hitBoxWidthScale;
		hitBoxSize.y *= hitBoxHeightScale;
		//hitBoxPos.x += hitBoxOffsetX;
		//hitBoxPos.y += hitBoxOffsetY;

		set_collider(new RectangularCollider(hitBoxPos, hitBoxSize.x, hitBoxSize.y));
		return this;
	}



	@Override
	public void update(ArrayList<GameObject> others) {

		int deltaT = G.p.millis() - lastUpdate;
		lastUpdate = G.p.millis();
		c.set_center(get_pos());

		ArrayList<CollisionReport> hits = c.get_hits();
		if (!hits.isEmpty()) {
			CollisionReport lastHit = hits.get(hits.size() - 1);
			PVector attackVector = lastHit.generateResponseVector(this);

			// TODO
			while (Collider.checkCollision(this, others))
				set_pos(get_pos().add(attackVector));

			set_pos(get_pos().add(attackVector.mult(10)));
			c.clearHits();
		}


		PVector posBefore = get_pos();
		PVector speed = get_speed().copy();
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


	}



	@Override
	public void draw() {
		// Draw mexican here
		PVector pos = Helper.GameToDrawPos(get_pos());
		PVector size = Helper.GameToDrawSize(get_size());
		c.draw();
		G.sprite.dispAnimation("Anim:Mexican", (int) pos.x, (int) pos.y, size.x, size.y, 130, 4);


	}



	public int getOwnerID() {
		return (int) ne.getValue("OwnerID");
	}



	public void setOwnerID(int ownerID) {
		ne.addKeyValuePair("OwnerID", (float) ownerID);
	}


}
