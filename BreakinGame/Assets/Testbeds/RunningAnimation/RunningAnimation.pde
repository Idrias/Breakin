PImage spritesheet;
int i = 6;
PImage[] sprites = new PImage[i*i];


void setup() {

  size(512, 512);

  spritesheet = loadImage("Explosion_1_Anim.png");
  int W = spritesheet.width/i;
  int H = spritesheet.height/i;
  for (int j=0; j<sprites.length; j++) {
    int x = j%i*W;
    int y = j/i*H;
    sprites[j] = spritesheet.get(x, y, W, H);
  }
}

void draw() {
  background(255);
  image(sprites[(millis() / 36)%36],0,0, 640, 480);  //ka wie gross der am ende angezeigt werden soll
}