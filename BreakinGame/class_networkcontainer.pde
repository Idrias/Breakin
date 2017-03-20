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