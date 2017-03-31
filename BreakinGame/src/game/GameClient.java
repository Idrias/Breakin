package game;

import java.net.ConnectException;
import java.util.ArrayList;
import game.actors.Dummy;
import game.actors.GameObject;
import graphics.MainMenu;
import network.NetClient;
import network.utilities.NetworkCommand;
import network.utilities.NetworkContainer;
import network.utilities.NetworkEntity;
import other.G;



public class GameClient {

	NetClient netClient;
	MainMenu mainmenu;
	ArrayList<GameObject> gos;

	final int PHASE_MAINMENU = 3;
	final int PHASE_PREPAREMENU = 2;
	int gamePhase = PHASE_PREPAREMENU;



	/////////////////////////////////////////////////////////////////////////////////////////////////////


	public GameClient(String ip, int port) {
		// To start a client which is connected to a server
		netClient = new NetClient(ip, port, G.p);
		gos = new ArrayList<GameObject>();
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
			handle_all();
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
		}

	}



	void update_gos() {
		for (GameObject go : gos) {
			if (G.CLIENTSIDE_PREDICTIONS) go.update();
			go.draw();
		}
	}



	/////////////////////////////////////////////////////////////////////////////////////////////////////


	void handle_all() {
		NetworkContainer nc = netClient.getLatestContainer();
		if (nc == null) return;

		handle_nes(nc);
		handle_commands(nc);
	}



	void handle_nes(NetworkContainer container) {

		ArrayList<NetworkEntity> nes = container.get_nes();

		if (nes == null) return;

		ArrayList<Integer> IDs = new ArrayList<Integer>();

		for (NetworkEntity ne : nes) {
			IDs.add(ne.get_id());
			int ne_id = ne.get_id();
			boolean found = false;
			for (GameObject go : gos) {
				if (ne_id == go.get_ne().get_id()) {
					go.ne = ne;
					found = true;
				}
			}
			if (!found) {
				switch (ne.get_type()) {
				case G.ACTORTYPE_DUMMY:
					gos.add(new Dummy(ne));
					break;
				}
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



	void handle_commands(NetworkContainer container) {
		ArrayList<NetworkCommand> ncs = container.get_commands();

		for (NetworkCommand nc : ncs) {

			int commandType = nc.get_commandType();
			ArrayList<String> stringParams = nc.get_stringParams();
			ArrayList<Float> floatParams = nc.get_floatParams();

			switch (commandType) {
			case NetworkCommand.PLAYERINFO:
				G.playerNames = stringParams;
				break;
			}
		}

	}



	/////////////////////////////////////////////////////////////////////////////////////////////////////


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
