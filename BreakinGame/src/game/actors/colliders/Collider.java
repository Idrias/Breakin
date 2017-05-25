package game.actors.colliders;

import java.util.ArrayList;
import game.actors.GameObject;
import other.G;
import processing.core.PVector;



public abstract class Collider {

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



	public void registerHit(GameObject other, PVector otherSurface) {
		hits.add(new CollisionReport(other, otherSurface));
	}



	public ArrayList<CollisionReport> get_hits() {
		return hits;
	}



	public void clearHits() {
		hits.clear();
	}



	public void draw() {
		// DEV TODO ONLY FOR DEBUG
	}



	static public boolean checkCollision(GameObject me, ArrayList<GameObject> others) {
		Collider c1 = me.get_collider();
		if (c1 == null) return false;

		for (GameObject other : others) {
			if (me == other) continue;

			Collider c2 = other.get_collider();
			if (c2 == null) continue;

			if (c1.getClass() == PolygonCollider.class && c2.getClass() == PolygonCollider.class) {
				if (poly_poly_collision((PolygonCollider) c1, (PolygonCollider) c2)) {
					PVector hitdir = poly_poly_hitdir((PolygonCollider) c1, (PolygonCollider) c2);
					c1.registerHit(other, hitdir);
					c2.registerHit(me, hitdir);
					return true;
				}
			}
		}

		return false;
	}



	static boolean poly_poly_collision(PolygonCollider c1, PolygonCollider c2) {
		
		ArrayList<PVector> b = c1.get_points();
		ArrayList<PVector> a = c2.get_points();
		
		// go through each of the vertices, plus the next vertex in the list
		int next = 0;
		for (int current = 0; current < a.size(); current++) {

			// get next vertex in list
			// if we've hit the end, wrap around to 0
			next = current + 1;
			if (next == a.size()) next = 0;

			// get the PVectors at our current position
			// this makes our if statement a little cleaner
			PVector vc = a.get(current); // c for "current"
			PVector vn = a.get(next); // n for "next"

			// now we can use these two points (a line) to compare to the
			// other polygon's vertices using polyLine()
			//System.out.println(b);
			boolean collision = poly_line_collision(b, vc.x, vc.y, vn.x, vn.y);
			if (collision) return true;

			// optional: check if the 2nd polygon is INSIDE the first
			//collision = poly_point_collision(a, b.get(0).x, b.get(0).y);
			//if (collision) return true;
		}

		return false;
	}



	static boolean poly_line_collision(ArrayList<PVector> vertices, float x1, float y1, float x2, float y2) {

		// go through each of the vertices, plus the next vertex in the list
		int next = 0;
		for (int current = 0; current < vertices.size(); current++) {

			// get next vertex in list
			// if we've hit the end, wrap around to 0
			next = current + 1;
			if (next == vertices.size()) next = 0;

			// get the PVectors at our current position
			// extract X/Y coordinates from each
			float x3 = vertices.get(current).x;
			float y3 = vertices.get(current).y;
			float x4 = vertices.get(next).x;
			float y4 = vertices.get(next).y;

			// do a Line/Line comparison
			// if true, return 'true' immediately and stop testing (faster)
			boolean hit = line_line_collision(x1, y1, x2, y2, x3, y3, x4, y4);
			if (hit) {
				return true;
			}
		}

		return false;
	}



	static boolean line_line_collision(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		// TODO DEV
		// calculate the direction of the lines
		float uA = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));
		float uB = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));

		// if uA and uB are between 0-1, lines are colliding
		if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {
			return true;
		}
		return false;
	}



	static boolean poly_point_collision(ArrayList<PVector> vertices, float px, float py) {
		boolean collision = false;
		// TODO
		// go through each of the vertices, plus the next vertex in the list
		int next = 0;
		for (int current = 0; current < vertices.size(); current++) {

			// get next vertex in list
			// if we've hit the end, wrap around to 0
			next = current + 1;
			if (next == vertices.size()) next = 0;

			// get the PVectors at our current position
			// this makes our if statement a little cleaner
			PVector vc = vertices.get(current); // c for "current"
			PVector vn = vertices.get(next); // n for "next"

			// compare position, flip 'collision' variable back and forth
			if (((vc.y > py && vn.y < py) || (vc.y < py && vn.y > py)) && (px < (vn.x - vc.x) * (py - vc.y) / (vn.y - vc.y) + vc.x))

			{
				collision = !collision;
			}
		}
		return collision;
	}

	
	
	static PVector poly_poly_hitdir(PolygonCollider c1, PolygonCollider c2) {
		
		ArrayList<PVector> b = c1.get_points();
		ArrayList<PVector> a = c2.get_points();
		
		// go through each of the vertices, plus the next vertex in the list
		int next = 0;
		for (int current = 0; current < a.size(); current++) {

			// get next vertex in list
			// if we've hit the end, wrap around to 0
			next = current + 1;
			if (next == a.size()) next = 0;

			// get the PVectors at our current position
			// this makes our if statement a little cleaner
			PVector vc = a.get(current); // c for "current"
			PVector vn = a.get(next); // n for "next"

			// now we can use these two points (a line) to compare to the
			// other polygon's vertices using polyLine()
			//System.out.println(b);
			boolean collision = poly_line_collision(b, vc.x, vc.y, vn.x, vn.y);
			if (collision) {
				PVector dir = new PVector(vn.x - vc.x, vn.y - vc.y).normalize();
				return dir;
			}

			// optional: check if the 2nd polygon is INSIDE the first
			//collision = poly_point_collision(a, b.get(0).x, b.get(0).y);
			//if (collision) return new PVector(0, 0);
		}

		return null;
	}
}
