void setup() {
  size(600, 900);
  setupVars();
  
  d = new Dummy(200, 200, 3, 3, 24);
  print(typeof(d));
}
  Dummy d;
  
void draw() {
  background(#D1FC6E);
  d.update();
  
  if(gameserver != null && gameclient != null) {
  if(frameCount%60==0) gameserver.update();
  delay(100);
  gameclient.checkMail();
  }
}

void keyPressed() {
  switch(key) {
    case 's': gameserver = new GameServer(); println("New GameServer created!"); break;
    case 'c': gameclient = new GameClient(); println("New GameClient created!"); break;
    case 'e': gameclient.stop(); gameserver.stop(); println("SERVER AND CLIENT CLOSED"); exit(); break;
  }

}