package game.actors;

import java.util.ArrayList;
import game.actors.colliders.Collider;
import game.actors.colliders.CollisionReport;
import game.actors.colliders.PolygonCollider;
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
		float hitBoxWidthScale = 0.65f;
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

		set_collider(
				new PolygonCollider(get_pos().copy())
				.addPointRelative(0, -0.3f)  //A
				.addPointRelative(-0.138f, -0.217f) // C
				.addPointRelative(-0.139f, 0.183f) //D!
				.addPointRelative(0, 0.383f) // F
				.addPointRelative(0.139f, 0.183f) //E!
				.addPointRelative(0.138f, -0.217f) //B
		);

		System.out.println("POS: " + get_pos());
		System.out.println("CENTER: " + c.get_center());
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
			c.clearHits();
		}

	}



	@Override
	public void draw() {
		// Draw mexican here
		
		PVector pos = Helper.GameToDrawPos(get_pos());
		PVector size = Helper.GameToDrawSize(get_size());
		c.draw();
		G.sprite.dispAnimation("Anim:Mexican", (int) pos.x, (int) pos.y, size.x, size.y, 130, 4);
                
		
		/* TODO DEV Debug Tool for finding hitbox points!
		PVector mouse = Helper.DrawToGamePos(new PVector(G.p.mouseX, G.p.mouseY));
		if(get_pos().y > 0)
		G.p.println("Mouse is ingame at: " + mouse, "Relative to Block: " + mouse.sub(get_pos()));
		G.p.fill(255,0,0);
		G.p.ellipse(G.p.mouseX, G.p.mouseY, 5, 5);
		*/

	}



	public int getOwnerID() {
		return (int) ne.getValue("OwnerID");
	}



	public void setOwnerID(int ownerID) {
		ne.addKeyValuePair("OwnerID", (float) ownerID);
	}


}
