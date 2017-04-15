package game.actors.colliders;

import java.util.ArrayList;
import game.actors.GameObject;
import processing.core.PVector;



public class Collider {

	PVector center;
	ArrayList<CollisionReport> hits;



	public Collider() {
		center = new PVector();
		hits = new ArrayList<CollisionReport>();
	}



	public float distanceToCenter(PVector p) {
		PVector way = center.sub(p);
		return (float) Math.sqrt(Math.pow(way.x, 2) + Math.pow(way.y, 2));
	}



	public PVector get_center() {
		return center;
	}



	public Collider set_center(PVector center) {
		this.center = center;
		return this;
	}



	public void registerHit(GameObject other, float overlap) {
		hits.add( new CollisionReport(other, overlap) );
	}



	public ArrayList<CollisionReport> get_hits() {
		return hits;
	}



	public void clearHits() {
		hits.clear();
	}



	static public boolean checkCollision(GameObject me, ArrayList<GameObject> others) {
		Collider c1 = me.get_collider();
		if (c1 == null) return false;

		for (GameObject other : others) {
			if (me == other) continue;

			Collider c2 = other.get_collider();
			if (c2 == null) continue;

			if (c1.getClass() == RectangularCollider.class && c2.getClass() == RectangularCollider.class) {
				if (R_R_collision((RectangularCollider) c1, (RectangularCollider) c2)) {
					c1.registerHit(other, 1);
					c2.registerHit(me, 1);
					return true;
				}
			}
		}

		return false;
	}



	static public boolean R_R_collision(RectangularCollider a, RectangularCollider b) {
		PVector centerA = a.get_center();
		PVector centerB = b.get_center();
		PVector sizeA = a.get_size();
		PVector sizeB = b.get_size();

		boolean c1 = centerA.x + sizeA.x / 2 > centerB.x - sizeB.x / 2;
		boolean c2 = centerA.x - sizeA.x / 2 < centerB.x + sizeB.x / 2;
		boolean c3 = centerA.y + sizeA.y / 2 > centerB.y - sizeB.y / 2;
		boolean c4 = centerA.y - sizeA.y / 2 < centerB.y + sizeB.y / 2;

		if (c1 && c2 && c3 && c4) return true;
		return false;
	}


	// TODO
	// add circular collider
	// add polygon collider
	// add low accuracy pre-test
	// add high velocity / high accuracy test

	// y1 = ax + b
	// y2 = dx + e

	// 0 = ax + b -dx - e
	// e-b = ax-dx
	// e-b = a-d(x)
	// (e-b)/(a-d) = x
	//
	// y1 = 3x + 10
	// y2 = 5x + 4
	// x = -6 / -2 = 3
}
