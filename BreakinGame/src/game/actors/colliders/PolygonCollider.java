package game.actors.colliders;

import java.util.ArrayList;
import other.G;
import other.Helper;
import processing.core.PVector;



public class PolygonCollider extends Collider {

	ArrayList<PVector> points;



	public PolygonCollider(PVector center) {
		super();
		points = new ArrayList<PVector>();
		this.center = center;
	}



	ArrayList<PVector> get_points() {
		ArrayList<PVector> returnPoints = new ArrayList<PVector>();
		
		for(PVector point : points) {
			returnPoints.add(point.copy().add(center));
		}
		
		return returnPoints;
	}



	public void draw() {
		G.p.fill(0, 0, 255);
		G.p.beginShape();
		for (PVector p : get_points()) {
			PVector drawPos = Helper.GameToDrawPos(p);
			G.p.vertex(drawPos.x, drawPos.y);
		}
		G.p.endShape();
	}



	private PolygonCollider addPoint(float x, float y) {
		System.out.println("ADDING POINT AT " + x + " " + y);
		points.add(new PVector(x, y));
		return this;
	}



	public PolygonCollider addPointRelative(float dx, float dy) {
		return addPoint(dx, dy);
	}
}
