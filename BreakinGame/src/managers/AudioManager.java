package managers;

import java.util.HashMap;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import other.G;



public class AudioManager {

	HashMap<String, AudioPlayer> audios;



	public AudioManager() {
		audios = new HashMap<String, AudioPlayer>();
	}



	public void addAudio(String name, String path) {
		audios.put(name, new Minim(G.p).loadFile(path));
	}



	public void addAudio(String name, String path, int bufferSize) {
		audios.put(name, new Minim(G.p).loadFile(path, bufferSize));
	}



	public void setLoopPoints(String name, int a, int b) {
		try {
			audios.get(name).setLoopPoints(a, b);
		}
		catch (NullPointerException e) {
			G.println("Could not find audio " + name + " for setting loop points!");
		}
	}



	public void loop(String name) {
		try {
			audios.get(name).loop();
		}
		catch (NullPointerException e) {
			G.println("Could not find audio " + name + " for looping!");
		}
	}



	public void play(String name) {
		try {
			AudioPlayer a = audios.get(name);
			a.pause();
			a.rewind();
			a.play();
		}
		catch (NullPointerException e) {
			G.println("Could not find audio " + name + " for playing!");
		}
	}



	public void stop(String name) {
		try {
			AudioPlayer a = audios.get(name);
			a.pause();
			a.rewind();
		}
		catch (NullPointerException e) {
			G.println("Could not find audio " + name + " for stopping!");
		}
	}



	public void pause(String name) {
		try {
			AudioPlayer a = audios.get(name);
			a.pause();
		}
		catch (NullPointerException e) {
			G.println("Could not find audio " + name + " for pausing!");
		}
	}



	public void playNoRewind(String name) {
		try {
			audios.get(name).play();
		}
		catch (NullPointerException e) {
			G.println("Could not find audio " + name + " for playing (no rewind)!");
		}
	}



	public void pauseAll() {
		for (String name : audios.keySet()) {
			try {
				audios.get(name).pause();
			}
			catch (NullPointerException e) {
				G.println("Could not find audio " + name + " for pausing!");
			}
		}
	}



	public void stopAll() {
		for (String name : audios.keySet()) {
			try {
				AudioPlayer a = audios.get(name);
				a.pause();
				a.rewind();
			}
			catch (NullPointerException e) {
				G.println("Could not find audio " + name + " for stopping!");
			}
		}
	}



	public void resumeAll() {
		for (String name : audios.keySet()) {
			try {
				AudioPlayer a = audios.get(name);
				if (a.position() != 0) a.play();
			}
			catch (NullPointerException e) {
				G.println("Could not find audio " + name + " for resuming!");
			}
		}
	}



	public AudioPlayer getAudioPlayer(String name) {
		return audios.get(name);
	}
}
