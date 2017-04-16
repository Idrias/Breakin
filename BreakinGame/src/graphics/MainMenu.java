package graphics;

import ddf.minim.analysis.FFT;
import graphics.ui.Button;
import graphics.ui.InputBox;
import graphics.ui.Label;
import graphics.ui.Window;
import other.G;
import processing.core.PApplet;
import processing.core.PImage;



public class MainMenu {

	int SCREEN_TITLESCREEN = 1; // Screen 1: Title screen
	int SCREEN_SINGLEPLAYER = 2; // Screen 2: -> Singleplayer
	int SCREEN_START_SINGLEPLAYER = 21; // SCREEN 21; START SINGLEPLAYER
	int SCREEN_MULTIPLAYER = 3; // Screen 3: -> Multiplayer

	int SCREEN_CREATEROOM = 31; // Screen 3 1: Create room
	int SCREEN_JOINROOM = 32; // Screem 3 2: Join room

	int SCREEN_HOSTLOBBY = 311; // Screen 3 1 1: Host Lobby
	int SCREEN_PLAYERLOBBY = 321; // Screen 3 2 1: Player Lobby

	int SCREEN_SETTINGS = 4; // Screen 4: -> Settings
	int SCREEN_SETTINGS_GRAPHICS = 41; // Screen 4 1: -> Screen4 -> Graphics
	int SCREEN_SETTINGS_SOUND = 42; // Screen 4 2: -> Screen4 -> Sound

	int SCREEN_QUIT = 6;
	// Screen 4 3: -> Screen4 -> Language

	int screen = 1;
	int lastScreen = screen;
	Button[] s1Buttons, s2Buttons, s3Buttons, s31Buttons, s32Buttons, s321Buttons, s4Buttons, s41Buttons;
	Label createRoom, joinRoom, enterIp, enterPort, enterHost, enterPlayer, players, waitingRoom;
	InputBox inputHost, inputPlayer, inputIP, inputPort;
	Window s1Window, s41Window, s31Window, s32Window;

	///////////////////////////////////////////////////////////////////////////////////////
	int bufferSize = 512;
	int fftSize = PApplet.floor(bufferSize * .5f) + 1;
	float ai = PApplet.TWO_PI / fftSize;
	float bgRotation = 180;
	FFT fft;
	///////////////////////////////////////////////////////////////////////////////////////

	// Rouven EDIT
	int displayWidth = G.p.width;
	int displayHeight = G.p.height;
	PImage defaultButtonTexture = G.defaultButtonTexture;
	// END Rouven EDIT



