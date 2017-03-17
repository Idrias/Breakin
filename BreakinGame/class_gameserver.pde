class GameServer {
  NetServer netServer;
  
  GameServer() {
    netServer = new NetServer(BREAKINGAME);
  }
  
  void update() {
    netServer.addEntity( ACTORTYPE_DUMMY );
    for(NetworkEntity ne : netServer.networkEntities) ne.pos = new PVector(random(0,width), random(0,height));
    netServer.pushEntities();
  }
  
}