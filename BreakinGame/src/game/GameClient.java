package game;

import java.net.ConnectException;
import java.util.ArrayList;
import game.actors.*;
import game.actors.bricks.IronBrick;
import game.actors.bricks.SimpleBrick;
import game.actors.bricks.TNTBrick;
import game.actors.dynamic.Helicopter;
import game.actors.dynamic.Mexican;
import game.actors.dynamic.Sombrero;
import graphics.MainMenu;
import graphics.ParallaxDesert;
import network.NetClient;
import network.utilities.NetworkCommand;
import network.utilities.NetworkContainer;
import network.utilities.NetworkEntity;
import other.G;
import processing.core.PVector;



public class GameClient {

	NetClient netClient;
	MainMenu mainmenu;
	ArrayList<GameObject> gos;

	final int PHASE_MAINMENU = 3;
	final int PHASE_PREPAREMENU = 2;
	final int PHASE_INGAME = 4;
	int gamePhase = PHASE_PREPAREMENU;
	int lastNetUpdate = 0;
	float netDeltaT;

	ParallaxDesert bgDesert;
	/////////////////////////////////////////////////////////////////////////////////////////////////////


	public GameClient(String ip, int port) {
		// To start a client which is connected to a server
		netClient = new NetClient(ip, port, G.p);
		gos = new ArrayList<GameObject>();
		netDeltaT = 1000 / G.NETWORK_UPDATERATE;
	}



	public GameClient() {
		// To start a client without server connection, e.g. for MainMenu
		gos = new ArrayList<GameObject>();
	}



	/////////////////////////////////////////////////////////////////////////////////////////////////////


	public void update() {

		if (netClient != null && netClient.active()) {
			// We are connected and should do a network update!
			// TODO not every frame?
			netClient.receive();
			net_handle_all();
			netClient.pushPendingCommands();
		}
		else if (netClient != null && !netClient.active()) disconnect();


		switch (gamePhase) {

		case PHASE_PREPAREMENU:
			mainmenu = new MainMenu();
			gamePhase = PHASE_MAINMENU;
			break;

		case PHASE_MAINMENU:
			mainmenu.draw();
			break;

		case PHASE_INGAME:
			update_INGAME();
			break;
		}

	}



	void update_INGAME() {
		if(bgDesert == null)bgDesert = new ParallaxDesert();
		bgDesert.disp();
		update_gos();
	}



	void update_gos() {
		for (GameObject go : gos) {
			if (G.CLIENTSIDE_PREDICTIONS) go.update(gos);
			go.draw();
		}
	}



	/////////////////////////////////////////////////////////////////////////////////////////////////////


	void net_handle_all() {
		NetworkContainer nc = netClient.getLatestContainer();
		if (nc == null) return;

		net_handle_nes(nc);
		net_handle_commands(nc);
		net_prepare_commands();
	}



	void net_handle_nes(NetworkContainer container) {

		ArrayList<NetworkEntity> nes = container.get_nes();

		if (nes == null) return;

		ArrayList<Integer> IDs = new ArrayList<Integer>();

		for (NetworkEntity ne : nes) {
			IDs.add(ne.get_id());
			int ne_id = ne.get_id();
			boolean found = false;
			for (GameObject go : gos) {
				if (ne_id == go.get_ne().get_id()) {
					go.set_ne(ne);
					found = true;
				}
			}
			if (!found) {
				Class<?> c = ne.get_type();

				if (c == Dummy.class)
					gos.add(new Dummy(ne));

				else if (c == SimpleBrick.class)
					gos.add(new SimpleBrick(ne));
				
				else if (c == IronBrick.class)
					gos.add(new IronBrick(ne));
				
				else if (c == TNTBrick.class)
					gos.add(new TNTBrick(ne));

				else if (c == Mexican.class) 
					gos.add(new Mexican(ne));
				
				else if (c == Helicopter.class) 
					gos.add(new Helicopter(ne));
				
				else if(c == Sombrero.class)
					gos.add(new Sombrero(ne));
			}
		}

		for (int i = 0; i < gos.size(); i++) {
			// Remove local GameObjects whose NetworkEntities aren't in the list
			// anymore!
			GameObject go = gos.get(i);
			if (!IDs.contains(go.get_ne().get_id())) {
				gos.remove(i);
				i--;
			}

		}
	}



