import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;

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
      AudioPlayer a = audios.get(name);
      a.pause();
      a.rewind();
      a.play();
    }
    catch(NullPointerException e) {
      println("Could not find audio " + name + " for playing!");
    }
  }

  void stop(String name) {
    try { 
      AudioPlayer a = audios.get(name);
      a.pause();
      a.rewind();
    }
    catch(NullPointerException e) {
      println("Could not find audio " + name + " for stopping!");
    }
  }

  void pause(String name) {
    try { 
      AudioPlayer a = audios.get(name);
      a.pause();
    }
    catch(NullPointerException e) {
      println("Could not find audio " + name + " for pausing!");
    }
  }

  void playNoRewind(String name) {
    try { 
      audios.get(name).play();
    }
    catch(NullPointerException e) {
      println("Could not find audio " + name + " for playing (no rewind)!");
    }
  }

  void pauseAll() {
    for (String name : audios.keySet()) {
      try {
        audios.get(name).pause();
      }
      catch(NullPointerException e) {
        println("Could not find audio " + name + " for pausing!");
      }
    }
  }

  void stopAll() {
    for (String name : audios.keySet()) {
      try {
        AudioPlayer a = audios.get(name);
        a.pause();
        a.rewind();
      }
      catch(NullPointerException e) {
        println("Could not find audio " + name + " for stopping!");
      }
    }
  }


  void resumeAll() {
    for (String name : audios.keySet()) {
      try {
        AudioPlayer a = audios.get(name);
        if (a.position() != 0) a.play();
      }
      catch(NullPointerException e) {
        println("Could not find audio " + name + " for resuming!");
      }
    }
  }

  AudioPlayer getAudioPlayer(String name) {
    return audios.get(name);
  }
}