class GameClient {
  NetClient netClient;
  MainMenu mainmenu;
  ArrayList<GameObject> gos;

  final int PHASE_MAINMENU = 3;
  final int PHASE_PREPAREMENU = 2;
  int gamePhase = PHASE_PREPAREMENU;

  GameClient() {
    netClient = new NetClient(BREAKINGAME);
    gos = new ArrayList<GameObject>();
  }

  void update() {
    background(0);

    switch(gamePhase) {
      case PHASE_PREPAREMENU:
        mainmenu = new MainMenu();
        gamePhase = PHASE_MAINMENU;
        break;
  
      case PHASE_MAINMENU:
        mainmenu.draw();
        break;
    }

    //fetch_nes();
    //handle_gos();
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