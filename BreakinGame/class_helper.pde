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
}