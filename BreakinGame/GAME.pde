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
    // Wenn der Client erstellt: NetworkEntity muss generiert werden!
    this(new NetworkEntity(actorType, networkID));
  }

  void simpleMove(int deltaT) {
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

  void set_speed(PVector speed) { 
    ne.set_speed(speed);
  }
  void set_pos(PVector pos) { 
    ne.set_pos(pos);
  }
  PVector get_speed() { 
    return ne.get_speed() == null ? new PVector(0, 0) : ne.get_speed();
  }
  PVector get_pos() { 
    return ne.get_pos() == null ? new PVector(0, 0) : ne.get_pos();
  }

  abstract void update();
  abstract void draw();
}



class Dummy extends GameObject {

  Dummy(int networkID) {
    super(ACTORTYPE_DUMMY, networkID);
  }
  Dummy(NetworkEntity ne) {
    super(ne);
  }

  void update() {
    int deltaT = millis() - lastUpdate;
    simpleMove(deltaT);
    lastUpdate = millis();
  }

  void draw() {
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

  void update() {
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

  void handle_gos() {
    for (GameObject go : gos) {
      if (CLIENTSIDE_PREDICTIONS) go.update();
      go.draw();
    }
  }

  void fetch_nes() {
    ArrayList<NetworkEntity> nes = netClient.receive().nes;
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
  int lastNetUpdate = 0;
  float netDeltaT;

  GameServer() {
    netServer = new NetServer(BREAKINGAME);
    gameObjects = new ArrayList<GameObject>();
    netDeltaT = 1000 / NETWORK_UPDATERATE;
    while(gameObjects.size() < 50) add_go(ACTORTYPE_DUMMY, new PVector(random(0, width), random(0, height)), new PVector(random(-0.5, 0.5), random(-0.5, 0.5)));
  }

  void update() {
    
    ////////////////////////////////////////////////
    // Handle arriving and departing clients ///////
    for(Client c : newClients) {
      netServer.addNewClient(c);
    }
    for(Client c : disconnectedClients) {
      netServer.removeDisconnectedClient(c);
    }
    ////////////////////////////////////////////////
    
    
    ////////////////////////////////////////////////
    // Receive messages from clients
    if(frameCount % 10 != 0) return;
    println("Frame " + frameCount + ":");
    for(NetworkContainer nc : netServer.receive()) {
      println(nc.commands.toString());
    }
    println();
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

  void add_go(int type, PVector pos, PVector speed) {
    switch(type) {
    case ACTORTYPE_DUMMY:
      Dummy d = new Dummy( netServer.generate_uniqueID() );
      d.set_pos(pos);
      d.set_speed(speed);
      gameObjects.add( d );
      break;
    }
  }

  ArrayList<NetworkEntity> getNetworkEntities() {
    ArrayList<NetworkEntity> nes = new ArrayList<NetworkEntity>();
    for (GameObject go : gameObjects) nes.add(go.ne);
    return nes;
  }
}