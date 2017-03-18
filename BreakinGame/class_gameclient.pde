class GameClient {
  NetClient netClient;
  ArrayList<GameObject> gos;
  
  GameClient() {
    netClient = new NetClient(BREAKINGAME);
    gos = new ArrayList<GameObject>();
  }
  
  void update() {
    netClient.receive();
    fetch_nes();
    handle_gos();
  }
  
  void handle_gos() {
    for(GameObject go : gos) {
      go.update();
      go.draw();
    }    
  }
  
  void fetch_nes() {
    for(NetworkEntity ne : netClient.get_entities()) {
      int ne_id = ne.get_id();
      boolean found = false;
      
      for(GameObject go : gos) {
        if(ne_id == go.ne.get_id()) { go.ne = ne; found = true;}
      }
  
      if(!found) {
        switch(ne.get_type()) {
          case ACTORTYPE_DUMMY:
            gos.add(new Dummy(ne));
            break;
        }
      }
    }
  }
  
  
  
}