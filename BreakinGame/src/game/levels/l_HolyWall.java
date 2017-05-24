package game.levels;

import game.actors.EndIndicator;
import game.actors.bricks.IronBrick;
import game.actors.bricks.SimpleBrick;
import game.actors.bricks.TNTBrick;
import other.G;



public class l_HolyWall extends Level {

	public l_HolyWall() {
		
		height = 3;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < G.playarea_width; x++) {
				// TODO etwas unsauber hier auf G.gameServer zurückzugreifen!
				
				float i = G.p.random(0, 2);
				
				if(i < 1.6){
					if(i < 0.05){
						TNTBrick tnt = new TNTBrick(G.gameServer.generate_uniqueID());
						tnt.set_pos(x + 0.5f, y);
						gameObjects.add(tnt);
					}
					else if(i < 0.3){
						IronBrick iron = new IronBrick(G.gameServer.generate_uniqueID());
						iron.set_pos(x + 0.5f, y);
						gameObjects.add(iron);
					} else {
						SimpleBrick brick = new SimpleBrick(G.gameServer.generate_uniqueID());
						brick.set_pos(x + 0.5f, y);
						gameObjects.add(brick);
					}
				}
			}
		}

		EndIndicator e = new EndIndicator(G.gameServer.generate_uniqueID());
		e.set_pos(0, 0);
		gameObjects.add(e);

	}

}
