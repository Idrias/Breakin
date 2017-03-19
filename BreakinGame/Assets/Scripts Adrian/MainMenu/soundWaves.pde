import ddf.minim.signals.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;

FFT fft;

int bufferSize = 1024;
int fftSize = floor(bufferSize*.5f)+1;
float ai = TWO_PI/fftSize;
float bgRotation = 180;

void drawWaves() {
  pushMatrix();
  translate(displayWidth/2, displayHeight/2 + displayHeight/12);
  rotate(bgRotation * TWO_PI/360);
  bgRotation += 0.2;
  colorMode(HSB, fftSize, 100, 100);
  background(0, 0, 100);
  fft.forward(bgmPlayer.right);
  for (int i = 0; i < fftSize; i++) {
    float band = fft.getBand(i);
    fill(i, 150+100*(band/10), 100, 100);
    arc(0, 0, 300+band * 10*(i+1), 300+band * 10*(i+1), ai*i, ai*(i+1));
  }
  popMatrix();
}