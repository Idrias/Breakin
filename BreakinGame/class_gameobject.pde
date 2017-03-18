
// Each actor within the game inherits from the GameObject object. 

abstract class GameObject {
  NetworkEntity ne;
  int lastUpdate; 
  
  GameObject(NetworkEntity ne) {
    this.ne = ne;
    lastUpdate = millis();
  }
  
  abstract void update();
  abstract void draw();
}



class Dummy extends GameObject {
  
  Dummy(NetworkEntity ne) {
    super(ne);
  }
 
  void update() { 
  }
  
  void draw() {
    fill(0, 0, 255);
    PVector pos = ne.get_pos();
    ellipse(pos.x, pos.y, 20, 20);
  }
}