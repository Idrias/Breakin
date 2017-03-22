import processing.net.*;
import java.io.*;
import java.util.Map;
import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;

final static String NET_SPLITSTRING = "-NEXT-";
final static int ACTORTYPE_DUMMY = 3;
final static int ID_SERVER = 42;

static BreakinGame BREAKINGAME;
GameServer gameServer;
GameClient gameClient;
AudioManager audio;
SpriteManager sprite;

PFont defaultFont;
PFont Arial;
PImage defaultButtonTexture;

ArrayList<Client> newClients;
ArrayList<Client> disconnectedClients;

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
  Arial = createFont("FFScala", 32);
  defaultButtonTexture = loadImage("/Assets/Graphics/Static Sprites/Button.png");
  sprite = new SpriteManager();
  load_sprites();

  BREAKINGAME = this;
  
  newClients = new ArrayList<Client>();
  disconnectedClients = new ArrayList<Client>();
  
  
  
  
  audio = new AudioManager();
  load_audio();
  
  gameServer = new GameServer();
  gameClient = new GameClient();
  
  
  shapeMode(CENTER);
  textureMode(NORMAL);
  imageMode(CENTER);
  textAlign(CENTER, CENTER);
  
  
  defaultFont = createFont("/Assets/Graphics/Fonts/komikax.ttf", displayHeight/27);
  textFont(defaultFont);
  //noCursor();
}



