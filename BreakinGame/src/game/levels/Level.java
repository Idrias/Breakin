package game.levels;

import java.util.ArrayList;
import game.actors.GameObject;
import other.G;



public abstract class Level {

	int height = 0;
	ArrayList<GameObject> gameObjects;



	Level() {
		gameObjects = new ArrayList<GameObject>();
		if (height == 0) height = G.playarea_height;
	}



	public ArrayList<GameObject> get_gameObjects() {
		return gameObjects;
	}



	public int get_height() {
		return height;
	}
}
