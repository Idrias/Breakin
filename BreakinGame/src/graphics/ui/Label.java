package graphics.ui;

import other.G;
import processing.core.PFont;



public class Label {

	int posX, posY, size, sSize;
	PFont font;
	int iro, kage; // TODO had to change color to int! color: #FFDD99 ->
					// 0xFFDD99
	String text;
	boolean shadow, border;



	public Label(int _posX, int _posY, int _size, PFont _font, int _iro, String _text) {
		posX = _posX;
		posY = _posY;
		size = _size;
		font = _font;
		iro = _iro;
		text = _text;
	}


	 //    Label(int,       int,       int,       PFont,       int,      String,       boolean,         int,        boolean,         int)
	public Label(int _posX, int _posY, int _size, PFont _font, int _iro, String _text, boolean _shadow, int _sSize, boolean _border, int _kage) {
		posX = _posX;
		posY = _posY;
		size = _size;
		font = _font;
		iro = _iro;
		text = _text;
		shadow = _shadow;
		border = _border;
		kage = _kage;
		sSize = _sSize;
	}



	public void disp() {

		G.p.textFont(font, size);

		G.p.fill(kage, 255);

		if (border) {
			G.p.text(text, posX + 1, posY);
			G.p.text(text, posX - 1, posY);
			G.p.text(text, posX, posY + 1);
			G.p.text(text, posX, posY - 1);
		}

		if (shadow) {
			G.p.text(text, posX + sSize, posY + sSize);
		}

		G.p.fill(iro, 255);
		G.p.text(text, posX, posY);
	}
}
