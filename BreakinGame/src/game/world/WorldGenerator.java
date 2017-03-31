package game.world;

import java.util.ArrayList;
import game.world.levels.Level;

/*
 * World: continous stream of levels 
 * Level: Formation of GameObjects that need to be destroyed / passes to reach next level
 * 
 *(Tunnel: Connection from exit of one level to entrance of new level)
 * 
 * WorldGenerator: Creates the world by instancing prefab levels
 */

public class WorldGenerator {
	ArrayList<Level> world;
}
