class GameClient {
  NetClient netClient;
  ArrayList<GameObject> gos;

  GameClient() {
    netClient = new NetClient(BREAKINGAME);
    gos = new ArrayList<GameObject>();
  }

  void update() {
    background(0);
    fetch_nes();
    handle_gos();
  }

  void handle_gos() {
    for (GameObject go : gos) {
      if (CLIENTSIDE_PREDICTIONS) go.update();
      go.draw();
    }
  }

  void fetch_nes() {
    ArrayList<NetworkEntity> nes = netClient.receive();
    if (nes == null) return;

    for (NetworkEntity ne : nes) {
      int ne_id = ne.get_id();
      boolean found = false;

      for (GameObject go : gos) {
        if (ne_id == go.ne.get_id()) { 
          go.ne = ne; 
          found = true;
        }
      }

      if (!found) {
        switch(ne.get_type()) {
        case ACTORTYPE_DUMMY:
          gos.add(new Dummy(ne));
          break;
        }
      }
    }
  }
}