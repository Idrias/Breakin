package managers;

import java.util.ArrayList;
import game.levels.L_SimpleMaze;
import game.levels.L_SimpleWall;
import game.levels.Level;



/*
 * World: continous stream of levels 
 * Level: Formation of GameObjects that need to be destroyed / passed to reach next level
 * 
 *(Tunnel: Connection from exit of one level to entrance of new level)
 * 
 * WorldGenerator: Creates the world by instancing prefab levels
 */

public class WorldManager {

	// The WorldManager class only cares about future blocks. The ones currently
	// visible on screen are solely handled by GameServer!
	ArrayList<Level> futureWorld;



	public WorldManager() {
		futureWorld = new ArrayList<Level>();
	}



	public Level nextLevel() {
		while (futureWorld.isEmpty()) {
			generateLevel();
		}

		Level nextLevel = futureWorld.get(0);
		futureWorld.remove(0);

		return nextLevel;
	}



	void generateLevel() {
		Level generatedLevel = null;

		generatedLevel = new L_SimpleWall(); //TODO / DEV
		generatedLevel = new L_SimpleMaze();
		
		if (generatedLevel != null) {
			futureWorld.add(generatedLevel);
		}
	}


}
