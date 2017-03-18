class NetClient extends Client {
  String messageBuffer = "";

  //////// Constructors
  NetClient(String ip, int port, BreakinGame g) { 
    super(g, ip, port);
  }
  NetClient(String ip, BreakinGame g) { 
    this(ip, 4242, g);
  }
  NetClient(BreakinGame g) { 
    this("127.0.0.1", g);
  }
  //////// End of constructors

  ArrayList<NetworkEntity> receive() {      
    if ( available() > 0 ) {
      byte[] incomingBytes = readBytes(); 
      String incomingString = "";

      try {
        // We need to work with the message as string. To not corrupt the byte[] object data we have to use a "bijective" encoding (ISO-8859-1)!
        // http://stackoverflow.com/a/21032684
        // For the incoming string add the leftover from last time + the bytes converted to string
        incomingString = messageBuffer + new String(incomingBytes, "ISO-8859-1");
      } 
      catch(UnsupportedEncodingException e) {
        println("Fatal Error: Unsupported Encoding!");
      }

      ArrayList<String> newMessages = new ArrayList<String>(); 
      for (String message : incomingString.split(NET_SPLITSTRING)) newMessages.add(message);       
      int newMessageCount = newMessages.size();

      if (incomingString.endsWith(NET_SPLITSTRING)) {
        messageBuffer = "";
      } else {
        messageBuffer = newMessages.get(newMessageCount-1);
        newMessages.remove(newMessageCount-1);
        newMessageCount--;
      }

      while (newMessages.contains("")) newMessages.remove("");

      for (String msg : newMessages) {
        byte[] bytes = null;
        try {
          bytes = msg.getBytes("ISO-8859-1");
        } 
        catch(UnsupportedEncodingException e) {
          println("Fatal Error: Unsupported Encoding!");
        }

        NetworkContainer nc = NetworkContainer.decompress(bytes);
        if (nc != null) return nc.nes;
      }
    }
    return null;
  }


  ////////////////////////////////////////////////////////
}