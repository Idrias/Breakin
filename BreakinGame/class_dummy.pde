class Dummy extends GameObject {
  
  Dummy(float posX, float posY, float speedX, float speedY, int uid) {
    super(posX, posY, speedX, speedY, uid);
  }
  
  void update() {
    move();
    draw();
    lastUpdate = millis();
  }
  
  void move() {
    pos.x += speed.x;
    pos.y += speed.y;
  }
  
  void draw() {
    fill(0);
    ellipse(pos.x, pos.y, 10, 10);
  }
  
  void dummy() {println("DUMMY");}
}