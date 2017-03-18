void setup() {
  setup_gvars();
  frameRate(60);
  size(600, 600);
}

void draw() {
  background(0);

  gameServer.update();
  gameClient.update();

  println("FPS: " + frameRate);
  println();
}