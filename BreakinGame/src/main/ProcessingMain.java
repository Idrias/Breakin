package main;

import other.G;
import processing.core.PApplet;
import processing.net.Client;
import processing.net.Server;



public class ProcessingMain extends PApplet {

	public static void main(String[] args) {
		PApplet.main("main.ProcessingMain");
	}



	public void settings() {
		fullScreen(P2D);
		
	}



	public void setup() {
		// Set a global variable to reference this PApplet
		frameRate(144);
		noCursor();
		shapeMode(CENTER);
		textureMode(NORMAL);
		imageMode(CENTER);
		textAlign(CENTER, CENTER);
		G.setup(this);
	}



	public void draw() {
		G.gameServer.update();
		G.gameClient.update();
	}



	public void serverEvent(Server s, Client c) {
		G.newClients.add(c);
	}



	public void disconnectEvent(Client c) {
		G.disconnectedClients.add(c);
	}



	public void keyPressed() {
		switch (G.p.key) {
		case '#':
			G.gameServer.deactivate();
			G.gameClient.disconnect();
			G.audio.stopAll();
			G.setup(this);
			break;
		}

		int k = Character.getNumericValue(key);
		if (k >= 0 && k < 256) {
			G.keys[k] = true;
		}
	}



	public void keyReleased() {
		int k = Character.getNumericValue(key);
		if (k >= 0 && k < 256) {
			G.keys[k] = false;
		}
	}
}
