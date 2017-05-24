package graphics;

import other.G;

public class Trail {
	
	int outerTrailLength, coreTrailLength;
	float outerTrailWeight, coreTrailWeight;
	int outerTrailColor;
	int[] trailX, trailY;
	
	public Trail(int _outerTrailColor){
		
		  outerTrailLength = 150;
		  coreTrailLength = 70;
		  outerTrailWeight = G.p.width/50;
		  coreTrailWeight = G.p.height/100;
		  outerTrailColor = _outerTrailColor;
		  //-----------------------------------------------------------------------------------------------------
		  
		  trailX = new int[outerTrailLength];
		  trailY = new int[outerTrailLength];
		
	}
	
	
	public void disp(int x, int y){
		
		
		
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
		  G.p.stroke(30, 255, 30);    //G.p.stroke(outerTrailColor);         
		  for (int i = 0; i < outerTrailLength - 1; i++) {
		    G.p.line(trailX[i], trailY[i], trailX[i + 1], trailY[i + 1]);
		    G.p.strokeWeight(outerTrailWeight - (outerTrailWeight / outerTrailLength) * (i + 1));
		  }

		  //DRAW CORE TRAIL
		  G.p.stroke(255);
		  for (int i = 0; i < coreTrailLength; i++) {
		    G.p.line(trailX[i], trailY[i], trailX[i + 1], trailY[i + 1]);
		    G.p.strokeWeight(coreTrailWeight - (coreTrailWeight / coreTrailLength) * (i + 1));
		  }
		
	}
	
	
}
