package game.actors.colliders;

import game.actors.GameObject;
import other.G;
import processing.core.PVector;

public class CollisionReport {
	
	GameObject other;
	PVector attackVector;
	float overlap;
	
	public CollisionReport(GameObject other, float overlap) {
		// TODO Auto-generated constructor stub
		this.other = other;
		this.attackVector = other.get_speed();
		this.overlap = overlap;
	}

	public PVector generateResponseVector(GameObject me) {
		// F = m * a
		// TODO
		//return attackVector.mult(-1).normalize().mult(overlap);
		return attackVector;
		
	}
}


/*
  GameObjects have mass 
  and faction
*/
 