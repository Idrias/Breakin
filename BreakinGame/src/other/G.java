package other;

import java.util.ArrayList;
import game.GameClient;
import game.GameServer;
import managers.AudioManager;
import managers.SpriteManager;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.net.Client;



public class G {

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/// Global Vars
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Reference to the processing PApplet
	// for networking and doing processing stuff (e.g. draw)
	public static PApplet p;

	/// Important Objects
	// public static Helper h;
	public static GameServer gameServer;
	public static GameClient gameClient;
	public static AudioManager audio;
	public static SpriteManager sprite;

	// Some more global variables
	public static ArrayList<String> playerNames;
	public static boolean[] keys;

	// Resources
	public static PFont defaultFont;
	public static PFont arial;
	public static PImage defaultButtonTexture;

	// Networking
	public static ArrayList<Client> newClients;
	public static ArrayList<Client> disconnectedClients;

	// Constants
	final public static String NET_SPLITSTRING = "-NEXT-";
	final public static int ID_SERVER = 42;


	public static enum graphics {
		None, Off, On, Low, Medium, High
	}

	public enum difficulty {
		easy, normal, hard
	}

	// Settings
	final public static float NETWORK_UPDATERATE = 20; // How often per second
														// do we want to send
														// updates from server
														// to client?
	final public static boolean CLIENTSIDE_PREDICTIONS = true; // Should the
																// gameclient
																// predict
																// movements?
	// GAME SETTINGS //
	final public static int playarea_width = 15;
	final public static int playarea_height = 15; //TODO 
	final public static float gravity = 0.005f;
	final public static float playerspeed = 0.015f;
	
	final public static int KEY_FORWARDS = Character.getNumericValue('w');
	final public static int KEY_BACKWARDS = Character.getNumericValue('s');
	final public static int KEY_RIGHT = Character.getNumericValue('d');
	final public static int KEY_LEFT = Character.getNumericValue('a');
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



	static public void setup(PApplet p) {
		//
		// setup reference to PApplet
		G.p = p;

		// Global Variables
		playerNames = new ArrayList<String>();
		keys = new boolean[256];
		for(int i=0; i<keys.length; i++) keys[i] = false;

		// load some resources
		arial = p.createFont("", 32);
		defaultFont = p.createFont("/Assets/Graphics/Fonts/komikax.ttf", p.height / 27);
		defaultButtonTexture = p.loadImage("/Assets/Graphics/Static_Sprites/Button.png");

		// load many more resources
		sprite = null;
		sprite = new SpriteManager();
		load_sprites();

		audio = null;
		audio = new AudioManager();
		load_audio();

		// instantiate some objects we need
		// h = new Helper();
		newClients = new ArrayList<Client>();
		disconnectedClients = new ArrayList<Client>();
		gameServer = new GameServer();
		gameClient = new GameClient();

	}



	static private void load_sprites() {
		String[] imageFiles = Resources.imageFiles;
		for (int i = 1; i < imageFiles.length; i += 2) {
			String name = imageFiles[i - 1];
			String location = imageFiles[i];
			sprite.addSprite(name, location);
		}
		
		sprite.resizeStatic("Static:SimpleBrick", p.width / playarea_width, p.height / playarea_height);
	}



	static private void load_audio() {
		String[] audioFiles = Resources.audioFiles;

		for (int i = 1; i < audioFiles.length; i += 2) {
			String name = audioFiles[i - 1];
			String location = audioFiles[i];
			audio.addAudio(name, location);
		}
	}



	static public void debug(String text) {
		System.out.println("DEBUG (Frame " + p.frameCount + "/" + p.millis() + "): " + text);
	}



	static public void println(Object text) {
		System.out.println(text);
	}



	static public void printFPS() {
		debug("" + p.frameRate);
	}
}
