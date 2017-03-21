class MainMenu {

  // Screen 1: Title screen
  // Screen 2: -> Singleplayer
  // Screen 3: -> Multiplayer
  // Screen 3 1: Create room
  // Screem 3 2: Join room
  // Screen 3 1 1: Host Lobby
  // Screen 3 2 1: Player Lobby
  // Screen 4: -> Settings
  // Screen 4 1: -> Screen4 -> Graphics
  // Screen 4 2: -> Screen4 -> Sound
  // Screen 4 3: -> Screen4 -> Language
  int screen = 1;
  Button[] s1Buttons, s2Buttons, s3Buttons, s31Buttons, s32Buttons, s4Buttons, s41Buttons;
  InputBox inputName, inputIP;

  MainMenu() {
    Button single, multi, settings, credits, quit, startSP, difficulty, backTo1, backTo4, graphics, audioButton, language, trailQuali, bgQuali, animations, vSync, resolution, fullScreen, create, connect, start, join;
    //Create Buttons      Position X      Position Y                              Size X          Size Y  
    single =   new Button(displayWidth/2, displayHeight/2 +     displayHeight/12, displayWidth/3, displayHeight/15, 2, "SINGLEPLAYER", true, defaultButtonTexture);
    multi =    new Button(displayWidth/2, displayHeight/2 + 2 * displayHeight/12, displayWidth/3, displayHeight/15, 3, "MULTIPLAYER", true, defaultButtonTexture);
    settings = new Button(displayWidth/2, displayHeight/2 + 3 * displayHeight/12, displayWidth/3, displayHeight/15, 4, "SETTINGS", true, defaultButtonTexture);
    credits =  new Button(displayWidth/2, displayHeight/2 + 4 * displayHeight/12, displayWidth/3, displayHeight/15, 1, "CREDITS", true, defaultButtonTexture);
    quit =     new Button(displayWidth/2, displayHeight/2 + 5 * displayHeight/12, displayWidth/3, displayHeight/15, 6, "QUIT", false, defaultButtonTexture);

    startSP =  new Button(displayWidth/2, displayHeight/2 +     displayHeight/12, displayWidth/3, displayHeight/15, 2, "START GAME", true, defaultButtonTexture);
    difficulty=new Button(displayWidth/2, displayHeight/2 + 2 * displayHeight/12, displayWidth/3, displayHeight/15, 7, "DIFFICULTY: NORMAL", true, defaultButtonTexture);
    backTo1 =  new Button(displayWidth/2, displayHeight/2 + 5 * displayHeight/12, displayWidth/3, displayHeight/15, 1, "BACK", false, defaultButtonTexture);

    graphics = new Button(displayWidth/2, displayHeight/2 +     displayHeight/12, displayWidth/3, displayHeight/15, 41, "GRAPHICS", true, defaultButtonTexture);
    audioButton=new Button(displayWidth/2, displayHeight/2 + 2 * displayHeight/12, displayWidth/3, displayHeight/15, 4, "SOUND", true, defaultButtonTexture);
    language = new Button(displayWidth/2, displayHeight/2 + 3 * displayHeight/12, displayWidth/3, displayHeight/15, 4, "LANGUAGE", true, defaultButtonTexture);

    trailQuali=new Button(32*displayWidth/99, displayHeight/2 + displayHeight/12, displayWidth/3, displayHeight/15, 411, "TRAIL QUALITY: HIGH", true, defaultButtonTexture);
    bgQuali  = new Button(67*displayWidth/99, displayHeight/2 + displayHeight/12, displayWidth/3, displayHeight/15, 41, "BACKGROUND QUALITY: HIGH", true, defaultButtonTexture);
    animations=new Button(32*displayWidth/99, displayHeight/2+2*displayHeight/12, displayWidth/3, displayHeight/15, 41, "ANIMATIONS: ON", true, defaultButtonTexture);
    vSync    = new Button(67*displayWidth/99, displayHeight/2+2*displayHeight/12, displayWidth/3, displayHeight/15, 41, "VSYNC: ON", true, defaultButtonTexture);
    resolution=new Button(32*displayWidth/99, displayHeight/2+3*displayHeight/12, displayWidth/3, displayHeight/15, 41, "RESOLUTION: " + displayWidth + "x" + displayHeight, true, defaultButtonTexture);
    fullScreen=new Button(67*displayWidth/99, displayHeight/2+3*displayHeight/12, displayWidth/3, displayHeight/15, 41, "FULLSCREEN", true, defaultButtonTexture);
    backTo4 =  new Button(displayWidth/2, displayHeight/2+5*displayHeight/12, displayWidth/3, displayHeight/15, 4, "BACK", false, defaultButtonTexture);

    create =   new Button(displayWidth/2, displayHeight/2 +     displayHeight/12, displayWidth/3, displayHeight/15, 31, "CREATE ROOM", true, defaultButtonTexture);
    connect =  new Button(displayWidth/2, displayHeight/2 + 2 * displayHeight/12, displayWidth/3, displayHeight/15, 32, "JOIN ROOM", true, defaultButtonTexture);
    start =    new Button(displayWidth/2, displayHeight/2 + 2 * displayHeight/12, displayWidth/3, displayHeight/15, 311, "START GAME", true, defaultButtonTexture);
    join =     new Button(displayWidth/2, displayHeight/2 + 2 * displayHeight/12, displayWidth/3, displayHeight/15, 321, "CONNECT", true, defaultButtonTexture);

    s1Buttons = new Button[]{single, multi, settings, credits, quit};
    s2Buttons = new Button[]{startSP, difficulty, backTo1};
    s3Buttons = new Button[]{create, connect, backTo1};
    s31Buttons= new Button[]{start, backTo1};
    s32Buttons= new Button[]{join, backTo1};
    s4Buttons = new Button[]{graphics, audioButton, language, backTo1};
    s41Buttons= new Button[]{trailQuali, bgQuali, animations, vSync, resolution, fullScreen, backTo4};


    fft = new FFT(audio.getAudioPlayer("Music:MainMenu").bufferSize(), audio.getAudioPlayer("Music:MainMenu").sampleRate());

    audio.stopAll();
    audio.loop("Music:MainMenu");

    inputName = new InputBox(displayWidth/2, displayHeight/2 + 3 * displayHeight/12, 300, 40, false);
    inputIP   = new InputBox(displayWidth/2, displayHeight/2 + 4 * displayHeight/12, 300, 40, true);
  }


  void draw() {
    background(255);

    drawWaves();
    tint(255);
    sprite.dispSprite("Static:BreakinLogo", displayWidth/2, 3 * displayHeight/12, displayWidth/1.5, displayWidth/5);



    Button[] renderButtons = new Button[]{};

    switch(screen) {
    case 1:
      renderButtons = s1Buttons;
      break;
    case 2:
      renderButtons = s2Buttons;
      break;
    case 3:
      renderButtons = s3Buttons;
      break;
    case 31:
      inputName.disp();
      inputIP.disp();
      renderButtons = s31Buttons;
      break;
    case 32:
      inputName.disp();
      inputIP.disp();
      renderButtons = s32Buttons;
      break;
    case 4:
      renderButtons = s4Buttons;
      break;
    case 41:
      renderButtons = s41Buttons;
      break;
    case 441:
      renderButtons = s41Buttons;
      //s41Buttons[0].text = "TRAIL QUALITY: " + graphicSettings
      break;
    case 6: 
      exit();
    }
    //Sample Animation
    //sprite.dispAnimation("Anim:DestructedWallBrick", 150, 100, 100, 50, 45, 9); //PosX, PosY, Width, Height, Animation Speed, Animation Frames

    for (Button b : renderButtons) {
      int i = b.disp();
      if (i != -1) screen = i;
    }

    translate(mouseX + 32, mouseY + 36); //Ursprung wird an die Ecke unten Rechts vom Taco cursor gesetzt
    if (mousePressed) {
      rotate(-7*TWO_PI/360);
    }
    tint(255);
    sprite.dispSprite("Static:Tacursor", -16, -18, 32, 35);
  }


  ///////////////////////////////////////////////////////////////////////////////////////
  int bufferSize = 512;
  int fftSize = floor(bufferSize*.5f)+1;
  float ai = TWO_PI/fftSize;
  float bgRotation = 180;
  FFT fft;

  void drawWaves() {
    noStroke();
    pushMatrix();
    translate(displayWidth/2, displayHeight/2 + displayHeight/12);
    rotate(bgRotation * TWO_PI/360);
    bgRotation += 0.2;
    colorMode(HSB, fftSize, 100, 100);
    fft.forward(audio.getAudioPlayer("Music:MainMenu").right);
    for (int i = 0; i < fftSize; i++) {
      float band = fft.getBand(i);
      fill(i, 150+100*(band/10), 100, 160);
      arc(0, 0, 300+band * 3*(i+1), 300+band * 3*(i+1), ai*i, ai*(i+1));
    }
    popMatrix();
    colorMode(RGB, 255);
    stroke(0);
  }
  ///////////////////////////////////////////////////////////////////////////////////////////////////
}