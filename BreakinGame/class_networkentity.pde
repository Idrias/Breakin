static class NetworkEntity implements Serializable {
  int actorType;  // Which type of actor does this NE support?
  int uniqueID;   // The unique ID for networking purposes
  PVector pos;
  PVector speed;
  HashMap<String, Integer> values;
  
  NetworkEntity(int actorType, int uniqueID) {
    this.actorType = actorType;
    this.uniqueID = uniqueID;
  }
  
  void addKeyValuePair(String k, int v) {
    values.put(k, v);
  }
  
  PVector get_pos() { return pos; }
  PVector get_speed() { return speed; }
  int get_id() { return uniqueID; }
  int get_type() { return actorType; }
  
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