class NetServer extends Server {
  
  ArrayList<NetworkEntity> networkEntities;
  int uniqueID_count;
  
  // Constructors
  NetServer(int port, BreakinGame g) {
    super(g, port);
    networkEntities = new ArrayList<NetworkEntity>();
  }
  NetServer(BreakinGame g) { this(4242, g); }
  // End of constructors
  

  void splitMSG() { write(NET_SPLITSTRING); }

  void pushEntities() {
    println("Server pushing " + networkEntities.size() + " entities. ");
    for(NetworkEntity ne : networkEntities) {
      write(ne.compress());
      splitMSG();
    }
    splitMSG();
  }
  
  int addEntity(int actorType) {
    networkEntities.add( new NetworkEntity( actorType, uniqueID_count) );
    uniqueID_count++;
    return uniqueID_count-1;
  }
  
}