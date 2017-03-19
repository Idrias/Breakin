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