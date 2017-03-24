package network;

import java.util.ArrayList;
import java.util.HashMap;
import network.utilities.DecompressResult;
import network.utilities.NetworkContainer;
import network.utilities.NetworkEntity;
import other.G;
import other.Helper;
import processing.core.PApplet;
import processing.net.Client;
import processing.net.Server;



public class NetServer extends Server {

	int uniqueID_count = 0;
	HashMap<Client, String> clientMessageBuffers;



	////////////////////////////////////////////////
	// Constructors
	public NetServer(int port, PApplet g) {
		super(g, port);
		clientMessageBuffers = new HashMap<Client, String>();
	}



	public NetServer(PApplet g) {
		this(4242, g);
	}
	// End of constructors
	////////////////////////////////////////////////



	////////////////////////////////////////////////
	// Send NetworkContainer to all clients!
	public void pushNetworkContainer(NetworkContainer nc) {
		byte[] bytes = nc.compress();
		write(bytes);
		write(G.NET_SPLITSTRING);
	}
	////////////////////////////////////////////////



	////////////////////////////////////////////////
	// Send networkEntitites as NetworkContainer to all clients!
	public void pushEntities(ArrayList<NetworkEntity> networkEntities) {
		NetworkContainer nc = new NetworkContainer();
		nc.set_nes(networkEntities);
		pushNetworkContainer(nc);
	}
	////////////////////////////////////////////////



	////////////////////////////////////////////////
	// Receive messages from clients
	public ArrayList<NetworkContainer> receive() {

		ArrayList<NetworkContainer> returnVals = new ArrayList<NetworkContainer>();
		Client sender;

		while (true) {
			sender = available();
			if (sender == null) break;

			String messageBuffer = clientMessageBuffers.get(sender);

			if (messageBuffer != null) {
				DecompressResult dr = Helper.getNetworkContainerFromByteArray(messageBuffer, sender.readBytes());
				clientMessageBuffers.put(sender, dr.get_messageBuffer());

				for (NetworkContainer nc : dr.get_networkContainers()) {
					returnVals.add(nc);
				}
			}
			else {
				// The client is not registered in clientMessageBuffers.
				// Therefore we won't accept the message!
				sender.readString();
			}
		}

		return returnVals;
	}
	////////////////////////////////////////////////



	////////////////////////////////////////////////
	// Handle arriving / departing clients
	public void addNewClient(Client client) {
		clientMessageBuffers.put(client, "");
	}



	public void removeDisconnectedClient(Client client) {
		clientMessageBuffers.remove(client);
	}



	////////////////////////////////////////////////
	// Get an unique network id for gameObjects
	public int generate_uniqueID() {
		uniqueID_count++;
		return uniqueID_count;
	}
	////////////////////////////////////////////////
}
