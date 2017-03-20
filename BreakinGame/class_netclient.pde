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