void setup() {
  fullScreen(P2D);
  frameRate(144);
  setup_gvars();
  
  audio.play("Music:MainMenu");
}

void draw() {
  gameServer.update();
  gameClient.update();
  //printFPS();

}




void keyPressed() {
  switch(key) {
    case 's':
      audio.stopAll();
      break;
    case 'p':
      audio.pauseAll();
      break;
    case 'r':
      audio.resumeAll();
      break;
    case 't':
      audio.play("Music:Trump");
  }
}