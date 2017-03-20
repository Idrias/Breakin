class GameServer {
  NetServer netServer;
  ArrayList<GameObject> gameObjects;
  int lastNetUpdate = 0;
  float netDeltaT;

  GameServer() {
    netServer = new NetServer(BREAKINGAME);
    gameObjects = new ArrayList<GameObject>();
    netDeltaT = 1000 / NETWORK_UPDATERATE;
    while(gameObjects.size() < 700) add_go(ACTORTYPE_DUMMY, new PVector(random(0, width), random(0, height)), new PVector(random(-0.5, 0.5), random(-0.5, 0.5)));
  }

  void update() {      
    for(GameObject go : gameObjects) go.update();
    
    if(millis() - lastNetUpdate  >= netDeltaT) {
      netServer.pushEntities( getNetworkEntities() ); 
      lastNetUpdate = millis();
    }
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