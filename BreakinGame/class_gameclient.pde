class GameClient extends Client {
  
  ArrayList<GameObject> gos;
  String messageBuffer = "";
  
  GameClient(String ip, int port) { super(GAME, ip, port); gos = new ArrayList<GameObject>();}
  GameClient(String ip) { this(ip, 4242); }
  GameClient() { this("127.0.0.1", 4242); }
  
  void checkMail() {
    while ( available() > 0 ) {
      
      String[] newMessages = (messageBuffer + readString()).split(";");  
      int newMessageCount = newMessages.length;
      
      if(newMessageCount>0) {
        // New messages arrived!
        newMessages[0] = messageBuffer + newMessages[0];  // Add the buffered rest of last time we received a message
      }
      
      for(int i = 0; i < newMessageCount; i++) {
        println(newMessages[i]);
      }
     println();  
   }
  }
  
  
  void draw() {
    for(GameObject go : gos) {
      go.draw();
    }
  }
}