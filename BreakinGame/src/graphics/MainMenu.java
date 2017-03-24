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

		Button single, multi, settings, credits, quit, startSP, difficulty, backTo1, backTo4, graphics, audioButton, language, trailQuali, bgQuali, animations, vSync, resolution, fullScreen, create, connect, start, join;
		// Create Buttons Position X Position Y Size X Size Y
		single = new Button(displayWidth / 2, displayHeight / 2 + displayHeight / 12, displayWidth / 3, displayHeight / 15, 2, "SINGLEPLAYER", true, defaultButtonTexture);
		multi = new Button(displayWidth / 2, displayHeight / 2 + 2 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 3, "MULTIPLAYER", true, defaultButtonTexture);
		settings = new Button(displayWidth / 2, displayHeight / 2 + 3 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 4, "SETTINGS", true, defaultButtonTexture);
		credits = new Button(displayWidth / 2, displayHeight / 2 + 4 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 1, "CREDITS", true, defaultButtonTexture);
		quit = new Button(displayWidth / 2, displayHeight / 2 + 5 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 6, "QUIT", false, defaultButtonTexture);

		startSP = new Button(displayWidth / 2, displayHeight / 2 + displayHeight / 12, displayWidth / 3, displayHeight / 15, 2, "START GAME", true, defaultButtonTexture);
		difficulty = new Button(displayWidth / 2, displayHeight / 2 + 2 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 7, "DIFFICULTY: NORMAL", true, defaultButtonTexture);
		backTo1 = new Button(displayWidth / 2, displayHeight / 2 + 5 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 1, "BACK", false, defaultButtonTexture);

		graphics = new Button(displayWidth / 2, displayHeight / 2 + displayHeight / 12, displayWidth / 3, displayHeight / 15, 41, "GRAPHICS", true, defaultButtonTexture);
		audioButton = new Button(displayWidth / 2, displayHeight / 2 + 2 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 4, "SOUND", true, defaultButtonTexture);
		language = new Button(displayWidth / 2, displayHeight / 2 + 3 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 4, "LANGUAGE", true, defaultButtonTexture);

		trailQuali = new Button(32 * displayWidth / 99, displayHeight / 2 + displayHeight / 12, displayWidth / 3, displayHeight / 15, 411, "TRAIL QUALITY: HIGH", true, defaultButtonTexture);
		bgQuali = new Button(67 * displayWidth / 99, displayHeight / 2 + displayHeight / 12, displayWidth / 3, displayHeight / 15, 41, "BACKGROUND QUALITY: HIGH", true, defaultButtonTexture);
		animations = new Button(32 * displayWidth / 99, displayHeight / 2 + 2 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 41, "ANIMATIONS: ON", true, defaultButtonTexture);
		vSync = new Button(67 * displayWidth / 99, displayHeight / 2 + 2 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 41, "VSYNC: ON", true, defaultButtonTexture);
		resolution = new Button(32 * displayWidth / 99, displayHeight / 2 + 3 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 41, "RESOLUTION: " + displayWidth + "x" + displayHeight, true, defaultButtonTexture);
		fullScreen = new Button(67 * displayWidth / 99, displayHeight / 2 + 3 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 41, "FULLSCREEN", true, defaultButtonTexture);
		backTo4 = new Button(displayWidth / 2, displayHeight / 2 + 5 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 4, "BACK", false, defaultButtonTexture);

		create = new Button(displayWidth / 2, displayHeight / 2 + displayHeight / 12, displayWidth / 3, displayHeight / 15, 31, "CREATE ROOM", true, defaultButtonTexture);
		connect = new Button(displayWidth / 2, displayHeight / 2 + 2 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 32, "JOIN ROOM", true, defaultButtonTexture);
		start = new Button(displayWidth / 2, displayHeight / 2 + 2 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 311, "START GAME", true, defaultButtonTexture);
		join = new Button(displayWidth / 2, displayHeight / 2 + 2 * displayHeight / 12, displayWidth / 3, displayHeight / 15, 321, "CONNECT", true, defaultButtonTexture);

		s1Buttons = new Button[] { single, multi, settings, credits, quit };
		s2Buttons = new Button[] { startSP, difficulty, backTo1 };
		s3Buttons = new Button[] { create, connect, backTo1 };
		s31Buttons = new Button[] { start, backTo1 };
		s32Buttons = new Button[] { join, backTo1 };
		s4Buttons = new Button[] { graphics, audioButton, language, backTo1 };

		s41Buttons = new Button[] { trailQuali, bgQuali, animations, vSync, resolution, fullScreen, backTo4 };

		fft = new FFT(G.audio.getAudioPlayer("Music:MainMenu").bufferSize(), G.audio.getAudioPlayer("Music:MainMenu").sampleRate());

		G.audio.stopAll();
		G.audio.loop("Music:MainMenu");

		inputName = new InputBox(displayWidth / 2 - 17 * displayWidth / 48 + displayWidth / 10, displayHeight / 2 - 2 * displayHeight / 48 + displayHeight / 40, displayWidth / 5, displayHeight / 30, false);
		inputIP = new InputBox(displayWidth / 2 - 17 * displayWidth / 48 + displayWidth / 10, displayHeight / 2 + 2 * displayHeight / 48 + displayHeight / 40, displayWidth / 5, displayHeight / 30, false);

		s1Window = new Window(displayWidth / 2, displayHeight / 2 + 3 * displayHeight / 12 + displayHeight / 100, 9 * displayWidth / 24, 11 * displayHeight / 24);
		s41Window = new Window(displayWidth / 2, displayHeight / 2 + 3 * displayHeight / 12 + displayHeight / 100, 35 * displayWidth / 48, 11 * displayHeight / 24);
		s31Window = new Window(displayWidth / 2, displayHeight / 2 + 2 * displayHeight / 12 + displayHeight / 100, 35 * displayWidth / 48, 15 * displayHeight / 24);

		createRoom = new Label(displayWidth / 2 - 17 * displayWidth / 48, displayHeight / 2 - 4 * displayHeight / 48, 50, G.defaultFont, G.p.color(255), "CREATE ROOM", true, 3, false, G.p.color(0));
		enterIp = new Label(displayWidth / 2 - 17 * displayWidth / 48, displayHeight / 2 + 2 * displayHeight / 48, 44, G.arial, G.p.color(255), "IP Adress:", false, 0, true, G.p.color(0));
		enterName = new Label(displayWidth / 2 - 17 * displayWidth / 48, displayHeight / 2 - 2 * displayHeight / 48, 44, G.arial, G.p.color(255), "Playername:", false, 0, true, G.p.color(0));
		players = new Label(displayWidth / 2 - 17 * displayWidth / 48, displayHeight / 2 + 10 * displayHeight / 48, 44, G.arial, G.p.color(255), "Players:", false, 0, true, G.p.color(0));
		// enterIp = new Label(displayWidth/2, displayHeight/2, 44, Arial, color
		// (255), "IP Adress:", false, 0, true, color(0));

		// createLabels = new Label[]{enterIp};

	}



	public void draw() {
		G.p.background(255);

		drawWaves();
		G.p.tint(255);
		G.sprite.dispSprite("Static:BreakinLogo", displayWidth / 2, 3 * displayHeight / 12, (float)(displayWidth / 1.5), (float)displayWidth / 5);

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
			// s41Buttons[0].text = "TRAIL QUALITY: " + graphicSettings
			break;
		case 6:
			G.p.exit();
		}

		// enterIp.disp();

		// Sample Animation
		// sprite.dispAnimation("Anim:DestructedWallBrick", 150, 100, 100, 50,
		// 45, 9); //PosX, PosY, Width, Height, Animation Speed, Animation
		// Frames

		for (Button b : renderButtons) {
			int i = b.disp();
			if (i != -1) screen = i;
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

	}



	void drawWaves() {
		G.p.noStroke();
		G.p.pushMatrix();
		G.p.translate(displayWidth / 2, displayHeight / 2 + displayHeight / 12);
		G.p.rotate(bgRotation * PApplet.TWO_PI / 360);
		bgRotation += 0.2;
		G.p.colorMode(PApplet.HSB, fftSize, 100, 100);

		// fft.forward(audio.getAudioPlayer("Music:MainMenu").right);

		// background(0, 0, 100);
		fft.forward(G.audio.getAudioPlayer("Music:Trump").right);

		for (int i = 0; i < fftSize; i++) {
			float band = fft.getBand(i);
			G.p.fill(i, 150 + 100 * (band / 10), 100, 160);
			G.p.arc(0, 0, 300 + band * 3 * (i + 1), 300 + band * 3 * (i + 1), ai * i, ai * (i + 1));
		}
		G.p.popMatrix();
		G.p.colorMode(PApplet.RGB, 255);
		G.p.stroke(0);
	}
}
