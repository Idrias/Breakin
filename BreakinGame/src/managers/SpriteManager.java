package managers;

import java.util.HashMap;
import other.G;
import processing.core.PImage;



public class SpriteManager {

	HashMap<String, PImage> sprites;
	HashMap<String, PImage[]> animations;



	public SpriteManager() {
		sprites = new HashMap<String, PImage>();
		animations = new HashMap<String, PImage[]>();
	}



	public void addSprite(String name, String path) {
		if (name.charAt(0) == 'S') { // STATIC SPRITE
			sprites.put(name, G.p.loadImage(path));
		}
		else { // ANIMATED SPRITE

			char c = ' ';
			int i = 0;

			do {
				c = path.charAt(i);
				i++;
			} while (c != '.');

			int a = Character.getNumericValue(path.charAt(i - 2));

			PImage[] frames = new PImage[a * a];
			int W = G.p.loadImage(path).width / a;
			int H = G.p.loadImage(path).height / a;
			for (int j = 0; j < frames.length; j++) {
				int x = j % a * W;
				int y = j / a * H;
				frames[j] = G.p.loadImage(path).get(x, y, W, H);
			}
			animations.put(name, frames);
		}
	}



	public void dispSprite(String name, int x, int y) {
		G.p.image(sprites.get(name), x, y);
	}



	public void dispSprite(String name, int x, int y, float w, float h) {
		G.p.image(sprites.get(name), x, y, w, h);
	}



	public void dispAnimation(String name, int x, int y, int speed, int frames) {
		G.p.image(animations.get(name)[(G.p.millis() / speed) % frames], x, y);
	}



	public void dispAnimation(String name, int x, int y, float w, float h, int speed, int frames) {
		G.p.image(animations.get(name)[(G.p.millis() / speed) % frames], x, y, w, h);
	}
}
