import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.net.*; 
import java.io.*; 
import java.util.Map; 
import ddf.minim.*; 
import ddf.minim.analysis.*; 
import ddf.minim.effects.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class BreakinGame extends PApplet {

public void setup() {
  
  frameRate(144);
  noCursor();
  setup_gvars();
}

public void draw() {
  gameServer.update();
  gameClient.update();
  printFPS();
}
abstract class GameObject {
  // Each actor within the game inherits from the GameObject object. 
  NetworkEntity ne;
  int lastUpdate; 
  
  GameObject(NetworkEntity ne) {
    // Wenn der Client erstellt: NetworkEntity schon vorhanden!
    this.ne = ne;
    lastUpdate = millis();
  }

  GameObject(int actorType, int networkID) {
    // Wenn der Server erstellt: NetworkEntity muss generiert werden!
    this(new NetworkEntity(actorType, networkID));
  }

  public void simpleMove(int deltaT) {
    PVector pos = get_pos();
    PVector speed = get_speed();

    if (pos != null && speed != null) {
      pos.x += speed.x * deltaT;
      pos.y += speed.y * deltaT;

      if (pos.x > width || pos.x < 0) {
        set_speed( new PVector(speed.x * -1, speed.y) ); 
        set_pos(new PVector( width/2, height/2));
      }
      if (pos.y > height || pos.y < 0) {
        set_speed( new PVector(speed.x, speed.y*-1) ); 
        set_pos(new PVector( width/2, height/2));
      }
    }
  }

  public void set_speed(PVector speed) { 
    ne.set_speed(speed);
  }
  public void set_pos(PVector pos) { 
    ne.set_pos(pos);
  }
  public PVector get_speed() { 
    return ne.get_speed() == null ? new PVector(0, 0) : ne.get_speed();
  }
  public PVector get_pos() { 
    return ne.get_pos() == null ? new PVector(0, 0) : ne.get_pos();
  }

  public abstract void update();
  public abstract void draw();
}





class Dummy extends GameObject {

  Dummy(int networkID) {
    super(ACTORTYPE_DUMMY, networkID);
  }
  Dummy(NetworkEntity ne) {
    super(ne);
  }

  public void update() {
    int deltaT = millis() - lastUpdate;
    simpleMove(deltaT);
    lastUpdate = millis();
  }

  public void draw() {
    fill(0, 0, 255);
    PVector pos = ne.get_pos();
    ellipse(pos.x, pos.y, 20, 20);
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
class Player {
  Client client;
  int playerID;
  String name = "Antonio-Juan don Pepe";
  
  Player(Client client, int playerID) {
    this.client = client;
    this.playerID = playerID;
  }
  
  public Client get_client(){return client;}
  public int get_playerID(){return playerID;}
  public String get_name(){return name;}
  public void set_name(String name){this.name = name;}
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
class GameClient {
  NetClient netClient;
  MainMenu mainmenu;
  ArrayList<GameObject> gos;

  final int PHASE_MAINMENU = 3;
  final int PHASE_PREPAREMENU = 2;
  int gamePhase = PHASE_PREPAREMENU;

  GameClient() {
    netClient = new NetClient(BREAKINGAME);
    gos = new ArrayList<GameObject>();
  }

  public void update() {
    background(0);

    switch(gamePhase) {
      case PHASE_PREPAREMENU:
        mainmenu = new MainMenu();
        gamePhase = PHASE_MAINMENU;
        break;
  
      case PHASE_MAINMENU:
        mainmenu.draw();
        break;
    }

    fetch_nes();
    handle_gos();
    
    netClient.addToSendingList("Hi", new int[]{1, 2, 3, 4});
    netClient.pushSendingList();
  }

  public void handle_gos() {
    for (GameObject go : gos) {
      if (CLIENTSIDE_PREDICTIONS) go.update();
      go.draw();
    }
  }

  public void fetch_nes() {
    ArrayList<NetworkEntity> nes = null;
    
    ArrayList<NetworkContainer> containers = netClient.receive();
    int numberContainers = containers.size();
    
    if(numberContainers >= 1) {
      nes = containers.get(containers.size()-1).nes;
    }
    if (nes == null) return;

    for (NetworkEntity ne : nes) {
      int ne_id = ne.get_id();
      boolean found = false;

      for (GameObject go : gos) {
        if (ne_id == go.ne.get_id()) { 
          go.ne = ne; 
          found = true;
        }
      }

      if (!found) {
        switch(ne.get_type()) {
        case ACTORTYPE_DUMMY:
          gos.add(new Dummy(ne));
          break;
        }
      }
    }
  }
  
  
  public void disconnect() {
    netClient.stop();
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
class GameServer {
  NetServer netServer;
  ArrayList<GameObject> gameObjects;
  ArrayList<Player> players;
  
  int lastNetUpdate = 0;
  float netDeltaT;

  GameServer() {
    netServer = new NetServer(BREAKINGAME);
    gameObjects = new ArrayList<GameObject>();
    players = new ArrayList<Player>();
        
    netDeltaT = 1000 / NETWORK_UPDATERATE;
    while(gameObjects.size() < 1000) add_go(ACTORTYPE_DUMMY, new PVector(random(0, width), random(0, height)), new PVector(random(-0.5f, 0.5f), random(-0.5f, 0.5f)));
  }

  public void update() {
    
    ////////////////////////////////////////////////
    // Handle arriving and departing clients ///////
    for(Client c : newClients) {
      int clientID = netServer.generate_uniqueID();
      NetworkContainer infoContainer = new NetworkContainer();
      infoContainer.set_destination(c.ip(), clientID);
      netServer.pushNetworkContainer(infoContainer);
      netServer.addNewClient(c);
      players.add( new Player(c, clientID));
    }
    for(Client c : disconnectedClients) {
      netServer.removeDisconnectedClient(c);
      for(int i=0; i<players.size(); i++) {
        if(players.get(i).get_client() == c) {
          players.remove(i);
          break;
        }
      }
    }
    
    newClients.clear();
    disconnectedClients.clear();
    ////////////////////////////////////////////////
    
    
    ////////////////////////////////////////////////
    // Receive messages from clients
    for(NetworkContainer nc : netServer.receive()) {
      //println(nc.commands.toString());
    }
    ////////////////////////////////////////////////
    
    
    ////////////////////////////////////////////////
    // Update the gameObjects
    for(GameObject go : gameObjects) go.update();
    ////////////////////////////////////////////////
    
    
    ////////////////////////////////////////////////
    // Send update to clients
    if(millis() - lastNetUpdate  >= netDeltaT) {
      netServer.pushEntities( getNetworkEntities() ); 
      lastNetUpdate = millis();
    }
    ////////////////////////////////////////////////
  }

  public void add_go(int type, PVector pos, PVector speed) {
    switch(type) {
    case ACTORTYPE_DUMMY:
      Dummy d = new Dummy( netServer.generate_uniqueID() );
      d.set_pos(pos);
      d.set_speed(speed);
      gameObjects.add( d );
      break;
    }
  }

  public ArrayList<NetworkEntity> getNetworkEntities() {
    ArrayList<NetworkEntity> nes = new ArrayList<NetworkEntity>();
    for (GameObject go : gameObjects) nes.add(go.ne);
    return nes;
  }
}
static class NetworkEntity implements Serializable {
  int actorType;  // Which type of actor does this NE support?
  int uniqueID;   // The unique ID for networking purposes
  PVector pos;
  PVector speed;
  HashMap<String, Integer> values;

  NetworkEntity(int actorType, int uniqueID) {
    this.actorType = actorType;
    this.uniqueID = uniqueID;

    pos = new PVector();
    speed = new PVector();
    values = new HashMap<String, Integer>();
  }

  public void addKeyValuePair(String k, int v) {
    values.put(k, v);
  }

  public PVector get_pos() { 
    return pos;
  }
  public PVector get_speed() { 
    return speed;
  }
  public void set_pos(PVector pos) { 
    this.pos = pos;
  }
  public void set_speed(PVector speed) {
    this.speed = speed;
  }

  public int get_id() { 
    return uniqueID;
  }
  public int get_type() { 
    return actorType;
  }

  public byte[] compress() {
    try {
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
      ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);

      objectStream.writeObject(this);

      objectStream.close();
      byteStream.close();

      return byteStream.toByteArray();
    }

    catch(Exception e) {
      println("FATAL ERROR: compression failed!");
      return null;
    }
  }

  public static NetworkEntity decompress(byte[] byteArray) {
    try {
      ByteArrayInputStream byteStream = new ByteArrayInputStream(byteArray);
      ObjectInputStream objectStream = new ObjectInputStream(byteStream);

      NetworkEntity ne = (NetworkEntity) objectStream.readObject();

      objectStream.close();
      byteStream.close();

      return ne;
    }

    catch(Exception e) {
      println("FATAL ERROR: decompression failed!");
      e.printStackTrace();
      return null;
    }
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
static class NetworkContainer implements Serializable {
  ArrayList<NetworkEntity> nes = null;
  HashMap<String, int[]> commands = null;

  // Usually we want to send NetworkCotnainers to all clients.
  // If this is set only a specific client is the destination!
  String destinationIP = "";
  int destinationID = -1;

  NetworkContainer() {
    nes = new ArrayList<NetworkEntity>();
    commands = new HashMap<String, int[]>();
  }

  public void set_nes(ArrayList<NetworkEntity> nes) { 
    this.nes = nes;
  }
  public ArrayList<NetworkEntity> get_nes() {
    return nes;
  }

  public void set_commands(HashMap<String, int[]> commands) {
    this.commands = commands;
  }
  public HashMap<String, int[]> get_commands() {
    return commands;
  }
  public void set_destination(String ip, int id) {
    this.destinationIP = ip;
    this.destinationID = id;
  }
  public int get_destinationID() { 
    return destinationID;
  }
  public String get_destinationIP() { 
    return destinationIP;
  }



  public byte[] compress() {
    try {
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
      ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
      objectStream.writeObject(this);
      objectStream.close();
      byteStream.close();

      return byteStream.toByteArray();
    }
    catch(Exception e) {
      println("FATAL ERROR: compression failed!");
      return null;
    }
  }

  public static NetworkContainer decompress(byte[] byteArray) {
    try {
      ByteArrayInputStream byteStream = new ByteArrayInputStream(byteArray);
      ObjectInputStream objectStream = new ObjectInputStream(byteStream);

      NetworkContainer nc = (NetworkContainer) objectStream.readObject();

      objectStream.close();
      byteStream.close();

      return nc;
    }

    catch(StreamCorruptedException e) {
      println("ERROR: StreamCorruptedException during decompress!");
      return null;
    }

    catch(UTFDataFormatException e) {
      println("ERROR: UTFDataFormatException during decompress!");
      return null;
    }

    catch(InvalidClassException e) {
      println("ERROR: InvalidClassException during decompress");
      return null;
    }
    catch(Exception e) {
      println("FATAL ERROR: decompression failed!");
      e.printStackTrace();
      return null;
    }
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
class NetClient extends Client {
  String messageBuffer = "";
  HashMap<String, int[]> sendingList;
  int playerID = -1;

  //////// Constructors
  NetClient(String ip, int port, BreakinGame g) { 
    super(g, ip, port);
    sendingList = new HashMap<String, int[]>();
  }
  NetClient(String ip, BreakinGame g) { 
    this(ip, 4242, g);
  }
  NetClient(BreakinGame g) { 
    this("127.0.0.1", g);
  }
  //////// End of constructors

  public ArrayList<NetworkContainer> receive() {      
    if ( available() > 0 ) {
      DecompressResult dr = Helper.getNetworkContainerFromByteArray(messageBuffer, readBytes());
      messageBuffer = dr.get_messageBuffer();

      ArrayList<NetworkContainer> containers = dr.get_networkContainers(); // not null

      for (int i=0; i<containers.size(); i++) {
        NetworkContainer c = containers.get(i);
        String destinationIP = c.get_destinationIP();
        int destinationID = c.get_destinationID();
        
        // BEGIN IF DESTINATION CHECK
        if (destinationIP.isEmpty()) {
          // All is good. This container is intended for all clients!
        }      
        else {
          // Woah there! This container is for a specific client only! Let's check if it's for me!
          if (destinationIP.equals(this.ip())) {
            // Container is for my ip!
            if(playerID == -1) {
              // My playerID is not set yet. This must be the message to set it!
              playerID = destinationID;
              println("Client set playerID: " + playerID);
            }
            
            else if(playerID == destinationID) {
              // The container is for my ip and also for my id. Means this is for me!
              // TODO do stuff?
            }
            
            else {
              // This container is for my ip, but not for my id. Might be an error or there is more than one client on my machine!
              // I'm not the only one???
              // Hellooooooo?? Anybody theeere?!
              containers.remove(i);
              i--;
            }
            
          } else {
            // Nope! This container is not for me! Dump it!
            containers.remove(i);
            i--;
          }
        }
        ///// ENDIF
      }
      return containers;
    }
    return new ArrayList<NetworkContainer>();
  }

  public void addToSendingList(String command, int[] values) {
    sendingList.put(command, values);
  }

  public void pushSendingList() {
    if (!active()) return;
    NetworkContainer nc = new NetworkContainer();
    nc.set_commands(sendingList);
    write(nc.compress());
    write(NET_SPLITSTRING);
    sendingList = new HashMap<String, int[]>();
  }
  ////////////////////////////////////////////////////////
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
class NetServer extends Server {
  int uniqueID_count = 0;
  HashMap<Client, String> clientMessageBuffers;

  ////////////////////////////////////////////////
  // Constructors
  NetServer(int port, BreakinGame g) {
    super(g, port);
    clientMessageBuffers = new HashMap<Client, String>();
  }
  NetServer(BreakinGame g) { 
    this(4242, g);
  }
  // End of constructors
  ////////////////////////////////////////////////


  ////////////////////////////////////////////////
  // Send NetworkContainer to all clients!
  public void pushNetworkContainer(NetworkContainer nc) {
    byte[] bytes = nc.compress();
    write(bytes);
    write(NET_SPLITSTRING);
  }
  ////////////////////////////////////////////////


  ////////////////////////////////////////////////
  // Send networkEntitites as NetworkContainer to all clients!
  public void pushEntities(ArrayList<NetworkEntity> networkEntities) {
    NetworkContainer nc = new NetworkContainer();
    nc.set_nes(networkEntities);
    pushNetworkContainer(nc);
  }
  ////////////////////////////////////////////////


  ////////////////////////////////////////////////
  // Receive messages from clients
  public ArrayList<NetworkContainer> receive() {

    ArrayList<NetworkContainer> returnVals = new ArrayList<NetworkContainer>();
    Client sender;

    while (true) {
      sender = available();
      if (sender == null) break;

      String messageBuffer = clientMessageBuffers.get(sender);

      if (messageBuffer != null) {
        DecompressResult dr = Helper.getNetworkContainerFromByteArray(messageBuffer, sender.readBytes());
        clientMessageBuffers.put(sender, dr.get_messageBuffer());

        for (NetworkContainer nc : dr.get_networkContainers()) {
          returnVals.add(nc);
        }
      } else {
        // The client is not registered in clientMessageBuffers. Therefore we won't accept the message!
        sender.readString();
      }
    }

    return returnVals;
  }
  ////////////////////////////////////////////////


  ////////////////////////////////////////////////
  // Handle arriving / departing clients
  public void addNewClient(Client client) {
    clientMessageBuffers.put(client, "");
  }
  public void removeDisconnectedClient(Client client) {
    clientMessageBuffers.remove(client);
  }

  ////////////////////////////////////////////////
  // Get an unique network id for gameObjects
  public int generate_uniqueID() {
    uniqueID_count++;
    return uniqueID_count;
  }
  ////////////////////////////////////////////////
}


// SERVER EVENTS ///////
// New client connected event
public void serverEvent(Server server, Client client) {
  println("NEW CLIENT CONNECTED: HELLO " + client);
  newClients.add(client);
}

// Client left server event
public void disconnectEvent(Client client) {
  println("CLIENT LEFT: GOODBYE " + client);
  disconnectedClients.add(client);
}
////////////////////////







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

public void setup_gvars() {
  Arial = createFont("FFScala", 32);
  defaultButtonTexture = loadImage("/Assets/Graphics/Static_Sprites/Button.png");
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



public void load_audio() {
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

public void printFPS() {
  println("FPS: " + frameRate);
  println();
}




public void load_sprites() {
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

  public void addAudio(String name, String path) {
    audios.put( name, new Minim(BREAKINGAME).loadFile(path) );
  }

  public void addAudio(String name, String path, int bufferSize) {
    audios.put( name, new Minim(BREAKINGAME).loadFile(path, bufferSize) );
  }

  public void setLoopPoints(String name, int a, int b) {
    try {
      audios.get(name).setLoopPoints(a, b);
    }
    catch(NullPointerException e) { 
      println("Could not find audio " + name + " for setting loop points!");
    }
  }

  public void loop(String name) {
    try { 
      audios.get(name).loop();
    }
    catch(NullPointerException e) {
      println("Could not find audio " + name + " for looping!");
    }
  }

  public void play(String name) {
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

  public void stop(String name) {
    try { 
      AudioPlayer a = audios.get(name);
      a.pause();
      a.rewind();
    }
    catch(NullPointerException e) {
      println("Could not find audio " + name + " for stopping!");
    }
  }

  public void pause(String name) {
    try { 
      AudioPlayer a = audios.get(name);
      a.pause();
    }
    catch(NullPointerException e) {
      println("Could not find audio " + name + " for pausing!");
    }
  }

  public void playNoRewind(String name) {
    try { 
      audios.get(name).play();
    }
    catch(NullPointerException e) {
      println("Could not find audio " + name + " for playing (no rewind)!");
    }
  }

  public void pauseAll() {
    for (String name : audios.keySet()) {
      try {
        audios.get(name).pause();
      }
      catch(NullPointerException e) {
        println("Could not find audio " + name + " for pausing!");
      }
    }
  }

  public void stopAll() {
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


  public void resumeAll() {
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

  public AudioPlayer getAudioPlayer(String name) {
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

  public static NetworkEntity getEntityByID(ArrayList<NetworkEntity> nes, int id) {
    for (NetworkEntity ne : nes) if (ne.uniqueID == id) return ne;
    return null;
  }

  public static int getEntityIndexByID(ArrayList<NetworkEntity> nes, int id) {
    int index = 0;
    for (NetworkEntity ne : nes) if (ne.uniqueID == id) return index; 
    else index++;
    return -1;
  }
  
  public static DecompressResult getNetworkContainerFromByteArray(String messageBuffer, byte[] incomingBytes) {
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
  
  public ArrayList<NetworkContainer> get_networkContainers() { return networkContainers; }
  public String get_messageBuffer() { return messageBuffer; }
}






class SpriteManager {

  HashMap<String, PImage> sprites;
  HashMap<String, PImage[]> animations;

  SpriteManager() {
    sprites = new HashMap<String, PImage>();
    animations = new HashMap<String, PImage[]>();
  }

  public void addSprite(String name, String path) {
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

  public void dispSprite(String name, int x, int y) {
    image(sprites.get(name), x, y);
  }
  public void dispSprite(String name, int x, int y, float w, float h) {
    image(sprites.get(name), x, y, w, h);
  }
  
  public void dispAnimation(String name, int x, int y, int speed, int frames){
    image(animations.get(name)[(millis() / speed)%frames], x, y);
  }
  
  public void dispAnimation(String name, int x, int y, float w, float h, int speed, int frames){
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

public void keyPressed() {
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
  
  if(PApplet.parseInt(key) < 256) keysDown[PApplet.parseInt(key)] = 1;
}

public void keyReleased() {
  if(PApplet.parseInt(key) < 256) keysDown[PApplet.parseInt(key)] = 0;
  if(key == 't') gameClient.disconnect();
}
class Button {
  int posX, posY, sizeX, sizeY;
  int newMenu;  
  boolean SFX;  //true = nextSFX, false = backSFX
  String text;

  PImage img; // Texture

  int r = 0, g = 255, b = 0;
  int u = 0, v = 1;

  Button(int _posX, int _posY, int _sizeX, int _sizeY, int _newMenu, String _text, boolean _SFX, PImage _texture) {
    posX = _posX;
    posY = _posY;
    sizeX = _sizeX;
    sizeY = _sizeY;
    newMenu = _newMenu;
    text = _text;
    SFX = _SFX;
    img = _texture.copy();
  }

  public boolean isOver(int mx, int my) 
  {
    if (mx > posX - sizeX/2
      && mx < posX + sizeX/2
      && my > posY - sizeY/2
      && my < posY + sizeY/2) {
      return true;
    } else { 
      return false;
    }
  }

  public int disp() {
    textFont(defaultFont);
    textAlign(CENTER, CENTER);

    tint(r, g, b);
    beginShape();
    texture(img);
    vertex(posX - sizeX/2, posY - sizeY/2, 0, u);
    vertex(posX + sizeX/2, posY - sizeY/2, 1, u);
    vertex(posX + sizeX/2, posY + sizeY/2, 1, v);
    vertex(posX - sizeX/2, posY + sizeY/2, 0, v);
    vertex(posX - sizeX/2, posY - sizeY/2, 0, u);
    endShape();

    fill(r/2, 140, b/2);
    text(text, posX + 3 + 2 * u, posY + 3 - displayHeight/100 + 3 * u);  //Schatteneffekt vom Text
    fill(0, 40, 0);
    text(text, posX + 2 * u, posY - displayHeight/100 + 2 * u);




    if (isOver(mouseX, mouseY)) {
      r = 90;
      b = 90;

      audio.playNoRewind("SFX:Menu:Hover");

      if (!mousePressed&& u == 1) { //workaround um das fehlende mouseReleased zu ersetzen
        if (SFX) {
          audio.play("SFX:Menu:Next");
        } else {
          audio.play("SFX:Menu:Back");
        }
        return newMenu;
      }

      if (mousePressed)
      {
        r = 130;
        b = 130;
        u = 1; 
        v = 0;
      } else {
        u = 0;
        v = 1;
      }
    } else {
      if (r == 90) { //hoverPlayer soll nur vom gehoverten Button rewindet werden
        audio.stop("SFX:Menu:Hover");
      }

      r = 0;
      b = 0;
      u = 0;
      v = 1;
    }
    return -1;
  }
}


class InputBox {

  int posX, posY, sizeX, sizeY;
  String lastInput = new String();
  String currentInput = new String();
  char c;
  boolean canType, numOnly;

  InputBox(int _posX, int _posY, int _sizeX, int _sizeY, boolean _numOnly) {
    posX    = _posX;
    posY    = _posY;
    sizeX   = _sizeX;
    sizeY   = _sizeY;
    numOnly = _numOnly;
  }

  public void disp() {

    if (canType)fill(255);
    else fill(200);
    beginShape();
    vertex(posX - sizeX/2, posY - sizeY/2);
    vertex(posX + sizeX/2, posY - sizeY/2);
    vertex(posX + sizeX/2, posY + sizeY/2);
    vertex(posX - sizeX/2, posY + sizeY/2);
    vertex(posX - sizeX/2, posY - sizeY/2);
    endShape();

    textAlign(LEFT);
    //textFont(Arial);
    fill(30, 30, 30);
    text(currentInput, posX - sizeX / 2 + 10, posY + 10);


    if (canType) {
      if (!keyPressed)c = ' ';
      if (keyPressed && key != c) {

        //if(numOnly){
        //  if(key == ('1' || '2' || '3' || '4' || '5' || '6' || '7' || '8' || '9' || '0' || '.'))
        //}



        c = key;
        if (key == BACKSPACE && currentInput.length() > 0)
        {
          currentInput = currentInput.substring(0, currentInput.length() - 1);
        } else
        {
          currentInput = currentInput + key;
        }
      }
    }


    if (mousePressed) {
      if (isOver(mouseX, mouseY)) {
        canType = true;
      } else {
        canType = false;
      }
    }
  }



  public boolean isOver(int mx, int my) 
  {
    if (mx  > posX - sizeX/2
      && mx < posX + sizeX/2
      && my > posY - sizeY/2
      && my < posY + sizeY/2) {
      return true;
    } else { 
      return false;
    }
  }
}


class Window {

  int posX, posY, sizeX, sizeY;

  Window(int _posX, int _posY, int _sizeX, int _sizeY) {
    posX  = _posX;
    posY  = _posY;
    sizeX = _sizeX;
    sizeY = _sizeY;
  }

  public void disp() {
    stroke(120);
    fill(230, 140);
    rect(posX - sizeX / 2, posY - sizeY / 2, sizeX, sizeY, 12);
    stroke(0);
  }
}

class Label {
  int     posX, posY, size, sSize;
  PFont   font;
  int   iro, kage;
  String  text;
  boolean shadow, border;


  Label (int _posX, int _posY, int _size, PFont _font, int _iro, String _text) {
    posX = _posX;
    posY = _posY;
    size = _size;
    font = _font;
    iro  = _iro;
    text = _text;
  }

  Label (int _posX, int _posY, int _size, PFont _font, int _iro, String _text, boolean _shadow, int _sSize, boolean _border, int _kage) {
    posX   = _posX;
    posY   = _posY;
    size   = _size;
    font   = _font;
    iro    = _iro;
    text   = _text;
    shadow = _shadow;
    border = _border;
    kage   = _kage;
    sSize  = _sSize;
  }

  public void disp() {

    textFont(font, size);

    fill(kage, 255);

    if (border) {
      text(text, posX + 1, posY);
      text(text, posX - 1, posY);
      text(text, posX, posY + 1);
      text(text, posX, posY - 1);
    }

    if (shadow) {
      text(text, posX + sSize, posY + sSize);
    }
    
    fill(iro, 255);
    text(text, posX, posY);
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
class MainMenu {

  // Screen 1: Title screen
  // Screen 2: -> Singleplayer
  // Screen 3: -> Multiplayer
  // Screen 3 1: Create room
  // Screem 3 2: Join room
  // Screen 3 1 1: Host Lobby
  // Screen 3 2 1: Player Lobby
  // Screen 4: -> Settings
  // Screen 4 1: -> Screen4 -> Graphics
  // Screen 4 2: -> Screen4 -> Sound
  // Screen 4 3: -> Screen4 -> Language
  int screen = 1;
  Button[] s1Buttons, s2Buttons, s3Buttons, s31Buttons, s32Buttons, s4Buttons, s41Buttons;
  Label createRoom, joinRoom, enterIp, enterName, players;
  InputBox inputName, inputIP;
  Window s1Window, s41Window, s31Window;
  

  MainMenu() {
    Button single, multi, settings, credits, quit, startSP, difficulty, backTo1, backTo4, graphics, audioButton, language, trailQuali, bgQuali, animations, vSync, resolution, fullScreen, create, connect, start, join;
    //Create Buttons      Position X      Position Y                              Size X          Size Y  
    single =   new Button(displayWidth/2, displayHeight/2 +     displayHeight/12, displayWidth/3, displayHeight/15, 2, "SINGLEPLAYER", true, defaultButtonTexture);
    multi =    new Button(displayWidth/2, displayHeight/2 + 2 * displayHeight/12, displayWidth/3, displayHeight/15, 3, "MULTIPLAYER", true, defaultButtonTexture);
    settings = new Button(displayWidth/2, displayHeight/2 + 3 * displayHeight/12, displayWidth/3, displayHeight/15, 4, "SETTINGS", true, defaultButtonTexture);
    credits =  new Button(displayWidth/2, displayHeight/2 + 4 * displayHeight/12, displayWidth/3, displayHeight/15, 1, "CREDITS", true, defaultButtonTexture);
    quit =     new Button(displayWidth/2, displayHeight/2 + 5 * displayHeight/12, displayWidth/3, displayHeight/15, 6, "QUIT", false, defaultButtonTexture);

    startSP =  new Button(displayWidth/2, displayHeight/2 +     displayHeight/12, displayWidth/3, displayHeight/15, 2, "START GAME", true, defaultButtonTexture);
    difficulty=new Button(displayWidth/2, displayHeight/2 + 2 * displayHeight/12, displayWidth/3, displayHeight/15, 7, "DIFFICULTY: NORMAL", true, defaultButtonTexture);
    backTo1 =  new Button(displayWidth/2, displayHeight/2 + 5 * displayHeight/12, displayWidth/3, displayHeight/15, 1, "BACK", false, defaultButtonTexture);

    graphics = new Button(displayWidth/2, displayHeight/2 +     displayHeight/12, displayWidth/3, displayHeight/15, 41, "GRAPHICS", true, defaultButtonTexture);
    audioButton=new Button(displayWidth/2, displayHeight/2 + 2 * displayHeight/12, displayWidth/3, displayHeight/15, 4, "SOUND", true, defaultButtonTexture);
    language = new Button(displayWidth/2, displayHeight/2 + 3 * displayHeight/12, displayWidth/3, displayHeight/15, 4, "LANGUAGE", true, defaultButtonTexture);

    trailQuali=new Button(32*displayWidth/99, displayHeight/2 + displayHeight/12, displayWidth/3, displayHeight/15, 411, "TRAIL QUALITY: HIGH", true, defaultButtonTexture);
    bgQuali  = new Button(67*displayWidth/99, displayHeight/2 + displayHeight/12, displayWidth/3, displayHeight/15, 41, "BACKGROUND QUALITY: HIGH", true, defaultButtonTexture);
    animations=new Button(32*displayWidth/99, displayHeight/2+2*displayHeight/12, displayWidth/3, displayHeight/15, 41, "ANIMATIONS: ON", true, defaultButtonTexture);
    vSync    = new Button(67*displayWidth/99, displayHeight/2+2*displayHeight/12, displayWidth/3, displayHeight/15, 41, "VSYNC: ON", true, defaultButtonTexture);
    resolution=new Button(32*displayWidth/99, displayHeight/2+3*displayHeight/12, displayWidth/3, displayHeight/15, 41, "RESOLUTION: " + displayWidth + "x" + displayHeight, true, defaultButtonTexture);
    fullScreen=new Button(67*displayWidth/99, displayHeight/2+3*displayHeight/12, displayWidth/3, displayHeight/15, 41, "FULLSCREEN", true, defaultButtonTexture);
    backTo4 =  new Button(   displayWidth/2, displayHeight/2+5*displayHeight/12, displayWidth/3, displayHeight/15, 4, "BACK", false, defaultButtonTexture);

    create =   new Button(displayWidth/2, displayHeight/2 +     displayHeight/12, displayWidth/3, displayHeight/15, 31, "CREATE ROOM", true, defaultButtonTexture);
    connect =  new Button(displayWidth/2, displayHeight/2 + 2 * displayHeight/12, displayWidth/3, displayHeight/15, 32, "JOIN ROOM", true, defaultButtonTexture);
    start =    new Button(displayWidth/2, displayHeight/2 + 2 * displayHeight/12, displayWidth/3, displayHeight/15, 311, "START GAME", true, defaultButtonTexture);
    join =     new Button(displayWidth/2, displayHeight/2 + 2 * displayHeight/12, displayWidth/3, displayHeight/15, 321, "CONNECT", true, defaultButtonTexture);

    s1Buttons = new Button[]{single, multi, settings, credits, quit};
    s2Buttons = new Button[]{startSP, difficulty, backTo1};
    s3Buttons = new Button[]{create, connect, backTo1};
    s31Buttons= new Button[]{start, backTo1};
    s32Buttons= new Button[]{join, backTo1};
    s4Buttons = new Button[]{graphics, audioButton, language, backTo1};

    s41Buttons= new Button[]{trailQuali, bgQuali, animations, vSync, resolution, fullScreen, backTo4};


    fft = new FFT(audio.getAudioPlayer("Music:MainMenu").bufferSize(), audio.getAudioPlayer("Music:MainMenu").sampleRate());

    audio.stopAll();
    audio.loop("Music:MainMenu");
    
    
    inputName = new InputBox(displayWidth/2 - 17 * displayWidth/48 + displayWidth/10, displayHeight/2 - 2 * displayHeight/48 + displayHeight/40, displayWidth/5, displayHeight/30, false);
    inputIP   = new InputBox(displayWidth/2 - 17 * displayWidth/48 + displayWidth/10, displayHeight/2 + 2 * displayHeight/48 + displayHeight/40, displayWidth/5, displayHeight/30, false);

    s1Window  = new Window(displayWidth/2, displayHeight/2 + 3 * displayHeight/12 + displayHeight/100, 9 * displayWidth / 24, 11 * displayHeight / 24);
    s41Window = new Window(displayWidth/2, displayHeight/2 + 3 * displayHeight/12 + displayHeight/100, 35 * displayWidth / 48, 11 * displayHeight / 24);
    s31Window = new Window(displayWidth/2, displayHeight/2 + 2 * displayHeight/12 + displayHeight/100, 35 * displayWidth / 48, 15 * displayHeight / 24);
    
    
    createRoom = new Label(displayWidth/2 - 17 * displayWidth/48, displayHeight/2 - 4 * displayHeight/48, 50, defaultFont, color (255), "CREATE ROOM", true, 3, false, color(0));
    enterIp =    new Label(displayWidth/2 - 17 * displayWidth/48, displayHeight/2 + 2 * displayHeight/48, 44, Arial, color (255), "IP Adress:", false, 0, true, color(0));
    enterName =  new Label(displayWidth/2 - 17 * displayWidth/48, displayHeight/2 - 2 * displayHeight/48, 44, Arial, color (255), "Playername:", false, 0, true, color(0));
    players =    new Label(displayWidth/2 - 17 * displayWidth/48, displayHeight/2 + 10 * displayHeight/48, 44, Arial, color (255), "Players:", false, 0, true, color(0));
    //enterIp = new Label(displayWidth/2, displayHeight/2, 44, Arial, color (255), "IP Adress:", false, 0, true, color(0));
    
    //createLabels = new Label[]{enterIp};

  }


  public void draw() {
    background(255);

    drawWaves();
    tint(255);
    sprite.dispSprite("Static:BreakinLogo", displayWidth/2, 3 * displayHeight/12, displayWidth/1.5f, displayWidth/5);



    Button[] renderButtons = new Button[]{};

    switch(screen) {
    case 1:
      s1Window.disp();
      renderButtons = s1Buttons;
      break;
    case 2:
      s1Window.disp();
      renderButtons = s2Buttons;
      break;
    case 3:
      s1Window.disp();
      renderButtons = s3Buttons;
      break;
    case 31:
      s31Window.disp();
      inputName.disp();
      inputIP.disp();
      
      createRoom.disp();
      enterName.disp();
      enterIp.disp();
      players.disp();
      
      renderButtons = s31Buttons;
      break;
    case 32:
      s31Window.disp();
      inputName.disp();
      inputIP.disp();
      renderButtons = s32Buttons;
      break;
    case 4:
      s1Window.disp();
      renderButtons = s4Buttons;
      break;
    case 41:
      s41Window.disp();
      renderButtons = s41Buttons;
      break;
    case 441:
      renderButtons = s41Buttons;
      //s41Buttons[0].text = "TRAIL QUALITY: " + graphicSettings
      break;
    case 6: 
      exit();
    }
    
    //enterIp.disp();
        
    //Sample Animation
    //sprite.dispAnimation("Anim:DestructedWallBrick", 150, 100, 100, 50, 45, 9); //PosX, PosY, Width, Height, Animation Speed, Animation Frames

    for (Button b : renderButtons) {
      int i = b.disp();
      if (i != -1) screen = i;
    }
    
    pushMatrix();
    translate(mouseX + 32, mouseY + 36); //Ursprung wird an die Ecke unten Rechts vom Taco cursor gesetzt
    if (mousePressed) {
      rotate(-7*TWO_PI/360);
    }
    tint(255);
    sprite.dispSprite("Static:Tacursor", -16, -18, 32, 35);
    popMatrix();  

}


  ///////////////////////////////////////////////////////////////////////////////////////
  int bufferSize = 512;
  int fftSize = floor(bufferSize*.5f)+1;
  float ai = TWO_PI/fftSize;
  float bgRotation = 180;
  FFT fft;

  public void drawWaves() {
    noStroke();
    pushMatrix();
    translate(displayWidth/2, displayHeight/2 + displayHeight/12);
    rotate(bgRotation * TWO_PI/360);
    bgRotation += 0.2f;
    colorMode(HSB, fftSize, 100, 100);

    //fft.forward(audio.getAudioPlayer("Music:MainMenu").right);

    //background(0, 0, 100);
    fft.forward(audio.getAudioPlayer("Music:Trump").right);

    for (int i = 0; i < fftSize; i++) {
      float band = fft.getBand(i);
      fill(i, 150+100*(band/10), 100, 160);
      arc(0, 0, 300+band * 3*(i+1), 300+band * 3*(i+1), ai*i, ai*(i+1));
    }
    popMatrix();
    colorMode(RGB, 255);
    stroke(0);
  }
  ///////////////////////////////////////////////////////////////////////////////////////////////////
}
/*
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

*/
  public void settings() {  fullScreen(P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "BreakinGame" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
