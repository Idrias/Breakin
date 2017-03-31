package game.world.levels;

import java.util.ArrayList;
import game.actors.GameObject;

public abstract class Level {
	
	final static float EVERYWHERE = 42;
	
	boolean inflated = false;
	float entryX;
	float exitX;
	float height;
	ArrayList<GameObject> gameObjects;
	
	Level() {
		
	}
	
	// Return true if we could match our entry/exit points with the parameters
	abstract boolean inflate(int tryEntryX, int tryExitX);
}
