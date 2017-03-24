package network.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import other.G;
import processing.core.PVector;



public class NetworkEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int actorType; // Which type of actor does this NE support?
	private int uniqueID; // The unique ID for networking purposes
	PVector pos;
	PVector speed;
	HashMap<String, Integer> values;



	public NetworkEntity(int actorType, int uniqueID) {
		this.actorType = actorType;
		this.setUniqueID(uniqueID);

		pos = new PVector();
		speed = new PVector();
		values = new HashMap<String, Integer>();
	}



	public void addKeyValuePair(String k, int v) {
		values.put(k, v);
	}



	public PVector get_pos() {
		return pos;
	}



	public PVector get_speed() {
		return speed;
	}



	public void set_pos(PVector pos) {
		this.pos = pos;
	}



	public void set_speed(PVector speed) {
		this.speed = speed;
	}



	public int get_id() {
		return getUniqueID();
	}



	public int get_type() {
		return actorType;
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



	public static NetworkEntity decompress(byte[] byteArray) {
		try {
			ByteArrayInputStream byteStream = new ByteArrayInputStream(byteArray);
			ObjectInputStream objectStream = new ObjectInputStream(byteStream);

			NetworkEntity ne = (NetworkEntity) objectStream.readObject();

			objectStream.close();
			byteStream.close();

			return ne;
		}

		catch (Exception e) {
			G.println("FATAL ERROR: decompression failed!");
			e.printStackTrace();
			return null;
		}
	}



	public int getUniqueID() {
		return uniqueID;
	}



	public void setUniqueID(int uniqueID) {
		this.uniqueID = uniqueID;
	}
}
