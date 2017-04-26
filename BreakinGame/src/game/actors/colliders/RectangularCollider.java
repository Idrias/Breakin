package game.actors.colliders;

import other.G;
import other.Helper;
import processing.core.PVector;

public class RectangularCollider extends Collider {

	PVector size;
	
	/*
	 *  _______
	 * |   |   |
	 * |---x---|
	 * |___|___|
	 *   height 2
	 *   width 6
	 */
	
	public RectangularCollider(PVector center, float width, float height) {
		if(center==null) center = new PVector();
		this.center = center;
		this.size = new PVector(width, height);
	}

	public PVector get_size() { return size; }
	
	public void draw() {
		PVector drawCenter = Helper.GameToDrawPos(center);
		PVector drawSize = Helper.GameToDrawSize(size);
		G.p.rect(drawCenter.x, drawCenter.y, drawSize.x, drawSize.y);
	}
	
}
