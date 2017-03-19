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
    
    fft = new FFT(audio.getAudioPlayer("Music:MainMenu").bufferSize(), audio.getAudioPlayer("Music:MainMenu").sampleRate());
    
    audio.stopAll();
    audio.loop("Music:MainMenu");
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
    fft.forward(audio.getAudioPlayer("Music:MainMenu").right);
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