	void net_handle_commands(NetworkContainer container) {
		ArrayList<NetworkCommand> ncs = container.get_commands();

		for (NetworkCommand nc : ncs) {

			int commandType = nc.get_commandType();
			ArrayList<String> stringParams = nc.get_stringParams();
			ArrayList<Float> floatParams = nc.get_floatParams();

			switch (commandType) {
			case NetworkCommand.PLAYERINFO:
				G.playerNames = stringParams;
				break;

			case NetworkCommand.SERVER_STATECHANGE:
				int newState = (int) (float) floatParams.get(0);

				switch (newState) {
				case GameServer.PHASE_INGAME:
					enterGame();
					break;
				}

				break;

			}
		}

	}



	void net_prepare_commands() {
		if (G.p.millis() - lastNetUpdate < netDeltaT) return;
		lastNetUpdate = G.p.millis();
		PVector mexicanPos = null;
		
		
		for(GameObject go : gos) {
			if(go.getClass() == Mexican.class) {
				Mexican m = (Mexican)go;
				if(m.getOwnerID() == netClient.get_playerID()) {
					mexicanPos = go.get_pos().copy();
				}
			}
		}
		
		
		// Prepare to send movement vector to server
		float movementX = 0, movementY = 0;
		if (G.keys[G.KEY_FORWARDS]) movementY -= 1;
		if (G.keys[G.KEY_BACKWARDS]) movementY += 1;
		if (G.keys[G.KEY_RIGHT]) movementX += 1;
		if (G.keys[G.KEY_LEFT]) movementX -= 1;
		
		//if(mexicanPos.x > G.p.mouseX)movementX -= 1;
		//Delse movementX += 1;

		ArrayList<Float> floatValues = new ArrayList<Float>();
		floatValues.add(movementX);
		floatValues.add(movementY);
		netClient.addToPendingCommands(new NetworkCommand(NetworkCommand.PLAYERMOVEMENTVECTOR, null, floatValues));

		if (G.CLIENTSIDE_PREDICTIONS && G.KEYBOARD_PREDICTIONS) {
			for (GameObject g : gos) {
				if (g instanceof Mexican) {
					// ABSCHIEBEN!
					Mexican m = (Mexican) g;
					if (m.getOwnerID() == netClient.get_playerID()) {
						m.set_speed(movementX, movementY);
					}
				}
			}
		}
	}



	/////////////////////////////////////////////////////////////////////////////////////////////////////

	public void enterGame() {
		if (gamePhase == PHASE_INGAME) return;
		G.audio.stopAll();
		mainmenu = null;
		gamePhase = PHASE_INGAME;
	}



	public void connect(String name, String ip) {

		// Find a creative name for the player if they didn't choose one
		if (name == null || name.equals("")) name = "" + Integer.toHexString(G.p.millis());

		String[] addressparts = ip.split(":");
		// TODO NOT SAFE FROM EXCEPTIONS

		try {
			if (addressparts.length == 0)
				// This means we want to connect to localhost!
				netClient = new NetClient(G.p);
			else if (addressparts.length == 1)
				netClient = new NetClient(addressparts[0], G.p);
			else if (addressparts.length == 2)
				netClient = new NetClient(addressparts[0], Integer.valueOf(addressparts[1]), G.p);
			else
				throw new ConnectException();
		}
		catch (ConnectException e) {
			G.println("Connection Exception: Probably wrong address!");
			netClient = null;
			return;
		}


		if (netClient != null && netClient.active()) {
			ArrayList<String> stringParams = new ArrayList<String>();
			stringParams.add(name);
			netClient.addToPendingCommands(new NetworkCommand(NetworkCommand.MYNAMEIS, stringParams, null));
		}

	}



	public void disconnect() {
		if (netClient != null && netClient.active()) netClient.stop();
		netClient = null;
		gos.clear();
		gamePhase = PHASE_PREPAREMENU;
	}

}
