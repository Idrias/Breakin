package game;

import java.util.ArrayList;
import game.actors.Dummy;
import game.actors.GameObject;
import network.NetServer;
import network.utilities.NetworkCommand;
import network.utilities.NetworkContainer;
import network.utilities.NetworkEntity;
import other.G;
import other.Helper;
import processing.core.PVector;
import processing.net.Client;



public class GameServer {

	NetServer netServer;
	ArrayList<GameObject> gameObjects;
	ArrayList<Player> players;
	ArrayList<NetworkCommand> pendingCommands;

	final int PHASE_INACTIVE = 1, PHASE_LOBBY = 3, PHASE_INGAME = 4;

	int phase = PHASE_INACTIVE;
	int lastNetUpdate = 0;
	float netDeltaT;



	public GameServer() {
		//TODO FREE PORT ON SERVER STOP
		//netServer = new NetServer(G.p);
		gameObjects = new ArrayList<GameObject>();
		players = new ArrayList<Player>();
		pendingCommands = new ArrayList<NetworkCommand>();
		netDeltaT = 1000 / G.NETWORK_UPDATERATE;
	}



	public void update() {
		switch (phase) {

		case PHASE_INACTIVE:
			return;

		case PHASE_LOBBY:
			break;

		case PHASE_INGAME:
			break;
		}

		updateACTIVE();
	}



	private void updateACTIVE() {
		// TODO private void vs void?

		////////////////////////////////////////////////
		// Handle arriving and departing clients ///////
		for (Client c : G.newClients) {
			int clientID = netServer.generate_uniqueID();
			NetworkContainer infoContainer = new NetworkContainer();
			infoContainer.set_destination(c.ip(), clientID);
			netServer.pushNetworkContainer(infoContainer);
			netServer.addNewClient(c);
			players.add(new Player(c, clientID));
			G.println("added new client!");
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
		for (NetworkContainer container : netServer.receive()) {
			int senderID = container.get_senderID();
			// Check commands
			for(NetworkCommand nc : container.get_commands()) {
				int commandType = nc.get_commandType();
				ArrayList<String> stringParams = nc.get_stringParams();
				ArrayList<Float> floatParams = nc.get_floatParams();
				
				switch(commandType) {
				case NetworkCommand.MYNAMEIS:
					int index = Helper.getPlayerIndexByID(players, senderID);
					if(index != -1 && stringParams.size() == 1) players.get(index).set_name(stringParams.get(0));
					break;
				}
			}
		}
		////////////////////////////////////////////////


		////////////////////////////////////////////////
		// Update the gameObjects
		for (GameObject go : gameObjects)
			go.update();
		////////////////////////////////////////////////


		////////////////////////////////////////////////
		// Send update to clients
		if (G.p.millis() - lastNetUpdate >= netDeltaT) {
			formCommands();
			netServer.pushInfo(getNetworkEntities(), getNetworkCommands());
			// TODO direct access to netCommands
			lastNetUpdate = G.p.millis();
			pendingCommands.clear();
		}
		////////////////////////////////////////////////

	}



	void formCommands() {
		NetworkCommand nc = new NetworkCommand(NetworkCommand.PLAYERINFO, getPlayerNames(), null);
		pendingCommands.add(nc);
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
			nes.add(go.get_ne());
		return nes;
	}



	ArrayList<NetworkCommand> getNetworkCommands() {
		return pendingCommands;
	}



	ArrayList<String> getPlayerNames() {
		ArrayList<String> pnames = new ArrayList<String>();
		for (Player p : players)
			pnames.add(p.name);
		return pnames;
	}



	public void activate(int port) {
		netServer = new NetServer(port, G.p);
		gameObjects.clear();
		players.clear();
		pendingCommands.clear();
		phase = PHASE_LOBBY;
	}



	public void deactivate() {
		netServer.stop();
		phase = PHASE_INACTIVE;
	}
}
