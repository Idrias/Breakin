package game;

import java.util.ArrayList;
import game.actors.Dummy;
import game.actors.GameObject;
import graphics.MainMenu;
import network.NetClient;
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



	public GameClient() {
		netClient = new NetClient(G.p);
		gos = new ArrayList<GameObject>();
	}



	public void update() {
		G.p.background(0);
		switch (gamePhase) {
		case PHASE_PREPAREMENU:
			mainmenu = new MainMenu();
			gamePhase = PHASE_MAINMENU;
			break;
		case PHASE_MAINMENU:
			mainmenu.draw();
			break;
		}
		fetch_nes();
		handle_gos();
		netClient.addToSendingList("Hi", new int[] { 1, 2, 3, 4 });
		netClient.pushSendingList();
	}



	void handle_gos() {
		for (GameObject go : gos) {
			if (G.CLIENTSIDE_PREDICTIONS) go.update();
			go.draw();
		}
	}



	void fetch_nes() {
		ArrayList<NetworkEntity> nes = null;
		ArrayList<NetworkContainer> containers = netClient.receive();
		int numberContainers = containers.size();
		if (numberContainers >= 1) {
			nes = containers.get(containers.size() - 1).nes;
		}
		if (nes == null) return;

		ArrayList<Integer> IDs = new ArrayList<Integer>();

		for (NetworkEntity ne : nes) {
			IDs.add(ne.get_id());
			int ne_id = ne.get_id();
			boolean found = false;
			for (GameObject go : gos) {
				if (ne_id == go.ne().get_id()) {
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
			if (!IDs.contains(go.get_networkEntity().get_id())) {
				gos.remove(i);
				i--;
			}

		}
	}



	void disconnect() {
		netClient.stop();
	}
}
