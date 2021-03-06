package network.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.io.UTFDataFormatException;
import java.util.ArrayList;
import other.G;



public class NetworkContainer implements Serializable {

	private static final long serialVersionUID = 1L;
	ArrayList<NetworkEntity> nes = null;
	ArrayList<NetworkCommand> commands = null;

	// Usually we want to send NetworkCotnainers to all clients.
	// If this is set only a specific client is the destination!
	String destinationIP = "";
	int destinationID = -1;
	int senderID = -1;



	public NetworkContainer() {
		nes = new ArrayList<NetworkEntity>();
		commands = new ArrayList<NetworkCommand>();
	}



	public void set_nes(ArrayList<NetworkEntity> nes) {
		this.nes = nes;
	}



	public void set_commands(ArrayList<NetworkCommand> commands) {
		this.commands = commands;
	}



	public void set_destination(String ip, int id) {
		this.destinationIP = ip;
		this.destinationID = id;
	}



	public void set_sender(int id) {
		senderID = id;
	}



	public ArrayList<NetworkEntity> get_nes() {
		return nes;
	}



	public ArrayList<NetworkCommand> get_commands() {
		return commands;
	}



	public int get_destinationID() {
		return destinationID;
	}



	public int get_senderID() {
		return senderID;
	}



	public String get_destinationIP() {
		return destinationIP;
	}



	public byte[] compress() {
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
			objectStream.writeObject(this);
			objectStream.close();
			byteStream.close();

			return byteStream.toByteArray();
		}
		catch (Exception e) {
			G.println("FATAL ERROR: compression failed!");
			return null;
		}
	}



	public static NetworkContainer decompress(byte[] byteArray) {
		try {
			ByteArrayInputStream byteStream = new ByteArrayInputStream(byteArray);
			ObjectInputStream objectStream = new ObjectInputStream(byteStream);

			NetworkContainer nc = (NetworkContainer) objectStream.readObject();

			objectStream.close();
			byteStream.close();

			return nc;
		}

		catch (StreamCorruptedException e) {
			G.println("ERROR: StreamCorruptedException during decompress!");
			return null;
		}

		catch (UTFDataFormatException e) {
			G.println("ERROR: UTFDataFormatException during decompress!");
			return null;
		}

		catch (InvalidClassException e) {
			G.println("ERROR: InvalidClassException during decompress");
			return null;
		}
		catch (Exception e) {
			G.println("FATAL ERROR: decompression failed!");
			e.printStackTrace();
			return null;
		}
	}
}
