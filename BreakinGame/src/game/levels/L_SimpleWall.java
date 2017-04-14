package game.levels;

import game.actors.EndIndicator;
import game.actors.SimpleBrick;
import other.G;



public class L_SimpleWall extends Level {

	public L_SimpleWall() {
		for (int y = 0; y < G.playarea_height; y++) {
			for (int x = 0; x < G.playarea_width; x++) {
				// TODO etwas unsauber hier auf G.gameServer zurückzugreifen!
				SimpleBrick brick = new SimpleBrick(G.gameServer.generate_uniqueID());
				brick.set_pos(x + 0.5f, y);
				gameObjects.add(brick);
			}
		}

		EndIndicator e = new EndIndicator(G.gameServer.generate_uniqueID());
		e.set_pos(0, 0);
		gameObjects.add(e);

	}

}
