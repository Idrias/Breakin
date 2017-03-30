package graphics.ui;

import other.G;
import processing.core.PApplet;



public class InputBox {

	int posX, posY, sizeX, sizeY;
	String lastInput = new String(); // TODO remove? unused?
	String currentInput = new String(); // can also be = ""
	String allowedSpecialKeys = ":.$€";
	char c;
	boolean canType, numOnly;



	public InputBox(int _posX, int _posY, int _sizeX, int _sizeY, boolean _numOnly) {
		posX = _posX;
		posY = _posY;
		sizeX = _sizeX;
		sizeY = _sizeY;
		numOnly = _numOnly;
	}



	public void disp() {

		if (canType)
			G.p.fill(255);
		else
			G.p.fill(200);
		G.p.beginShape();
		G.p.vertex(posX - sizeX / 2, posY - sizeY / 2);
		G.p.vertex(posX + sizeX / 2, posY - sizeY / 2);
		G.p.vertex(posX + sizeX / 2, posY + sizeY / 2);
		G.p.vertex(posX - sizeX / 2, posY + sizeY / 2);
		G.p.vertex(posX - sizeX / 2, posY - sizeY / 2);
		G.p.endShape();

		G.p.textAlign(PApplet.LEFT);
		G.p.textFont(G.arial);
		G.p.fill(30, 30, 30);
		G.p.text(currentInput, posX - sizeX / 2 + 10, posY + 10);

		if (canType) {

			boolean keyPressed = G.p.keyPressed;
			char key = G.p.key;

			if (!keyPressed) c = ' ';
			if (G.p.keyPressed && key != c) {

				// if(numOnly){
				// if(key == ('1' || '2' || '3' || '4' || '5' || '6' || '7' ||
				// '8' || '9' || '0' || '.'))
				// }

				c = key;
				if (key == PApplet.BACKSPACE && currentInput.length() > 0) {
					currentInput = currentInput.substring(0, currentInput.length() - 1);
				}

				else {
					if (Character.isLetterOrDigit(key) || allowedSpecialKeys.contains("" + key)) {
						if (numOnly && Character.isDigit(key) || !numOnly) currentInput = currentInput + key;
					}
				}
			}
		}

		if (G.p.mousePressed) {
			if (isOver(G.p.mouseX, G.p.mouseY)) {
				canType = true;
			}
			else {
				canType = false;
			}
		}
	}



	boolean isOver(int mx, int my) {
		if (mx > posX - sizeX / 2 && mx < posX + sizeX / 2 && my > posY - sizeY / 2 && my < posY + sizeY / 2) {
			return true;
		}
		else {
			return false;
		}
	}



	public String get_content() {
		return currentInput;
	}
}
