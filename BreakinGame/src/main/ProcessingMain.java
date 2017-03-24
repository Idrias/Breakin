package main;

import other.G;
import processing.core.PApplet;



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
		G.printFPS();
	}
}
