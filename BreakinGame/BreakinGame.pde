void setup() {
  setup_gvars();
  frameRate(60);
  size(600, 600);
}

void draw() {
  background(0);
  int a = millis();
  gameServer.update();
  int b = millis();
  gameClient.update();
  int c = millis();
  
  servertime += b-a;
  clienttime += c-a;
  println("GameServer took " + (b-a) + " ms.");
  println("GameClient took " + (c-a) + " ms.");
  println("Servertime: " + servertime + "ms | Clienttime: " + clienttime + "ms");
  println();
}