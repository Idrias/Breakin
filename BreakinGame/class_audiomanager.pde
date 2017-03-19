import ddf.minim.*;

class AudioManager {
  HashMap<String, AudioPlayer> audios;


  AudioManager() {
    audios = new HashMap<String, AudioPlayer>();
  }

  void addAudio(String name, String path) {
    audios.put( name, new Minim(BREAKINGAME).loadFile(path) );
  }

  void addAudio(String name, String path, int bufferSize) {
    audios.put( name, new Minim(BREAKINGAME).loadFile(path, bufferSize) );
  }

  void setLoopPoints(String name, int a, int b) {
    try {
      audios.get(name).setLoopPoints(a, b);
    }
    catch(NullPointerException e) { 
      println("Could not find audio " + name + " for setting loop points!");
    }
  }

  void loop(String name) {
    try { 
      audios.get(name).loop();
    }
    catch(NullPointerException e) {
      println("Could not find audio " + name + " for looping!");
    }
  }
  
  void play(String name) {
    try { 
      audios.get(name).play();
    }
    catch(NullPointerException e) {
      println("Could not find audio " + name + " for playing!");
    }
  }
}