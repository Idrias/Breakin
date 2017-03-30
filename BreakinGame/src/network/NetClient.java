package network;

import java.util.ArrayList;
import java.util.HashMap;
import network.utilities.DecompressResult;
import network.utilities.NetworkCommand;
import network.utilities.NetworkContainer;
import other.G;
import other.Helper;
import processing.core.PApplet;
import processing.net.Client;



public class NetClient extends Client {

	NetworkContainer latestContainer;
	String messageBuffer = "";
	ArrayList<NetworkCommand> pendingCommands; // TODO same on gameserver/netserver
	int playerID = -1;



	//////// Constructors
	public NetClient(String ip, int port, PApplet g) {
		super(g, ip, port);
		pendingCommands = new ArrayList<NetworkCommand>();
		latestContainer = new NetworkContainer();
	}



	public NetClient(String ip, PApplet g) {
		this(ip, 4242, g);
	}



	public NetClient(PApplet g) {
		this("127.0.0.1", g);
	}
	//////// End of constructors



	public ArrayList<NetworkContainer> receive() {
		
		
		if (available() > 0) {
			DecompressResult dr = Helper.getNetworkContainerFromByteArray(messageBuffer, readBytes());
			messageBuffer = dr.get_messageBuffer();

			ArrayList<NetworkContainer> containers = dr.get_networkContainers(); // not
																					// null

			for (int i = 0; i < containers.size(); i++) {
				NetworkContainer c = containers.get(i);
				String destinationIP = c.get_destinationIP();
				int destinationID = c.get_destinationID();

				// BEGIN IF DESTINATION CHECK
				if (destinationIP.isEmpty()) {
					// All is good. This container is intended for all clients!
				}
				else {
					// Woah there! This container is for a specific client only!
					// Let's check if it's for me!
					if (destinationIP.equals(this.ip())) {
						// Container is for my ip!
						if (playerID == -1) {
							// My playerID is not set yet. This must be the
							// message to set it!
							playerID = destinationID;
							G.println("Client set playerID: " + playerID);
						}

						else if (playerID == destinationID) {
							// The container is for my ip and also for my id.
							// Means this is for me!
							// TODO do stuff?
						}

						else {
							// This container is for my ip, but not for my id.
							// Might be an error or there is more than one
							// client on my machine!
							// I'm not the only one???
							// Hellooooooo?? Anybody theeere?!
							containers.remove(i);
							i--;
						}

					}
					else {
						// Nope! This container is not for me! Dump it!
						containers.remove(i);
						i--;
					}
				}
				///// ENDIF
			}

			if(containers.size()>0) latestContainer = containers.get(containers.size() - 1);
			return containers;
		}
		return new ArrayList<NetworkContainer>();
	}



	public void addToPendingCommands(NetworkCommand nc) {
		pendingCommands.add(nc);
	}



	public void pushPendingCommands() {
		if (!active() || pendingCommands.size() == 0 || playerID == -1) return; //TODO where does playerid come from?
		NetworkContainer nc = new NetworkContainer();
		nc.set_sender(playerID);
		nc.set_commands(pendingCommands);
		write(nc.compress());
		write(G.NET_SPLITSTRING);
		pendingCommands.clear();
	}



	public NetworkContainer getLatestContainer() {
		return latestContainer;

	}
	////////////////////////////////////////////////////////
}