	public MainMenu() {

		Button single, multi, settings, credits, quit, startSP, difficulty, difficultyMP, KI, music, backTo1, backTo3, backTo3_1, backTo4, graphics, audioButton, language, trailQuali, bgQuali, animations, vSync, resolution, fullScreen,
				create, connect, start, join;
		// Create Buttons Position X Position Y Size X Size Y
		single = new Button(displayWidth / 2, displayHeight / 2 + displayHeight / 12, displayWidth / 3, displayHeight / 15, SCREEN_SINGLEPLAYER, "SINGLEPLAYER", true, defaultButtonTexture);
		multi = new Button(displayWidth / 2, displayHeight / 2 + 2 * displayHeight / 12, displayWidth / 3, displayHeight / 15, SCREEN_MULTIPLAYER, "MULTIPLAYER", true, defaultButtonTexture);
		settings = new Button(displayWidth / 2, displayHeight / 2 + 3 * displayHeight / 12, displayWidth / 3, displayHeight / 15, SCREEN_SETTINGS, "SETTINGS", true, defaultButtonTexture);
		credits = new Button(displayWidth / 2, displayHeight / 2 + 4 * displayHeight / 12, displayWidth / 3, displayHeight / 15, SCREEN_TITLESCREEN, "CREDITS", true, defaultButtonTexture);
		quit = new Button(displayWidth / 2, displayHeight / 2 + 5 * displayHeight / 12, displayWidth / 3, displayHeight / 15, SCREEN_QUIT, "QUIT", false, defaultButtonTexture);

		startSP = new Button(displayWidth / 2, displayHeight / 2 + displayHeight / 12, displayWidth / 3, displayHeight / 15, SCREEN_START_SINGLEPLAYER, "START GAME", true, defaultButtonTexture);
		difficulty = new Button(displayWidth / 2, displayHeight / 2 + 2 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 7, "DIFFICULTY: NORMAL", true, defaultButtonTexture);
		backTo1 = new Button(displayWidth / 2, displayHeight / 2 + 5 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 1, "BACK", false, defaultButtonTexture);

		graphics = new Button(displayWidth / 2, displayHeight / 2 + displayHeight / 12, displayWidth / 3, displayHeight / 15, 41, "GRAPHICS", true, defaultButtonTexture);
		audioButton = new Button(displayWidth / 2, displayHeight / 2 + 2 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 4, "SOUND", true, defaultButtonTexture);
		language = new Button(displayWidth / 2, displayHeight / 2 + 3 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 4, "LANGUAGE", true, defaultButtonTexture);

		trailQuali = new Button(32 * displayWidth / 99, displayHeight / 2 + displayHeight / 12, displayWidth / 3, displayHeight / 15, 411, "TRAIL QUALITY: HIGH", true, false, defaultButtonTexture);
		bgQuali = new Button(67 * displayWidth / 99, displayHeight / 2 + displayHeight / 12, displayWidth / 3, displayHeight / 15, 41, "BACKGROUND QUALITY: HIGH", true, false, defaultButtonTexture);
		animations = new Button(32 * displayWidth / 99, displayHeight / 2 + 2 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 41, "ANIMATIONS: ON", true, false, defaultButtonTexture);
		vSync = new Button(67 * displayWidth / 99, displayHeight / 2 + 2 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 41, "VSYNC: ON", true, false, defaultButtonTexture);
		resolution = new Button(32 * displayWidth / 99, displayHeight / 2 + 3 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 41, "RESOLUTION: " + displayWidth + "x" + displayHeight, true, false, defaultButtonTexture);
		fullScreen = new Button(67 * displayWidth / 99, displayHeight / 2 + 3 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 41, "FULLSCREEN", true, false, defaultButtonTexture);
		backTo4 = new Button(displayWidth / 2, displayHeight / 2 + 5 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 4, "BACK", false, defaultButtonTexture);

		create = new Button(displayWidth / 2, displayHeight / 2 + displayHeight / 12, displayWidth / 3, displayHeight / 15, 31, "CREATE ROOM", true, defaultButtonTexture);
		connect = new Button(displayWidth / 2, displayHeight / 2 + 2 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 32, "JOIN ROOM", true, defaultButtonTexture);
		join = new Button(displayWidth / 2, displayHeight / 2 + 4 * displayHeight / 12, displayWidth / 4, displayHeight / 15, 321, "CONNECT", true, defaultButtonTexture);
		backTo3_1 = new Button(displayWidth / 2, displayHeight / 2 + 5 * displayHeight / 12, displayWidth / 4, displayHeight / 15, 3, "BACK", false, defaultButtonTexture);

		start = new Button(72 * displayWidth / 100, displayHeight / 2 - displayHeight / 12, displayWidth / 4, displayHeight / 15, 311, "START GAME", true, defaultButtonTexture);
		difficultyMP = new Button(72 * displayWidth / 100, displayHeight / 2, displayWidth / 4, displayHeight / 15, 7, "DIFFICULTY: NORMAL", true, defaultButtonTexture);
		KI = new Button(72 * displayWidth / 100, displayHeight / 2 + displayHeight / 12, displayWidth / 4, displayHeight / 15, 7, "GRENZWACHE: OFF", true, false, defaultButtonTexture);
		music = new Button(72 * displayWidth / 100, displayHeight / 2 + 2 * displayHeight / 12, displayWidth / 4, displayHeight / 15, 7, "Track 1", true, false, defaultButtonTexture);
		backTo3 = new Button(72 * displayWidth / 100, displayHeight / 2 + 5 * displayHeight / 12, displayWidth / 4, displayHeight / 15, 3, "BACK", false, defaultButtonTexture);

		s1Buttons = new Button[] { single, multi, settings, credits, quit };
		s2Buttons = new Button[] { startSP, difficulty, backTo1 };
		s3Buttons = new Button[] { create, connect, backTo1 };
		s31Buttons = new Button[] { start, difficultyMP, KI, music, backTo3 };
		s32Buttons = new Button[] { join, backTo3_1 };
		s321Buttons = new Button[] { backTo3_1 };
		s4Buttons = new Button[] { graphics, audioButton, language, backTo1 };
		s41Buttons = new Button[] { trailQuali, bgQuali, animations, vSync, resolution, fullScreen, backTo4 };

		fft = new FFT(G.audio.getAudioPlayer("Music:MainMenu").bufferSize(), G.audio.getAudioPlayer("Music:MainMenu").sampleRate());

		G.audio.stopAll();
		//G.audio.loop("Music:MainMenu");

		inputHost = new InputBox(displayWidth / 2 - 17 * displayWidth / 48 + displayWidth / 10, displayHeight / 2 - 2 * displayHeight / 48 + displayHeight / 40, displayWidth / 5, displayHeight / 30, false);
		inputPort = new InputBox(displayWidth / 2 - 17 * displayWidth / 48 + displayWidth / 10, displayHeight / 2 + 2 * displayHeight / 48 + displayHeight / 40, displayWidth / 5, displayHeight / 30, false);
		inputPlayer = new InputBox(displayWidth / 2, displayHeight / 2 + 8 * displayHeight / 48 + displayHeight / 200, displayWidth / 4, displayHeight / 30, false);
		inputIP = new InputBox(displayWidth / 2, displayHeight / 2 + 12 * displayHeight / 48 + displayHeight / 200, displayWidth / 4, displayHeight / 30, false);

		s1Window = new Window(displayWidth / 2, displayHeight / 2 + 3 * displayHeight / 12 + displayHeight / 100, 9 * displayWidth / 24, 11 * displayHeight / 24);
		s41Window = new Window(displayWidth / 2, displayHeight / 2 + 3 * displayHeight / 12 + displayHeight / 100, 35 * displayWidth / 48, 11 * displayHeight / 24);
		s31Window = new Window(displayWidth / 2, displayHeight / 2 + 2 * displayHeight / 12 + displayHeight / 100, 35 * displayWidth / 48, 15 * displayHeight / 24);
		s32Window = new Window(displayWidth / 2, displayHeight / 2 + 3 * displayHeight / 12 + displayHeight / 100, 7 * displayWidth / 24, 11 * displayHeight / 24);

		// Titles
		createRoom = new Label(displayWidth / 2 - 17 * displayWidth / 48, displayHeight / 2 - 4 * displayHeight / 48, 50, G.defaultFont, G.p.color(255), "CREATE ROOM", true, 3, false, G.p.color(0));
		waitingRoom = new Label(displayWidth / 2 - 17 * displayWidth / 48, displayHeight / 2 - 4 * displayHeight / 48, 50, G.defaultFont, G.p.color(255), "WAITING FOR PLAYERS", true, 3, false, G.p.color(0));
		joinRoom = new Label(displayWidth / 2 - 6 * displayWidth / 48, displayHeight / 2 + 4 * displayHeight / 48, 50, G.defaultFont, G.p.color(255), "JOIN ROOM", true, 3, false, G.p.color(0));

		// Subtitles
		enterHost = new Label(displayWidth / 2 - 17 * displayWidth / 48, displayHeight / 2 - 2 * displayHeight / 48, 44, G.arial, G.p.color(255), "Playername:", false, 0, true, G.p.color(0));
		enterPort = new Label(displayWidth / 2 - 17 * displayWidth / 48, displayHeight / 2 + 2 * displayHeight / 48, 44, G.arial, G.p.color(255), "Port:", false, 0, true, G.p.color(0));
		enterPlayer = new Label(displayWidth / 2 - 6 * displayWidth / 48, displayHeight / 2 + 7 * displayHeight / 48, 44, G.arial, G.p.color(255), "Playername:", false, 0, true, G.p.color(0));
		enterIp = new Label(displayWidth / 2 - 6 * displayWidth / 48, displayHeight / 2 + 11 * displayHeight / 48, 44, G.arial, G.p.color(255), "IP:", false, 0, true, G.p.color(0));

		players = new Label(displayWidth / 2 - 17 * displayWidth / 48, displayHeight / 2 + 10 * displayHeight / 48, 44, G.arial, G.p.color(255), "Players:", false, 0, true, G.p.color(0));
	}



