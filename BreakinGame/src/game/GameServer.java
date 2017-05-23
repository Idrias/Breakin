package game;

import java.util.ArrayList;
import game.actors.EndIndicator;
import game.actors.GameObject;
import game.actors.Mexican;
import game.actors.Sombrero;
import game.actors.colliders.RectangularCollider;
import game.levels.Level;
import managers.WorldManager;
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
	WorldManager world;

	ArrayList<GameObject> gameObjects;
	ArrayList<Player> players;
	ArrayList<NetworkCommand> pendingCommands;

	public final static int PHASE_INACTIVE = 1, PHASE_LOBBY = 3, PHASE_INGAME = 4;
	int phase = PHASE_INACTIVE;
	int lastNetUpdate = 0;
	float netDeltaT;



	/////////////////////////////////////////////////////////////////////////////////////////////////////


	public GameServer() {
		// TODO FREE PORT ON SERVER STOP
		// netServer = new NetServer(G.p);
		world = new WorldManager();
		gameObjects = new ArrayList<GameObject>();
		players = new ArrayList<Player>();
		pendingCommands = new ArrayList<NetworkCommand>();
		netDeltaT = 1000 / G.NETWORK_UPDATERATE;
		
		
		
	}



	/////////////////////////////////////////////////////////////////////////////////////////////////////

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



	/////////////////////////////////////////////////////////////////////////////////////////////////////

	private void updateACTIVE() {
		// TODO private void vs void?
		handle_connections();
		handle_rcvMessages();
		handle_gameObjects();
		handle_sendUpdates();

	}



	private void handle_connections() {
		////////////////////////////////////////////////
		// Handle arriving and departing clients ///////
		for (Client c : G.newClients) {
			int clientID = netServer.generate_uniqueID();
			NetworkContainer infoContainer = new NetworkContainer();
			infoContainer.set_destination(c.ip(), clientID);
			netServer.pushNetworkContainer(infoContainer);
			netServer.addNewClient(c);
			
			Mexican mexican = new Mexican(generate_uniqueID());
			mexican.set_pos(clientID%G.playarea_width, G.playarea_height - 3);
			mexican.setDefaultValues();
			mexican.setOwnerID(clientID);
			// TODO add collider to networkentity
			
			Player p = new Player(c, clientID);
			p.set_gameObject(mexican);
			players.add(p);
			
			if(gameObjects != null)
				gameObjects.add( mexican );
			
			
			ArrayList<Float> floatArgs = new ArrayList<Float>();
			floatArgs.add((float)phase);
			pendingCommands.add( new NetworkCommand(NetworkCommand.SERVER_STATECHANGE, null, floatArgs) );
			
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

	}



	private void handle_rcvMessages() {
		////////////////////////////////////////////////
		// Receive messages from clients
		for (NetworkContainer container : netServer.receive()) {
			int senderID = container.get_senderID();
			// Check commands
			for (NetworkCommand nc : container.get_commands()) {
				int commandType = nc.get_commandType();
				ArrayList<String> stringParams = nc.get_stringParams();
				ArrayList<Float> floatParams = nc.get_floatParams();

				switch (commandType) {
				case NetworkCommand.MYNAMEIS:
					int index = Helper.getPlayerIndexByID(players, senderID);
					if (index != -1 && stringParams.size() == 1) players.get(index).set_name(stringParams.get(0));
					break;
					
				case NetworkCommand.PLAYERMOVEMENTVECTOR:
					int index2 = Helper.getPlayerIndexByID(players, senderID);
					if (index2 != -1 && floatParams.size() == 2) players.get(index2).set_movementVector(floatParams.get(0), floatParams.get(1));
					break;
				}
			}
		}
		////////////////////////////////////////////////
	}



	private void handle_gameObjects() {
		if (phase != PHASE_INGAME) return;
		////////////////////////////////////////////////
		// Update the gameObjects
		for (int i = 0; i < gameObjects.size(); i++) {
			GameObject go = gameObjects.get(i);

			go.update(gameObjects);


			if (go.getClass() == EndIndicator.class && go.get_pos().y > 0) {
				gameObjects.remove(i);
				i--;
				fetch_nextLevel(go.get_pos().y);
				continue;
			}


			if (go.get_pos().y > G.playarea_height) {
				gameObjects.remove(i);
				i--;
				continue;
			}
		}

		if (gameObjects.size() == players.size()*2) fetch_nextLevel(0);
		////////////////////////////////////////////////

	}



	private void handle_sendUpdates() {
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



	/////////////////////////////////////////////////////////////////////////////////////////////////////

	void formCommands() {
		NetworkCommand nc = new NetworkCommand(NetworkCommand.PLAYERINFO, getPlayerNames(), null);
		pendingCommands.add(nc);
	}



	void fetch_nextLevel(float yOffset) {
		Level nextLevel = world.nextLevel();
		ArrayList<GameObject> new_gos = nextLevel.get_gameObjects();

		for (GameObject new_go : new_gos) {
			PVector oldPos = new_go.get_pos();
			new_go.set_pos(oldPos.x, oldPos.y - nextLevel.get_height() + yOffset);
		}

		gameObjects.addAll(new_gos);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////



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



	/////////////////////////////////////////////////////////////////////////////////////////////////////

	public int generate_uniqueID() {
		if (netServer != null) {
			return netServer.generate_uniqueID();
		}
		return -1;
	}



	public void activate(int port) {
		netServer = new NetServer(port, G.p);
		gameObjects.clear();
		players.clear();
		pendingCommands.clear();
		phase = PHASE_LOBBY;
		
		gameObjects.add(new Sombrero(netServer.generate_uniqueID()));
	}



	public void go_ingame() {
		phase = PHASE_INGAME;
		
		for(GameObject go : gameObjects) {
			go.touch();
		}
		
		ArrayList<Float> floatArgs = new ArrayList<Float>();
		floatArgs.add((float)phase);
		pendingCommands.add( new NetworkCommand(NetworkCommand.SERVER_STATECHANGE, null, floatArgs) );
	}



	public void deactivate() {
		if (netServer != null && netServer.active()) netServer.stop();
		phase = PHASE_INACTIVE;
	}
}
