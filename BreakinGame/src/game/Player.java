package game;

import processing.net.Client;



public class Player {

	Client client;
	int playerID;
	String name = "Antonio-Juan don Pepe";



	public Player(Client client, int playerID) {
		this.client = client;
		this.playerID = playerID;
	}



	public Client get_client() {
		return client;
	}



	public int get_playerID() {
		return playerID;
	}



	public String get_name() {
		return name;
	}



	public void set_name(String name) {
		this.name = name;
	}
}
