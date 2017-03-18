class GameServer {
  NetServer netServer;
  
  GameServer() {
    netServer = new NetServer(BREAKINGAME);
  }
  
  void update() {
    
    if(netServer.networkEntities.size() < 1000) netServer.addEntity( ACTORTYPE_DUMMY );
    //else exit();
    
    for(NetworkEntity ne : netServer.networkEntities) ne.pos = new PVector(random(0, width), random(0,height));
    netServer.pushEntities();
  }
}