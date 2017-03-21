class Button {
  int posX, posY, sizeX, sizeY;
  int newMenu;  
  boolean SFX;  //true = nextSFX, false = backSFX
  String text;
  
  PImage img; // Texture
  
  int r = 0, g = 255, b = 0;
  int u = 0, v = 1;

  Button(int _posX, int _posY, int _sizeX, int _sizeY, int _newMenu, String _text, boolean _SFX, PImage _texture) {
    posX = _posX;
    posY = _posY;
    sizeX = _sizeX;
    sizeY = _sizeY;
    newMenu = _newMenu;
    text = _text;
    SFX = _SFX;
    img = _texture.copy();
  }

  boolean isOver(int mx, int my) 
  {
    if (mx > posX - sizeX/2
      && mx < posX + sizeX/2
      && my > posY - sizeY/2
      && my < posY + sizeY/2) {
      return true;
    } else { 
      return false;
    }
  }

  int disp() {
    tint(r, g, b);
    beginShape();
    texture(img);
    vertex(posX - sizeX/2, posY - sizeY/2, 0, u);
    vertex(posX + sizeX/2, posY - sizeY/2, 1, u);
    vertex(posX + sizeX/2, posY + sizeY/2, 1, v);
    vertex(posX - sizeX/2, posY + sizeY/2, 0, v);
    vertex(posX - sizeX/2, posY - sizeY/2, 0, u);
    endShape();

    fill(r/2, 140, b/2);
    text(text, posX + 3 + 2 * u, posY + 3 - displayHeight/100 + 3 * u);  //Schatteneffekt vom Text
    fill(0, 40, 0);
    text(text, posX + 2 * u, posY - displayHeight/100 + 2 * u);




    if (isOver(mouseX, mouseY)) {
      r = 90;
      b = 90;

      audio.playNoRewind("SFX:Menu:Hover");

      if (!mousePressed&& u == 1) { //workaround um das fehlende mouseReleased zu ersetzen
        if (SFX) {
          audio.play("SFX:Menu:Next");
        } else {
          audio.play("SFX:Menu:Back");
        }
        return newMenu;
      }

      if (mousePressed)
      {
        r = 130;
        b = 130;
        u = 1; 
        v = 0;
      } else {
        u = 0;
        v = 1;
      }
    } else {
      if (r == 90) { //hoverPlayer soll nur vom gehoverten Button rewindet werden
        audio.stop("SFX:Menu:Hover");
      }

      r = 0;
      b = 0;
      u = 0;
      v = 1;
    }
    return -1;
  }
}








/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class MainMenu {

  // Screen 1: Title screen
  // Screen 2: -> Singleplayer
  // Screen 3: -> Multiplayer
  // Screen 4: -> Settings
  // Screen 4 1: -> Screen4 -> Graphics
  // Screen 4 2: -> Screen4 -> Sound
  // Screen 4 3: -> Screen4 -> Language
  int screen = 1;
  Button[] s1Buttons, s2Buttons, s4Buttons;

  MainMenu() {
    Button single, multi, settings, credits, quit, startSP, difficulty, backTo1, graphics, audioButton, language;
    //Create Buttons      Position X      Position Y                              Size X          Size Y  
    single =   new Button(displayWidth/2, displayHeight/2 +     displayHeight/12, displayWidth/3, displayHeight/15, 2, "SINGLEPLAYER", true, defaultButtonTexture);
    multi =    new Button(displayWidth/2, displayHeight/2 + 2 * displayHeight/12, displayWidth/3, displayHeight/15, 1, "MULTIPLAYER", true, defaultButtonTexture);
    settings = new Button(displayWidth/2, displayHeight/2 + 3 * displayHeight/12, displayWidth/3, displayHeight/15, 4, "SETTINGS", true, defaultButtonTexture);
    credits =  new Button(displayWidth/2, displayHeight/2 + 4 * displayHeight/12, displayWidth/3, displayHeight/15, 1, "CREDITS", true, defaultButtonTexture);
    quit =     new Button(displayWidth/2, displayHeight/2 + 5 * displayHeight/12, displayWidth/3, displayHeight/15, 6, "QUIT", false, defaultButtonTexture);

    startSP =  new Button(displayWidth/2, displayHeight/2 +     displayHeight/12, displayWidth/3, displayHeight/15, 2, "START GAME", true, defaultButtonTexture);
    difficulty=new Button(displayWidth/2, displayHeight/2 + 2 * displayHeight/12, displayWidth/3, displayHeight/15, 7, "DIFFICULTY: NORMAL", true, defaultButtonTexture);
    backTo1 =  new Button(displayWidth/2, displayHeight/2 + 5 * displayHeight/12, displayWidth/3, displayHeight/15, 1, "BACK", false, defaultButtonTexture);

    graphics = new Button(displayWidth/2, displayHeight/2 +     displayHeight/12, displayWidth/3, displayHeight/15, 4, "GRAPHICS", true, defaultButtonTexture);
    audioButton = new Button(displayWidth/2, displayHeight/2 + 2 * displayHeight/12, displayWidth/3, displayHeight/15, 4, "SOUND", true, defaultButtonTexture);
    language = new Button(displayWidth/2, displayHeight/2 + 3 * displayHeight/12, displayWidth/3, displayHeight/15, 4, "LANGUAGE", true, defaultButtonTexture);

    s1Buttons = new Button[]{single, multi, settings, credits, quit};
    s2Buttons = new Button[]{startSP, difficulty, backTo1};
    s4Buttons = new Button[]{graphics, audioButton, language, backTo1};
    
    fft = new FFT(audio.getAudioPlayer("Music:Trump").bufferSize(), audio.getAudioPlayer("Music:Trump").sampleRate());
    
    audio.stopAll();
    audio.loop("Music:Trump");
  }


  void draw() {
    background(0);

    drawWaves();

    Button[] renderButtons = new Button[]{};

    switch(screen) {
    case 1:
      renderButtons = s1Buttons;
      break;
    case 2:
      renderButtons = s2Buttons;
      break;
    case 4:
      renderButtons = s4Buttons;
      break;

    case 6: 
      exit();
    }

    for (Button b : renderButtons) {
      int i = b.disp();
      if (i != -1) screen = i;
    }
  }
  
  
  ///////////////////////////////////////////////////////////////////////////////////////
  int bufferSize = 1024;
  int fftSize = floor(bufferSize*.5f)+1;
  float ai = TWO_PI/fftSize;
  float bgRotation = 180;
  FFT fft;

  void drawWaves() {
    pushMatrix();
    translate(displayWidth/2, displayHeight/2 + displayHeight/12);
    rotate(bgRotation * TWO_PI/360);
    bgRotation += 0.2;
    colorMode(HSB, fftSize, 100, 100);
    //background(0, 0, 100);
    fft.forward(audio.getAudioPlayer("Music:Trump").right);
    for (int i = 0; i < fftSize; i++) {
      float band = fft.getBand(i);
      fill(i, 150+100*(band/10), 100, 100);
      arc(0, 0, 300+band * 10*(i+1), 300+band * 10*(i+1), ai*i, ai* 1.05 * (i+1));
    }
    popMatrix();
    colorMode(RGB, 255);
  }
  ///////////////////////////////////////////////////////////////////////////////////////////////////
}