package game.levels;

import java.util.ArrayList;
import game.actors.GameObject;



public abstract class Level {

	ArrayList<GameObject> gameObjects;



	Level() {
		gameObjects = new ArrayList<GameObject>();
	}



	public ArrayList<GameObject> get_gameObjects() {
		return gameObjects;
	}
}
