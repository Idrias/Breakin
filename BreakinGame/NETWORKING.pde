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

  void addKeyValuePair(String k, int v) {
    values.put(k, v);
  }

  PVector get_pos() { 
    return pos;
  }
  PVector get_speed() { 
    return speed;
  }
  void set_pos(PVector pos) { 
    this.pos = pos;
  }
  void set_speed(PVector speed) {
    this.speed = speed;
  }

  int get_id() { 
    return uniqueID;
  }
  int get_type() { 
    return actorType;
  }

  byte[] compress() {
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

  static NetworkEntity decompress(byte[] byteArray) {
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
  
  NetworkContainer() {
    nes = new ArrayList<NetworkEntity>();
    commands = new HashMap<String, int[]>();
  }
  
  void set_nes(ArrayList<NetworkEntity> nes) { 
    this.nes = nes;
  }
  ArrayList<NetworkEntity> get_nes() {
    return nes;
  }

  void set_commands(HashMap<String, int[]> commands) {
    this.commands = commands;
  }
  HashMap<String, int[]> get_commands() {
    return commands;
  }

  byte[] compress() {
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

  static NetworkContainer decompress(byte[] byteArray) {
    try {
      ByteArrayInputStream byteStream = new ByteArrayInputStream(byteArray);
      ObjectInputStream objectStream = new ObjectInputStream(byteStream);

      NetworkContainer nc = (NetworkContainer) objectStream.readObject();

      objectStream.close();
      byteStream.close();

      return nc;
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

  NetworkContainer receive() {      
    if ( available() > 0 ) {
      DecompressResult dr = Helper.getNetworkContainerFromByteArray(messageBuffer,readBytes());
      messageBuffer = dr.get_messageBuffer();
      return dr.get_networkContainer();
    }
    return new NetworkContainer();
  }
  
  void addToSendingList(String command, int[] values) {
    sendingList.put(command, values);
  }
  
  void pushSendingList() {
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