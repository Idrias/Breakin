void setup() {
  size(600, 600);
  frameRate(144);
  setup_gvars();
}

void draw() {
  gameServer.update();
  gameClient.update();
  printFPS();
}