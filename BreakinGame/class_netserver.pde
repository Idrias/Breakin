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
  // Send networkEntitites as NetworkContainer to all clients!
  void pushEntities(ArrayList<NetworkEntity> networkEntities) {
    NetworkContainer nc = new NetworkContainer();
    nc.set_nes(networkEntities);
    byte[] bytes = nc.compress();
    write(bytes);
    write(NET_SPLITSTRING);
  }
  ////////////////////////////////////////////////


  ////////////////////////////////////////////////
  // Receive messages from clients
  ArrayList<NetworkContainer> receive() {

    ArrayList<NetworkContainer> returnVals = new ArrayList<NetworkContainer>();
    Client sender;

    while (true) {
      sender = available();
      if (sender == null) break;

      String messageBuffer = clientMessageBuffers.get(sender);

      if (messageBuffer != null) {

        DecompressResult dr = Helper.getNetworkContainerFromByteArray(messageBuffer, sender.readBytes());
        clientMessageBuffers.put(sender, dr.get_messageBuffer());

        returnVals.add(dr.get_networkContainer());
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
  void addNewClient(Client client) {
    clientMessageBuffers.put(client, "");
  }
  void removeDisconnectedClient(Client client) {
    clientMessageBuffers.remove(client);
  }

  ////////////////////////////////////////////////
  // Get an unique network id for gameObjects
  int generate_uniqueID() {
    uniqueID_count++;
    return uniqueID_count;
  }
  ////////////////////////////////////////////////
}



// SERVER EVENTS ///////

// New client connected event
void serverEvent(Server server, Client client) {
  println("NEW CLIENT CONNECTED: HELLO " + client);
  newClients.add(client);
}

// Client left server event
void disconnectEvent(Client client) {
  println("CLIENT LEFT: GOODBYE " + client);
  disconnectedClients.add(client);
}
////////////////////////