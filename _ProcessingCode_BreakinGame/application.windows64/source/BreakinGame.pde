void setup() {
  fullScreen(P2D);
  frameRate(144);
  noCursor();
  setup_gvars();
}

void draw() {
  gameServer.update();
  gameClient.update();
  printFPS();
}