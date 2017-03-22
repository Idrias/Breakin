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
  Label createRoom, joinRoom, enterIp, enterName, players;
  InputBox inputName, inputIP;
  Window s1Window, s41Window, s31Window;
  

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
    backTo4 =  new Button(   displayWidth/2, displayHeight/2+5*displayHeight/12, displayWidth/3, displayHeight/15, 4, "BACK", false, defaultButtonTexture);

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
    
    
    inputName = new InputBox(displayWidth/2 - 17 * displayWidth/48 + displayWidth/10, displayHeight/2 - 2 * displayHeight/48 + displayHeight/40, displayWidth/5, displayHeight/30, false);
    inputIP   = new InputBox(displayWidth/2 - 17 * displayWidth/48 + displayWidth/10, displayHeight/2 + 2 * displayHeight/48 + displayHeight/40, displayWidth/5, displayHeight/30, false);

    s1Window  = new Window(displayWidth/2, displayHeight/2 + 3 * displayHeight/12 + displayHeight/100, 9 * displayWidth / 24, 11 * displayHeight / 24);
    s41Window = new Window(displayWidth/2, displayHeight/2 + 3 * displayHeight/12 + displayHeight/100, 35 * displayWidth / 48, 11 * displayHeight / 24);
    s31Window = new Window(displayWidth/2, displayHeight/2 + 2 * displayHeight/12 + displayHeight/100, 35 * displayWidth / 48, 15 * displayHeight / 24);
    
    
    createRoom = new Label(displayWidth/2 - 17 * displayWidth/48, displayHeight/2 - 4 * displayHeight/48, 50, defaultFont, color (255), "CREATE ROOM", true, 3, false, color(0));
    enterIp =    new Label(displayWidth/2 - 17 * displayWidth/48, displayHeight/2 + 2 * displayHeight/48, 44, Arial, color (255), "IP Adress:", false, 0, true, color(0));
    enterName =  new Label(displayWidth/2 - 17 * displayWidth/48, displayHeight/2 - 2 * displayHeight/48, 44, Arial, color (255), "Playername:", false, 0, true, color(0));
    players =    new Label(displayWidth/2 - 17 * displayWidth/48, displayHeight/2 + 10 * displayHeight/48, 44, Arial, color (255), "Players:", false, 0, true, color(0));
    //enterIp = new Label(displayWidth/2, displayHeight/2, 44, Arial, color (255), "IP Adress:", false, 0, true, color(0));
    
    //createLabels = new Label[]{enterIp};

  }


  void draw() {
    background(255);

    drawWaves();
    tint(255);
    sprite.dispSprite("Static:BreakinLogo", displayWidth/2, 3 * displayHeight/12, displayWidth/1.5, displayWidth/5);



    Button[] renderButtons = new Button[]{};

    switch(screen) {
    case 1:
      s1Window.disp();
      renderButtons = s1Buttons;
      break;
    case 2:
      s1Window.disp();
      renderButtons = s2Buttons;
      break;
    case 3:
      s1Window.disp();
      renderButtons = s3Buttons;
      break;
    case 31:
      s31Window.disp();
      inputName.disp();
      inputIP.disp();
      
      createRoom.disp();
      enterName.disp();
      enterIp.disp();
      players.disp();
      
      renderButtons = s31Buttons;
      break;
    case 32:
      s31Window.disp();
      inputName.disp();
      inputIP.disp();
      renderButtons = s32Buttons;
      break;
    case 4:
      s1Window.disp();
      renderButtons = s4Buttons;
      break;
    case 41:
      s41Window.disp();
      renderButtons = s41Buttons;
      break;
    case 441:
      renderButtons = s41Buttons;
      //s41Buttons[0].text = "TRAIL QUALITY: " + graphicSettings
      break;
    case 6: 
      exit();
    }
    
    //enterIp.disp();
        
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

    //background(0, 0, 100);
    fft.forward(audio.getAudioPlayer("Music:Trump").right);

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