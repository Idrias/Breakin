package managers;

import java.util.ArrayList;
import game.levels.L_SimpleMaze;
import game.levels.L_SimpleWall;
import game.levels.L_Test;
import game.levels.Level;
import other.G;



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

		int levelCount = 3;
		int levelType = (int) Math.floor(G.p.random(levelCount));

		switch (levelType) {
		case 0:
			generatedLevel = new L_SimpleWall();
			break;// TODO / DEV
		case 1:
			generatedLevel = new L_SimpleMaze();
			break;
		case 2:
			generatedLevel = new L_Test(L_Test.m1);
			break;
		}
		
		
		if (generatedLevel != null) {
			futureWorld.add(generatedLevel);
		}
	}


}
