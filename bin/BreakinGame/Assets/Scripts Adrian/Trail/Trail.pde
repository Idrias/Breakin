//Created: March 16th 2017 

int outerTrailLength, coreTrailLength;
float outerTrailWeight, coreTrailWeight;
color outerTrailColor;
int[] trailX, trailY;

void setup() { 
  fullScreen(P2D);
  noCursor();

  //SET TRAIL PARAMETERS
  //IMPORTANT: OUTER TRAIL HAS TO BE BIGGER THAN CORE TRAIL
  //CARE, NOT EVERY MONITOR IS 16:9
  //-----------------------------------------------------------------------------------------------------
  outerTrailLength = 20;
  coreTrailLength = 6;
  outerTrailWeight = displayWidth/50;
  coreTrailWeight = displayWidth/100;
  outerTrailColor = color(255, 30, 255);
  //-----------------------------------------------------------------------------------------------------

  trailX = new int[outerTrailLength];
  trailY = new int[outerTrailLength];
} 

void draw() { 

  background(0); 

  //UPDATE TRAIL VERTEXES
  for (int i = 0; i < outerTrailLength; i++) {
    if (i == outerTrailLength-1) {
      trailX[0] = mouseX;              //INSERT X-COORDINATES OF SOMBRERO HERE
      trailY[0] = mouseY;              //INSERT Y-COORDINATES OF SOMBRERO HERE
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