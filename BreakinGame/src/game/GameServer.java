package game;

import java.util.ArrayList;
import game.actors.Dummy;
import game.actors.GameObject;
import network.NetServer;
import network.utilities.NetworkContainer;
import network.utilities.NetworkEntity;
import other.G;
import processing.core.PVector;
import processing.net.Client;



public class GameServer {

	NetServer netServer;
	ArrayList<GameObject> gameObjects;
	ArrayList<Player> players;
	int lastNetUpdate = 0;
	float netDeltaT;



	public GameServer() {
		netServer = new NetServer(G.p);
		gameObjects = new ArrayList<GameObject>();
		players = new ArrayList<Player>();
		netDeltaT = 1000 / G.NETWORK_UPDATERATE;
		//while (gameObjects.size() < 1000)
		//	add_go(G.ACTORTYPE_DUMMY, new PVector(G.p.random(0, G.p.width), G.p.random(0, G.p.height)), new PVector(G.p.random(-0.5f, 0.5f), G.p.random(-0.5f, 0.5f)));
	}



	public void update() {
		////////////////////////////////////////////////
		// Handle arriving and departing clients ///////
		for (Client c : G.newClients) {
			int clientID = netServer.generate_uniqueID();
			NetworkContainer infoContainer = new NetworkContainer();
			infoContainer.set_destination(c.ip(), clientID);
			netServer.pushNetworkContainer(infoContainer);
			netServer.addNewClient(c);
			players.add(new Player(c, clientID));
		}
		for (Client c : G.disconnectedClients) {
			netServer.removeDisconnectedClient(c);
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).get_client() == c) {
					players.remove(i);
					break;
				}
			}
		}
		G.newClients.clear();
		G.disconnectedClients.clear();
		////////////////////////////////////////////////
		////////////////////////////////////////////////
		// Receive messages from clients
		for (NetworkContainer nc : netServer.receive()) {
			G.println(nc.toString());
		}
		////////////////////////////////////////////////
		////////////////////////////////////////////////
		// Update the gameObjects
		for (GameObject go : gameObjects)
			go.update();
		
		if(gameObjects.size() > 0)
		gameObjects.remove((int)G.p.random(gameObjects.size())); // TESTING DEBUG REMOVE TODO 
		////////////////////////////////////////////////
		////////////////////////////////////////////////
		// Send update to clients
		if (G.p.millis() - lastNetUpdate >= netDeltaT) {
			netServer.pushEntities(getNetworkEntities());
			lastNetUpdate = G.p.millis();
		}
		////////////////////////////////////////////////
	}



	void add_go(int type, PVector pos, PVector speed) {
		switch (type) {
		case G.ACTORTYPE_DUMMY:
			Dummy d = new Dummy(netServer.generate_uniqueID());
			d.set_pos(pos);
			d.set_speed(speed);
			gameObjects.add(d);
			break;
		}
	}



	ArrayList<NetworkEntity> getNetworkEntities() {
		ArrayList<NetworkEntity> nes = new ArrayList<NetworkEntity>();
		for (GameObject go : gameObjects)
			nes.add(go.ne());
		return nes;
	}
}
