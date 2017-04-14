package network.utilities;

import java.io.Serializable;
import java.util.ArrayList;



public class NetworkCommand implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int PLAYERINFO = 34;
	public static final int MYNAMEIS = 35;
	public static final int PLAYERMOVEMENTVECTOR = 36;

	int commandType = 0;
	ArrayList<String> stringParams;
	ArrayList<Float> floatParams;



	public NetworkCommand() {
		stringParams = new ArrayList<String>();
		floatParams = new ArrayList<Float>();
	}



	public NetworkCommand(int commandType) {
		this();
		this.commandType = commandType;
	}



	public NetworkCommand(int commandType, ArrayList<String> stringParams, ArrayList<Float> floatParams) {
		this();
		this.commandType = commandType;

		if (stringParams != null) this.stringParams = stringParams;

		if (floatParams != null) this.floatParams = floatParams;
	}



	/// Getters

	public ArrayList<String> get_stringParams() {
		return stringParams;
	}



	public ArrayList<Float> get_floatParams() {
		return floatParams;
	}



	public int get_commandType() {
		return commandType;
	}

	///



	/// Setters

	public void set_stringParams(ArrayList<String> stringParams) {
		this.stringParams = stringParams;
	}



	public void set_floatParams(ArrayList<Float> floatParams) {
		this.floatParams = floatParams;
	}



	public void set_commandType(int commandType) {
		this.commandType = commandType;
	}

	///

}
