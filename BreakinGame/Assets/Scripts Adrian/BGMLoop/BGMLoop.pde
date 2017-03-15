import ddf.minim.*;

Minim minim;
AudioPlayer player;

void setup() {
  minim = new Minim(this);
  player = minim.loadFile("MuteCity.mp3");

  player.setLoopPoints(0, 54869);
  
  player.loop();
}

void draw(){
}