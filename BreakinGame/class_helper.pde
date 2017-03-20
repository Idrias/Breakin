static class Helper {

  static NetworkEntity getEntityByID(ArrayList<NetworkEntity> nes, int id) {
    for (NetworkEntity ne : nes) if (ne.uniqueID == id) return ne;
    return null;
  }

  static int getEntityIndexByID(ArrayList<NetworkEntity> nes, int id) {
    int index = 0;
    for (NetworkEntity ne : nes) if (ne.uniqueID == id) return index; 
    else index++;
    return -1;
  }
  
  static DecompressResult getNetworkContainerFromByteArray(String messageBuffer, byte[] incomingBytes) {
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
      
      // TODO possible problem: will only do one message per call!
      // bottleneck?;
      
      NetworkContainer nc = NetworkContainer.decompress(bytes);
      if (nc != null) return BREAKINGAME.new DecompressResult(nc, messageBuffer);
    }
    
    return BREAKINGAME.new DecompressResult(new NetworkContainer(), messageBuffer);
  }
}


class DecompressResult {
  NetworkContainer networkContainer;
  String messageBuffer;
  
  DecompressResult(NetworkContainer networkContainer, String messageBuffer) {
    this.networkContainer = networkContainer;
    this.messageBuffer = messageBuffer;
  }
  
  NetworkContainer get_networkContainer() { return networkContainer; }
  String get_messageBuffer() { return messageBuffer; }
}