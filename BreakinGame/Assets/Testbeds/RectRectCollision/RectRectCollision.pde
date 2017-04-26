PVector centerA;
PVector centerB;
PVector sizeA;
PVector sizeB;

void setup() {
size(800, 800);
  centerA = new PVector();
  centerB = new PVector(width/2, height/2);
  sizeA = new PVector(150, 150);
  sizeB = new PVector(75, 130);
  rectMode(CENTER);
}


void draw() {
  background(0);
  if(R_R_collision()) background(100);
  
  fill(255, 0, 0);
  rect(centerA.x, centerA.y, sizeA.x, sizeA.y);
  
  fill(0, 255, 0);
  rect(centerB.x, centerB.y, sizeB.x, sizeB.y);

  if(mousePressed) {
    if(mouseButton == LEFT) centerA.set(mouseX, mouseY);
    else if(mouseButton == RIGHT) centerB.set(mouseX, mouseY);
  }
}


boolean R_R_collision() { 
    boolean c1 = centerA.x + sizeA.x / 2 > centerB.x - sizeB.x / 2; 
    boolean c2 = centerA.x - sizeA.x / 2 < centerB.x + sizeB.x / 2;
    boolean c3 = centerA.y + sizeA.y / 2 > centerB.y - sizeB.y / 2;
    boolean c4 = centerA.y - sizeA.y / 2 < centerB.y + sizeB.y / 2;
    
    if(c1 && c2 && c3 && c4) return true;
    return false;
}