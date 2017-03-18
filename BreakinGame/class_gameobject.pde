
// Each actor within the game inherits from the GameObject object. 

abstract class GameObject {
  NetworkEntity ne;
  int lastUpdate; 

  GameObject(NetworkEntity ne) {
    // Wenn der Client erstellt: NetworkEntity schon vorhanden!
    this.ne = ne;
    lastUpdate = millis();
  }

  GameObject(int actorType, int networkID) {
    // Wenn der Client erstellt: NetworkEntity muss generiert werden!
    this(new NetworkEntity(actorType, networkID));
  }

  void simpleMove(int deltaT) {
    PVector pos = get_pos();
    PVector speed = get_speed();

    if (pos != null && speed != null) {
      pos.x += speed.x * deltaT;
      pos.y += speed.y * deltaT;

      if (pos.x > width || pos.x < 0) {
        set_speed( new PVector(speed.x * -1, speed.y) ); 
        set_pos(new PVector( width/2, height/2));
      }
      if (pos.y > height || pos.y < 0) {
        set_speed( new PVector(speed.x, speed.y*-1) ); 
        set_pos(new PVector( width/2, height/2));
      }
    }
  }

  void set_speed(PVector speed) { 
    ne.set_speed(speed);
  }
  void set_pos(PVector pos) { 
    ne.set_pos(pos);
  }
  PVector get_speed() { 
    return ne.get_speed() == null ? new PVector(0, 0) : ne.get_speed();
  }
  PVector get_pos() { 
    return ne.get_pos() == null ? new PVector(0, 0) : ne.get_pos();
  }

  abstract void update();
  abstract void draw();
}



class Dummy extends GameObject {

  Dummy(int networkID) {
    super(ACTORTYPE_DUMMY, networkID);
  }
  Dummy(NetworkEntity ne) {
    super(ne);
  }

  void update() {
    int deltaT = millis() - lastUpdate;
    simpleMove(deltaT);
    lastUpdate = millis();
  }

  void draw() {
    fill(0, 0, 255);
    PVector pos = ne.get_pos();
    ellipse(pos.x, pos.y, 20, 20);
  }
}