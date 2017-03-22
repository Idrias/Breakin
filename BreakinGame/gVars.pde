import processing.net.*;
import java.io.*;
import java.util.Map;

final String NET_SPLITSTRING = "-NEXT-";
final int ACTORTYPE_DUMMY = 3;

BreakinGame BREAKINGAME;
GameServer gameServer;
GameClient gameClient;
AudioManager audio;
SpriteManager sprite;

PFont defaultFont;
PFont Arial;
PImage defaultButtonTexture;

// Settings //
float NETWORK_UPDATERATE = 20;           // How often per second do we want to send updates from server to client?
boolean CLIENTSIDE_PREDICTIONS = true;   // Should the gameclient predict movements?
//////////////

//Graphic settins
enum graphics {
    None, 
    Off, 
    On, 
    Low, 
    Medium, 
    High
}

void setup_gvars() {
  BREAKINGAME = this;
  defaultButtonTexture = loadImage("/Assets/Graphics/Static_Sprites/Button.png");

  audio = new AudioManager();
  load_audio();
  sprite = new SpriteManager();
  load_sprites();

  gameServer = new GameServer();
  gameClient = new GameClient();


  shapeMode(CENTER);
  textureMode(NORMAL);
  imageMode(CENTER);
  textAlign(CENTER, CENTER);


  defaultFont = createFont("/Assets/Graphics/Fonts/komikax.ttf", displayHeight/27);
  Arial = createFont("FFScala", 32);
  textFont(defaultFont);
  //noCursor();
}



void load_audio() {
  //////// LOAD AUDIO
  String[] audioFiles = new String[]{
    "Music:Trump", "/Assets/Audio/Music/Trump.mp3", 
    "Music:MainMenu", "/Assets/Audio/Music/bgmMainMenu.wav", 
    "SFX:Game:Bounce", "/Assets/Audio/SFX/game/Bounce.mp3", 
    "SFX:Game:BrickBurst", "/Assets/Audio/SFX/game/BrickBurst.mp3", 
    "SFX:Game:MexicanAttack1", "/Assets/Audio/SFX/game/MexicanAttack_1.mp3", 
    "SFX:Game:MexicanAttack2", "/Assets/Audio/SFX/game/MexicanAttack_2.mp3", 
    "SFX:Game:MexicanAttack3", "/Assets/Audio/SFX/game/MexicanAttack_3.mp3", 
    "SFX:Game:MexicanAttack4", "/Assets/Audio/SFX/game/MexicanAttack_4.mp3", 
    "SFX:Game:MexicanAttack5", "/Assets/Audio/SFX/game/MexicanAttack_5.mp3", 
    "SFX:Game:MexicanLost", "/Assets/Audio/SFX/game/MexicanLost.mp3", 
    "SFX:Menu:Back", "/Assets/Audio/SFX/menu/back.wav", 
    "SFX:Menu:Hover", "/Assets/Audio/SFX/menu/hover.wav", 
    "SFX:Menu:Next", "/Assets/Audio/SFX/menu/next.wav", 
  };

  for (int i=1; i<audioFiles.length; i+=2) {
    String name = audioFiles[i-1];
    String location = audioFiles[i];
    println("Adding file " + name + " from " + location + ".");
    audio.addAudio(name, location);
  }
  ///////////// AUDIO LOADED
}


void load_sprites() {
  String[] imageFiles = new String[]{

    //Static
    "Static:BreakinLogo", "/Assets/Graphics/Static_Sprites/Breakin.png", 
    "Static:Tacursor", "/Assets/Graphics/Static_Sprites/Tacursor.png", 

    //Animated
    "Anim:Mexican", "/Assets/Graphics/Spritesheets/Juan_2.png", 
    "Anim:DestructedWallBrick", "/Assets/Graphics/Spritesheets//DestructedWallBrick_3.png"
  };

  for (int i = 1; i < imageFiles.length; i += 2) {
    String name = imageFiles[i-1];
    String location = imageFiles[i];
    println("Adding sprite " + name + " from " + location + ".");
    sprite.addSprite(name, location);
  }
}

void printFPS() {
  println("FPS: " + frameRate);
  println();
}