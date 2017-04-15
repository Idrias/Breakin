package graphics.ui;

import other.G;
import processing.core.PApplet;
import processing.core.PImage;



public class Button {

	int posX, posY, sizeX, sizeY;
	int newMenu;
	boolean SFX; // true = nextSFX, false = backSFX
	String text, event;
	PImage img; // Texture
	int r = 0, g = 255, b = 0, a = 255;
	int u = 0, v = 1;
	
	boolean active = true;
  

	public Button(int _posX, int _posY, int _sizeX, int _sizeY, int _newMenu, String _text, boolean _SFX,
			PImage _texture) {
		posX = _posX;
		posY = _posY;
		sizeX = _sizeX;
		sizeY = _sizeY;
		newMenu = _newMenu;
		text = _text;
		SFX = _SFX;
		img = _texture.copy();
	}
	
	public Button(int _posX, int _posY, int _sizeX, int _sizeY, int _newMenu, String _text, boolean _SFX, boolean _active,
			PImage _texture) {
		posX = _posX;
		posY = _posY;
		sizeX = _sizeX;
		sizeY = _sizeY;
		newMenu = _newMenu;
		text = _text;
		SFX = _SFX;
		img = _texture.copy();
		active = _active;
	}
	
	
	//Unused constructer
	public Button(int _posX, int _posY, int _sizeX, int _sizeY, int _newMenu, String _text, boolean _SFX,
			PImage _texture, String _event) {
		posX = _posX;
		posY = _posY;
		sizeX = _sizeX;
		sizeY = _sizeY;
		newMenu = _newMenu;
		text = _text;
		SFX = _SFX;
		img = _texture.copy();
		event = _event;
	}



	public int disp() {
		
		if(active){
			if (isOver(G.p.mouseX, G.p.mouseY)) {
				r = 90;
				b = 90;
				G.audio.playNoRewind("SFX:Menu:Hover");
				if (!G.p.mousePressed && u == 1) { // workaround um das fehlende
													// mouseReleased zu ersetzen
					if (SFX) {
						G.audio.play("SFX:Menu:Next");
					}
					else {
						G.audio.play("SFX:Menu:Back");
					}
					
					
					if(event == "SET_DIFFICULTY"){
						setDifficulty();
					}
					
					
					
					return newMenu;
				}
				if (G.p.mousePressed) {
					r = 130;
					b = 130;
					u = 1;
					v = 0;
				}
				else {
					u = 0;
					v = 1;
				}
			}
			else {
				if (r == 90) { // hoverPlayer soll nur vom gehoverten Button
					           // rewindet werden
					G.audio.stop("SFX:Menu:Hover");
				}
				r = 0;
				b = 0;
				u = 0;
				v = 1;
			}
		}
		
		
		
		if(active){
			a = 255;
		} else {
			a = 150;
			if (isOver(G.p.mouseX, G.p.mouseY)) {
				if(G.p.mousePressed){
					G.audio.play("SFX:Menu:Buzzer");
				}
			}
		}
		
		
		G.p.textFont(G.defaultFont);
		G.p.textAlign(PApplet.CENTER, PApplet.CENTER);
		G.p.tint(r, g, b, a);
		G.p.beginShape();
		G.p.texture(img);
		G.p.vertex(posX - sizeX / 2, posY - sizeY / 2, 0, u);
		G.p.vertex(posX + sizeX / 2, posY - sizeY / 2, 1, u);
		G.p.vertex(posX + sizeX / 2, posY + sizeY / 2, 1, v);
		G.p.vertex(posX - sizeX / 2, posY + sizeY / 2, 0, v);
		G.p.vertex(posX - sizeX / 2, posY - sizeY / 2, 0, u);
		G.p.endShape();
		G.p.fill(r / 2, 140, b / 2, a);
		G.p.text(text, posX + 3 + 2 * u, posY + 3 - G.p.height / 100 + 3 * u);  // Schatteneffekt
																				// vom
																				// Text
		G.p.fill(0, 40, 0, a);
		G.p.text(text, posX + 2 * u, posY - G.p.height / 100 + 2 * u);

		return -1;
	}



	boolean isOver(int mx, int my) {
		if (mx > posX - sizeX / 2 && mx < posX + sizeX / 2 && my > posY - sizeY / 2 && my < posY + sizeY / 2) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//WORK IN PROGRESS
	String setDifficulty(){
		
		return "";
	};
}
