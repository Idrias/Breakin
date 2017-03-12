
// Each actor within the game inherits from the GameObject object. 

abstract class GameObject {
  PVector pos;    // Position of the GameObject
  PVector speed;  // Speed of the GameObject
  int uid;        // Unique ID for networking purposes
  int lastUpdate; // in millis since game start: when was this actor updated?
  
  boolean isServerSide = false;  // is this a sever side GameObject?
  GameObject(String s) {}
  GameObject(PVector pos, PVector speed, int uid) {
    this.pos = pos;
    this.speed = speed;
    
    lastUpdate = millis();
    
    if(uid == CREATE_SERVERSIDE) {
      // This is a server side GameObject, therefore no ID exists yet.
      // We need to create it!
      this.uid = uidcount;
      uidcount++;
      isServerSide = true;  // This GameObject is now server side!
    }
    
    else {
      // This GameObject is client side! Therefore we already have an ID from the Server!
      this.uid = uid;
    }
    
  }
  
  GameObject(float posX, float posY, float speedX, float speedY, int uid) {
    this( new PVector(posX, posY), new PVector(speedX, speedY), uid);
  }
  
  GameObject() {
    this (width/2, height/2, 0, 0, 0);
  }
  
  abstract void update(); 
  abstract void move();
  abstract void draw();
  
  abstract void toNetworkString()
  
  void set_isServerSide(boolean b) { isServerSide = b; }

  String toString() {
    return "TODO";
  }
}