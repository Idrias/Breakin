int outerTrailLength, coreTrailLength;
float outerTrailWeight, coreTrailWeight;
color outerTrailColor;
int[] trailX, trailY;

void setupTrail() {   //DIESE FUNKTION IST ZUR ZEIT UNBENUTZT
  //-----------------------------------------------------------------------------------------------------
  outerTrailLength = 15;
  coreTrailLength = 5;
  outerTrailWeight = displayWidth/100;
  coreTrailWeight = displayWidth/200;
  outerTrailColor = color(255, 30, 30);
  //-----------------------------------------------------------------------------------------------------

  trailX = new int[outerTrailLength];
  trailY = new int[outerTrailLength];
}
void drawTrail(int x, int y){ //int outerTrailLength, int coreTrailLength, float outerTrailWeight, float coreTrailWeight, PVector outerTrailColor, int trailX[], int trailY[]
  //UPDATE TRAIL VERTEXES
  for (int i = 0; i < outerTrailLength; i++) {
    if (i == outerTrailLength-1) {
      trailX[0] = x;              //INSERT X-COORDINATES OF SOMBRERO HERE
      trailY[0] = y;              //INSERT Y-COORDINATES OF SOMBRERO HERE
    } else {
      trailX[outerTrailLength-1-i] = trailX[outerTrailLength-2-i];
      trailY[outerTrailLength-1-i] = trailY[outerTrailLength-2-i];
    }
  }
  
  //DRAW OUTER TRAIL
  stroke(outerTrailColor);             
  for (int i = 0; i < outerTrailLength - 1; i++) {
    line(trailX[i], trailY[i], trailX[i + 1], trailY[i + 1]);
    strokeWeight(outerTrailWeight - (outerTrailWeight / outerTrailLength) * (i + 1));
  }

  //DRAW CORE TRAIL
  stroke(255);
  for (int i = 0; i < coreTrailLength; i++) {
    line(trailX[i], trailY[i], trailX[i + 1], trailY[i + 1]);
    strokeWeight(coreTrailWeight - (coreTrailWeight / coreTrailLength) * (i + 1));
  }
}