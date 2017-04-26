package graphics.ui;

import other.G;
import processing.core.PApplet;



public class Window {

	int posX, posY, sizeX, sizeY;



	public Window(int _posX, int _posY, int _sizeX, int _sizeY) {
		posX = _posX;
		posY = _posY;
		sizeX = _sizeX;
		sizeY = _sizeY;
	}



	public void disp() {
		G.p.rectMode(PApplet.CORNER);
		G.p.stroke(120);
		G.p.fill(230, 140);
		G.p.rect(posX - sizeX / 2, posY - sizeY / 2, sizeX, sizeY, 12);
		G.p.stroke(0);
		G.p.rectMode(PApplet.CENTER);
	}
}
