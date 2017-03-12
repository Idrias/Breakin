class GameServer extends Server {
  
  ArrayList<GameObject> gos;
  
  GameServer(int port) {
    super(GAME, port);
    gos = new ArrayList<GameObject>();
  }
  
  GameServer() {
    this(4242);
  }
  
  void update() {
    for(GameObject go : gos) {
      go.update();
    }
    
    write("ye;Hello;World!;b;end");
  }
  
  void addObject(float posX, float posY, float speedX, float speedY) {
    gos.add( new Dummy(posX, posY, speedX, speedY, CREATE_SERVERSIDE) );
  }
}