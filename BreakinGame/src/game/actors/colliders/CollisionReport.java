package game.actors.colliders;

import game.actors.GameObject;
import processing.core.PVector;

public class CollisionReport {
	
	GameObject other;
	PVector attackVector;
	
	public CollisionReport(GameObject other) {
		// TODO Auto-generated constructor stub
		this.other = other;
		this.attackVector = other.get_speed().copy();
	}

	public PVector generateResponseVector(GameObject me) {
		return attackVector;
	}
}


/*
  GameObjects have mass 
  and faction
*/
 