package other;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import game.Player;
import network.utilities.DecompressResult;
import network.utilities.NetworkContainer;
import network.utilities.NetworkEntity;
import processing.core.PVector;



public class Helper {

	public static NetworkEntity getEntityByID(ArrayList<NetworkEntity> nes, int id) {
		for (NetworkEntity ne : nes)
			if (ne.getUniqueID() == id) return ne;
		return null;
	}



	public static int getEntityIndexByID(ArrayList<NetworkEntity> nes, int id) {
		int index = 0;
		for (NetworkEntity ne : nes)
			if (ne.getUniqueID() == id)
				return index;
			else
				index++;
		return -1;
	}



	public static int getPlayerIndexByID(ArrayList<Player> players, int id) {
		int index = 0;
		for (Player p : players)
			if (p.get_playerID() == id)
				return index;
			else
				index++;
		return -1;

	}

	
	
	public static PVector DrawToGamePos(PVector drawPos) {
		//TODO DEV
		return new PVector((drawPos.x*G.playarea_width)/G.p.width, (drawPos.y*G.playarea_height)/G.p.height);
	}

	
	public static PVector GameToDrawPos(PVector gamePos) {
		float limitingFactor;
		if(G.p.width > G.p.height) limitingFactor = G.p.height;
		else limitingFactor = G.p.width;
		limitingFactor /= 1;
		//return new PVector(gamePos.x / G.playarea_width * limitingFactor, gamePos.y / G.playarea_height * limitingFactor);
		return new PVector((gamePos.x / G.playarea_width) * G.p.width, (gamePos.y / G.playarea_height) * G.p.height);
	}



	public static PVector GameToDrawSize(PVector gameSize) {
		//return new PVector(gameSize.x * G.p.width, gameSize.y * G.p.height);
		return GameToDrawPos(gameSize);
		//return new PVector((gameSize.x / G.playarea_width) * G.p.width, (gameSize.y / G.playarea_height) * G.p.height);
	}



	public static DecompressResult getNetworkContainerFromByteArray(String messageBuffer, byte[] incomingBytes) {
		String incomingString = "";

		try {
			// We need to work with the message as string. To not corrupt the
			// byte[] object data we have to use a "bijective" encoding
			// (ISO-8859-1)!
			// http://stackoverflow.com/a/21032684
			// For the incoming string add the leftover from last time + the
			// bytes converted to string
			incomingString = messageBuffer + new String(incomingBytes, "ISO-8859-1");
		}
		catch (UnsupportedEncodingException e) {
			G.println("Fatal Error: Unsupported Encoding!");
		}

		ArrayList<String> newMessages = new ArrayList<String>();
		for (String message : incomingString.split(G.NET_SPLITSTRING))
			newMessages.add(message);
		int newMessageCount = newMessages.size();

		if (incomingString.endsWith(G.NET_SPLITSTRING)) {
			messageBuffer = "";
		}
		else {
			messageBuffer = newMessages.get(newMessageCount - 1);
			newMessages.remove(newMessageCount - 1);
			newMessageCount--;
		}

		while (newMessages.contains(""))
			newMessages.remove("");

		ArrayList<NetworkContainer> returnedContainers = new ArrayList<NetworkContainer>();

		for (String msg : newMessages) {
			byte[] bytes = null;
			try {
				bytes = msg.getBytes("ISO-8859-1");
			}
			catch (UnsupportedEncodingException e) {
				G.println("Fatal Error: Unsupported Encoding!");
			}

			NetworkContainer nc = NetworkContainer.decompress(bytes);
			if (nc != null) returnedContainers.add(nc);
		}

		return new DecompressResult(returnedContainers, messageBuffer);
	}

}