void load_audio() {
  //////// LOAD AUDIO
  String[] audioFiles = new String[]{
    "Music:Trump", "/Assets/Audio/Music/Trump.mp3",
    //"Music:MainMenu", "/Assets/Audio/Music/bgmMainMenu.mp3",
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
  
  for(int i=1; i<audioFiles.length; i+=2)  {
    String name = audioFiles[i-1];
    String location = audioFiles[i];
    println("Adding file " + name + " from " + location + ".");
    audio.addAudio(name, location);
  }
  audio.addAudio("Music:MainMenu", "/Assets/Audio/Music/bgmMainMenu.mp3", 512);
  ///////////// AUDIO LOADED  
}

void printFPS() {
  println("FPS: " + frameRate);
  println();
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





/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class AudioManager {
  HashMap<String, AudioPlayer> audios;

  AudioManager() {
    audios = new HashMap<String, AudioPlayer>();
  }

  void addAudio(String name, String path) {
    audios.put( name, new Minim(BREAKINGAME).loadFile(path) );
  }

  void addAudio(String name, String path, int bufferSize) {
    audios.put( name, new Minim(BREAKINGAME).loadFile(path, bufferSize) );
  }

  void setLoopPoints(String name, int a, int b) {
    try {
      audios.get(name).setLoopPoints(a, b);
    }
    catch(NullPointerException e) { 
      println("Could not find audio " + name + " for setting loop points!");
    }
  }

  void loop(String name) {
    try { 
      audios.get(name).loop();
    }
    catch(NullPointerException e) {
      println("Could not find audio " + name + " for looping!");
    }
  }

  void play(String name) {
    try { 
      AudioPlayer a = audios.get(name);
      a.pause();
      a.rewind();
      a.play();
    }
    catch(NullPointerException e) {
      println("Could not find audio " + name + " for playing!");
    }
  }

  void stop(String name) {
    try { 
      AudioPlayer a = audios.get(name);
      a.pause();
      a.rewind();
    }
    catch(NullPointerException e) {
      println("Could not find audio " + name + " for stopping!");
    }
  }

  void pause(String name) {
    try { 
      AudioPlayer a = audios.get(name);
      a.pause();
    }
    catch(NullPointerException e) {
      println("Could not find audio " + name + " for pausing!");
    }
  }

  void playNoRewind(String name) {
    try { 
      audios.get(name).play();
    }
    catch(NullPointerException e) {
      println("Could not find audio " + name + " for playing (no rewind)!");
    }
  }

  void pauseAll() {
    for (String name : audios.keySet()) {
      try {
        audios.get(name).pause();
      }
      catch(NullPointerException e) {
        println("Could not find audio " + name + " for pausing!");
      }
    }
  }

  void stopAll() {
    for (String name : audios.keySet()) {
      try {
        AudioPlayer a = audios.get(name);
        a.pause();
        a.rewind();
      }
      catch(NullPointerException e) {
        println("Could not find audio " + name + " for stopping!");
      }
    }
  }


  void resumeAll() {
    for (String name : audios.keySet()) {
      try {
        AudioPlayer a = audios.get(name);
        if (a.position() != 0) a.play();
      }
      catch(NullPointerException e) {
        println("Could not find audio " + name + " for resuming!");
      }
    }
  }

  AudioPlayer getAudioPlayer(String name) {
    return audios.get(name);
  }
}










/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
static class Helper {

  static NetworkEntity getEntityByID(ArrayList<NetworkEntity> nes, int id) {
    for (NetworkEntity ne : nes) if (ne.uniqueID == id) return ne;
    return null;
  }

  static int getEntityIndexByID(ArrayList<NetworkEntity> nes, int id) {
    int index = 0;
    for (NetworkEntity ne : nes) if (ne.uniqueID == id) return index; 
    else index++;
    return -1;
  }
  
  static DecompressResult getNetworkContainerFromByteArray(String messageBuffer, byte[] incomingBytes) {
    String incomingString = "";

    try {
      // We need to work with the message as string. To not corrupt the byte[] object data we have to use a "bijective" encoding (ISO-8859-1)!
      // http://stackoverflow.com/a/21032684
      // For the incoming string add the leftover from last time + the bytes converted to string
      incomingString = messageBuffer + new String(incomingBytes, "ISO-8859-1");
    } 
    catch(UnsupportedEncodingException e) {
      println("Fatal Error: Unsupported Encoding!");
    }

    ArrayList<String> newMessages = new ArrayList<String>(); 
    for (String message : incomingString.split(NET_SPLITSTRING)) newMessages.add(message);       
    int newMessageCount = newMessages.size();

    if (incomingString.endsWith(NET_SPLITSTRING)) {
      messageBuffer = "";
    } else {
      messageBuffer = newMessages.get(newMessageCount-1);
      newMessages.remove(newMessageCount-1);
      newMessageCount--;
    }

    while (newMessages.contains("")) newMessages.remove("");
    
    ArrayList<NetworkContainer> returnedContainers = new ArrayList<NetworkContainer>();
    
    for (String msg : newMessages) {
      byte[] bytes = null;
      try {
        bytes = msg.getBytes("ISO-8859-1");
      } 
      catch(UnsupportedEncodingException e) {
        println("Fatal Error: Unsupported Encoding!");
      }
      
      // TODO possible problem: will only do one message per call!
      // bottleneck?;
      
      NetworkContainer nc = NetworkContainer.decompress(bytes);
      if (nc != null) returnedContainers.add(nc);
    }
    
    return BREAKINGAME.new DecompressResult(returnedContainers, messageBuffer);
  }
}


class DecompressResult {
  ArrayList<NetworkContainer> networkContainers;
  String messageBuffer;
  
  DecompressResult(ArrayList<NetworkContainer> networkContainers, String messageBuffer) {
    this.networkContainers = networkContainers;
    this.messageBuffer = messageBuffer;
  }
  
  ArrayList<NetworkContainer> get_networkContainers() { return networkContainers; }
  String get_messageBuffer() { return messageBuffer; }
}






class SpriteManager {

  HashMap<String, PImage> sprites;
  HashMap<String, PImage[]> animations;

  SpriteManager() {
    sprites = new HashMap<String, PImage>();
    animations = new HashMap<String, PImage[]>();
  }

  void addSprite(String name, String path) {
    if (name.charAt(0) == 'S') {                    //STATIC SPRITE
      sprites.put(name, loadImage(path));
    } else {                                        //ANIMATED SPRITE

      char c = ' ';
      int i = 0;

      do {
        c = path.charAt(i);
        i++;
      } while (c != '.');

      int a = Character.getNumericValue(path.charAt(i - 2));
      
      PImage[] frames = new PImage[a*a];
      int W = loadImage(path).width/a;
      int H = loadImage(path).height/a;
      for (int j = 0; j < frames.length; j++) {
        int x = j%a*W;
        int y = j/a*H;
      frames[j] = loadImage(path).get(x, y, W, H);
      }
      animations.put(name, frames);
    }
  }

  void dispSprite(String name, int x, int y) {
    image(sprites.get(name), x, y);
  }
  void dispSprite(String name, int x, int y, float w, float h) {
    image(sprites.get(name), x, y, w, h);
  }
  
  void dispAnimation(String name, int x, int y, int speed, int frames){
    image(animations.get(name)[(millis() / speed)%frames], x, y);
  }
  
  void dispAnimation(String name, int x, int y, float w, float h, int speed, int frames){
    image(animations.get(name)[(millis() / speed)%frames], x, y, w, h);
  }
}



/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

int[] keysDown = new int[256];

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
  
  if(int(key) < 256) keysDown[int(key)] = 1;
}

void keyReleased() {
  if(int(key) < 256) keysDown[int(key)] = 0;
  if(key == 't') gameClient.disconnect();
}