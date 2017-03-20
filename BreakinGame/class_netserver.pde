class NetServer extends Server {
  int uniqueID_count = 0;
  int clientID_count = 0;
  HashMap<String, String> clientMessageBuffers;

  // Constructors
  NetServer(int port, BreakinGame g) {
    super(g, port);
    clientMessageBuffers = new HashMap<String, String>();
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

  int generate_clientID() {
    clientID_count++;
    return clientID_count;
  }


  ArrayList<NetworkContainer> receive() {

    ArrayList<NetworkContainer> returnVals = new ArrayList<NetworkContainer>();

    Client sender = available();
    
    // cant do a while loop here cause it will break the client class...
    // TODO take whole client as id
    
    if (sender != null) {

      if (millis()%1000 > 990) println(sender, frameCount);


        String messageBuffer = clientMessageBuffers.get(sender.ip());
        if (messageBuffer != null) {
          DecompressResult dr = Helper.getNetworkContainerFromByteArray(messageBuffer, sender.readBytes());
          clientMessageBuffers.put(sender.ip(), dr.get_messageBuffer());
          returnVals.add(dr.get_networkContainer());
        } else {
        }; // the messageBuffer has not been initialized, which means that the ip+clientid is not known!

      sender = available();
    }
    return returnVals;
  }


  int handleNewClient(Client client) {
    int id = generate_clientID();
    return id;
  }
}

// SERVER EVENT //
void serverEvent(Server server, Client client) {

  // TODO could be unsafe 
  //if(gameServer == null) return;
  gameServer.handleNewClient(client);
}