package game.actors.colliders;

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
}
