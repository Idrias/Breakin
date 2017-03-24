package network.utilities;

import java.util.ArrayList;



public class DecompressResult {

	ArrayList<NetworkContainer> networkContainers;
	String messageBuffer;



	public DecompressResult(ArrayList<NetworkContainer> networkContainers, String messageBuffer) {
		this.networkContainers = networkContainers;
		this.messageBuffer = messageBuffer;
	}



	public ArrayList<NetworkContainer> get_networkContainers() {
		return networkContainers;
	}



	public String get_messageBuffer() {
		return messageBuffer;
	}
}