	public void draw() {
		G.p.background(70);

		drawWaves();
		G.p.tint(255);
		G.sprite.dispSprite("Static:BreakinLogo", displayWidth / 2, 3 * displayHeight / 12, (float) (displayWidth / 1.5), (float) displayWidth / 5);

		Button[] renderButtons = new Button[] {};

		switch (screen) {
		case 1:
			s1Window.disp();
			renderButtons = s1Buttons;
			break;
		case 2:
			s1Window.disp();
			renderButtons = s2Buttons;
			break;
		case 21:
			break;
		case 3:
			s1Window.disp();
			renderButtons = s3Buttons;
			break;
		case 31:
			s31Window.disp();
			inputHost.disp();
			inputPort.disp();

			createRoom.disp();
			enterHost.disp();
			enterPort.disp();
			players.disp();

			renderButtons = s31Buttons;
			break;
		case 32:
			s32Window.disp();

			inputPlayer.disp();
			inputIP.disp();

			joinRoom.disp();
			enterIp.disp();
			enterPlayer.disp();
			renderButtons = s32Buttons;
			break;

		case 321:
			s31Window.disp();
			waitingRoom.disp();
			renderButtons = s321Buttons;
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
			// s41Buttons[0].text = "TRAIL QUALITY: " + graphicSettings
			break;
		case 6:
			G.p.exit();
		}

		// Sample Animation
		// sprite.dispAnimation("Anim:DestructedWallBrick", 150, 100, 100, 50,
		// 45, 9); //PosX, PosY, Width, Height, Animation Speed, Animation
		// Frames

		// Display all the buttons that are relevant for the current screen
		for (Button b : renderButtons) {
			// Get the id of the new screen if the button is pressed
			int i = b.disp();
			if (i != -1) screen = i;
		}
		
		
		if(screen == SCREEN_HOSTLOBBY) {
			if(G.p.keyPressed && G.p.key=='+') {
				// We are a server host and want to launch the game!
				G.gameServer.go_ingame();
			}
		}

		
		if (screen != lastScreen) {
			// we changed screens! //
			// TODO open server etc

			if (lastScreen == SCREEN_JOINROOM && screen == SCREEN_PLAYERLOBBY) {
				// Player entered ip and now wants to join a server!.
				G.gameClient.connect(inputPlayer.get_content(), inputIP.get_content());
			}


			else if (lastScreen == SCREEN_CREATEROOM && screen == SCREEN_HOSTLOBBY) {
				// Player just opened a lobby and wants to be host
				// Open the server, then connect local client to it!
				int port = 0;

				try {
					port = Integer.parseInt(inputIP.get_content());
				}
				catch (NumberFormatException e) {
					port = 4242;
				}

				G.gameServer.activate(port);
				G.gameClient.connect(inputPlayer.get_content(), "127.0.0.1:" + port);
			}

			
			
			else if(lastScreen == SCREEN_SINGLEPLAYER && screen == SCREEN_START_SINGLEPLAYER) {
				// Start a singleplayer game
				G.gameServer.activate(4242);
				G.gameServer.go_ingame();
				G.gameClient.connect("Player", "");
				//G.gameClient.enterGame();
			}
		}

		G.p.pushMatrix();
		G.p.translate(G.p.mouseX + 32, G.p.mouseY + 36); // Ursprung wird an die
															// Ecke unten
		// Rechts vom Taco cursor
		// gesetzt
		if (G.p.mousePressed) {
			G.p.rotate(-7 * PApplet.TWO_PI / 360);
		}
		G.p.tint(255);
		G.sprite.dispSprite("Static:Tacursor", -16, -18, 32, 35);
		G.p.popMatrix();

		lastScreen = screen;
	}



	void drawWaves() {
		G.p.noStroke();
		G.p.pushMatrix();
		G.p.translate(displayWidth / 2, displayHeight / 2 + displayHeight / 12);
		G.p.rotate(bgRotation * PApplet.TWO_PI / 360);
		bgRotation += 0.2;
		G.p.colorMode(PApplet.HSB, fftSize, 100, 100);

		fft.forward(G.audio.getAudioPlayer("Music:MainMenu").right);
		// G.p.background(0x000000); // HEX BLACK (can change)

		for (int i = 0; i < fftSize; i++) {
			float band = fft.getBand(i);
			G.p.fill(i, 150 + 100 * (band / 10), 100, 255);
			G.p.arc(0, 0, 300 + band * 3 * (i + 1), 300 + band * 3 * (i + 1), ai * i, ai * (i + 1));
		}
		G.p.popMatrix();
		G.p.colorMode(PApplet.RGB, 255);
		G.p.stroke(0);
	}
}
