import processing.opengl.*;

PImage desert;
float[] y_Image = new float[2];
float scrollSpeed = 2;

void setup() {
  fullScreen();
  desert = loadImage("Desert.png");

  y_Image[0] = 0;
  y_Image[1] = -displayHeight;
}


void draw() {
  
  println(frameRate);
  
  image(desert, 0, y_Image[0], displayWidth, displayHeight);
  image(desert, 0, y_Image[1], displayWidth, displayHeight);
  
  for (int i = 0; i < 2; i++) {

    if (y_Image[i] < displayHeight) {
      y_Image[i] += scrollSpeed;
    } else {
      y_Image[i] = -displayHeight;
    }
  }
}