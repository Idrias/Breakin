//Created March 19th 2017
import ddf.minim.*;

Minim hoverSFX = new Minim(this);
Minim nextSFX = new Minim(this);
Minim backSFX = new Minim(this);
Minim bgm = new Minim(this);                        //bgm = background music
AudioPlayer hoverPlayer;
AudioPlayer nextPlayer;
AudioPlayer backPlayer;
AudioPlayer bgmPlayer;

Button single, multi, settings, credits, quit, startSP, difficulty, backTo1, graphics, audio, language;
PImage img, cursor, breakin;
PFont font;
int currentMenu = 1; //1 = Main Menu, 2 = SinglePlayer, 3 = Multiplayer, 4 = Settings, 5 = Credits, 6 = Quit 

void setup() {
  //Load content
  img = loadImage("Button.png");
  cursor = loadImage("Tacursor.png");
  breakin = loadImage("Breakin.png");
  font = createFont("komikax.ttf", displayHeight/27);  

  hoverPlayer = hoverSFX.loadFile("hover.wav");  //Minim ist ein Hurensohn und muckt bei .ogg files rum
  nextPlayer = nextSFX.loadFile("next.wav");
  backPlayer = backSFX.loadFile("back.wav");
  bgmPlayer = bgm.loadFile("bgmMainMenu.mp3", 1024);    //noch in arbeit >> schlagzeug und square suckt derbst .>>

  //Modes usw
  fullScreen(P2D);
  shapeMode(CENTER);
  textureMode(NORMAL);
  imageMode(CENTER);
  textAlign(CENTER, CENTER);
  textFont(font);
  noCursor();

  //Create Buttons      Position X      Position Y                              Size X          Size Y          
  single =   new Button(displayWidth/2, displayHeight/2 +     displayHeight/12, displayWidth/3, displayHeight/15, 2, "SINGLEPLAYER", true);
  multi =    new Button(displayWidth/2, displayHeight/2 + 2 * displayHeight/12, displayWidth/3, displayHeight/15, 1, "MULTIPLAYER", true);
  settings = new Button(displayWidth/2, displayHeight/2 + 3 * displayHeight/12, displayWidth/3, displayHeight/15, 4, "SETTINGS", true);
  credits =  new Button(displayWidth/2, displayHeight/2 + 4 * displayHeight/12, displayWidth/3, displayHeight/15, 1, "CREDITS", true);
  quit =     new Button(displayWidth/2, displayHeight/2 + 5 * displayHeight/12, displayWidth/3, displayHeight/15, 6, "QUIT", false);

  startSP =  new Button(displayWidth/2, displayHeight/2 +     displayHeight/12, displayWidth/3, displayHeight/15, 2, "START GAME", true);
  difficulty=new Button(displayWidth/2, displayHeight/2 + 2 * displayHeight/12, displayWidth/3, displayHeight/15, 7, "DIFFICULTY: NORMAL", true);
  backTo1 =  new Button(displayWidth/2, displayHeight/2 + 5 * displayHeight/12, displayWidth/3, displayHeight/15, 1, "BACK", false);
  
  graphics = new Button(displayWidth/2, displayHeight/2 +     displayHeight/12, displayWidth/3, displayHeight/15, 4, "GRAPHICS", true);
  audio =    new Button(displayWidth/2, displayHeight/2 + 2 * displayHeight/12, displayWidth/3, displayHeight/15, 4, "SOUND", true);
  language = new Button(displayWidth/2, displayHeight/2 + 3 * displayHeight/12, displayWidth/3, displayHeight/15, 4, "LANGUAGE", true);
  
  //Misc
  bgmPlayer.setLoopPoints(0, 52561);
  bgmPlayer.loop();
  fft = new FFT(bgmPlayer.bufferSize(), bgmPlayer.sampleRate());
}


void draw() {
  println(frameRate);

  background(255);

  drawWaves();
  colorMode(RGB, 255);

  stroke(0);
  strokeWeight(1);
  image(breakin, displayWidth/2, 3 * displayHeight/12, displayWidth/1.5, displayWidth/5);

  switch(currentMenu) {
  case 1:                     //Main Menu
    single.disp();
    multi.disp();
    settings.disp();
    credits.disp();
    quit.disp();
    break;
  case 2:                    //Single Player Menu
    startSP.disp();
    difficulty.disp();
    backTo1.disp();
    break;
  case 4:        //Settings
  graphics.disp();
  audio.disp();
  language.disp();
  backTo1.disp();  
    break;
  case 6:                    //Quit
    exit();
    break;
  case 7:                    //Difficulty Select
    startSP.disp();
    difficulty.disp();
    backTo1.disp();
    difficulty.text = changeDifficulty(difficulty.text);
    currentMenu = 2;
    break;
  }
  
  //drawTrail(mouseX + 16, mouseY + 18, 15, 5, displayWidth/100, displayWidth/200, new PVector(255,40,40),new int[15], new int[15]);
  
  drawTrail(mouseX, mouseY);
  
  translate(mouseX + 32, mouseY + 36); //Ursprung wird an die Ecke unten Rechts vom Taco cursor gesetzt
  if (mousePressed) {
    rotate(-7*TWO_PI/360);
  }
  tint(255);
  image(cursor, -16, -18, 32, 32); //cursor is nich 32x32 weil ich mich verzeichnet hab :] und zu faul bin es zu korrigieren ;'}
}

String changeDifficulty(String s) {
  switch(s) {
  case "DIFFICULTY: EASY":
    s = "DIFFICULTY: NORMAL";
    break;
  case "DIFFICULTY: NORMAL":
    s = "DIFFICULTY: HARD";
    break;
  case "DIFFICULTY: HARD":
    s = "DIFFICULTY: EASY";
    break;
  }
  return s;
}