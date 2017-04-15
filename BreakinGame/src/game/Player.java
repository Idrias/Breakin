package game;

import game.actors.GameObject;
import processing.core.PVector;
import processing.net.Client;



public class Player {

	Client client;
	GameObject gameObject;
	int playerID;
	String name = "Antonio-Juan don Pepe";
	PVector movementVector;



	/////////////////////////////////////////////////////////////////////////////////////////////////////

	public Player(Client client, int playerID) {
		this.client = client;
		this.playerID = playerID;
		gameObject = null;
		movementVector = new PVector(0, 0);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////



	public Client get_client() {
		return client;
	}



	public int get_playerID() {
		return playerID;
	}



	public String get_name() {
		return name;
	}



	public GameObject get_gameObject() {
		return gameObject;
	}



	public PVector get_movementVector() {
		return movementVector;
	}



	public void set_name(String name) {
		this.name = name;
	}



	public void set_gameObject(GameObject gameObject) {
		this.gameObject = gameObject;
	}



	public void set_movementVector(float vX, float vY) {
		movementVector = new PVector(vX, vY);
		if(gameObject != null) gameObject.set_speed(movementVector);
	}
}
