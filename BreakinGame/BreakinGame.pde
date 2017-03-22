void setup() {
  fullScreen(P2D);
  frameRate(60);
  noCursor();
  setup_gvars();
}

void draw() {
  gameServer.update();
  gameClient.update();
}

