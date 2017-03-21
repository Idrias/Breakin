
class SpriteManager {

  HashMap<String, PImage> sprites;
  HashMap<String, PImage[]> animations;

  SpriteManager() {
    sprites = new HashMap<String, PImage>();
    animations = new HashMap<String, PImage[]>();
  }

  void addSprite(String name, String path) {
    if (name.charAt(0) == 'S') {                    //STATIC SPRITE
      sprites.put(name, loadImage(path));
    } else {                                        //ANIMATED SPRITE

      char c = ' ';
      int i = 0;

      do {
        c = path.charAt(i);
        i++;
      } while (c != '.');

      int a = Character.getNumericValue(path.charAt(i - 2));
      
      PImage[] frames = new PImage[a*a];
      int W = loadImage(path).width/a;
      int H = loadImage(path).height/a;
      for (int j = 0; j < frames.length; j++) {
        int x = j%a*W;
        int y = j/a*H;
      frames[j] = loadImage(path).get(x, y, W, H);
      }
      animations.put(name, frames);
    }
  }

  void dispSprite(String name, int x, int y) {
    image(sprites.get(name), x, y);
  }
  void dispSprite(String name, int x, int y, float w, float h) {
    image(sprites.get(name), x, y, w, h);
  }
  
  void dispAnimation(String name, int x, int y, int speed, int frames){
    image(animations.get(name)[(millis() / speed)%frames], x, y);
  }
  
  void dispAnimation(String name, int x, int y, float w, float h, int speed, int frames){
    image(animations.get(name)[(millis() / speed)%frames], x, y, w, h);
  }
}