package game.actors.colliders;

import game.actors.GameObject;
import processing.core.PVector;

public class CollisionReport {
	
	GameObject other;
	PVector otherSurface;
	PVector attackVector;
	
	public CollisionReport(GameObject other, PVector otherSurface) {
		// TODO Auto-generated constructor stub
		this.other = other;
		this.attackVector = other.get_speed().copy();
		this.otherSurface = otherSurface;
	}

	public PVector generateResponseVector(GameObject me) {
		return attackVector;
	}

	public PVector get_otherSurface() { return otherSurface.copy(); }
}


/*
  GameObjects have mass 
  and faction
*/
 