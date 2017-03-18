class NetServer extends Server {
  int uniqueID_count = 0;

  // Constructors
  NetServer(int port, BreakinGame g) {
    super(g, port);
  }
  NetServer(BreakinGame g) { 
    this(4242, g);
  }
  // End of constructors


  void splitMSG() { 
    write(NET_SPLITSTRING);
  }

  void pushEntities(ArrayList<NetworkEntity> networkEntities) {
    //println("Server pushing " + networkEntities.size() + " entities. ");

    NetworkContainer nc = new NetworkContainer();
    nc.set_nes(networkEntities);


    byte[] c = nc.compress();
    write(c);
    splitMSG();
  }

  int generate_uniqueID() {
    uniqueID_count++;
    return uniqueID_count;
  }
}