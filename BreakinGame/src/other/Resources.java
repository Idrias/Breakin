package other;

public class Resources {

	static final String[] audioFiles;
	static final String[] imageFiles;

	static {
		audioFiles = new String[] { "Music:Trump", "/Assets/Audio/Music/Trump.mp3", "SFX:Game:Bounce",
				"/Assets/Audio/SFX/game/Bounce.mp3", "SFX:Game:BrickBurst", "/Assets/Audio/SFX/game/BrickBurst.mp3",
				"SFX:Game:MexicanAttack1", "/Assets/Audio/SFX/game/MexicanAttack_1.mp3", "SFX:Game:MexicanAttack2",
				"/Assets/Audio/SFX/game/MexicanAttack_2.mp3", "SFX:Game:MexicanAttack3",
				"/Assets/Audio/SFX/game/MexicanAttack_3.mp3", "SFX:Game:MexicanAttack4",
				"/Assets/Audio/SFX/game/MexicanAttack_4.mp3", "SFX:Game:MexicanAttack5",
				"/Assets/Audio/SFX/game/MexicanAttack_5.mp3", "SFX:Game:MexicanLost",
				"/Assets/Audio/SFX/game/MexicanLost.mp3", "SFX:Menu:Back", "/Assets/Audio/SFX/menu/back.wav",
				"SFX:Menu:Hover", "/Assets/Audio/SFX/menu/hover.wav", "SFX:Menu:Next",
				"/Assets/Audio/SFX/menu/next.wav", };

		imageFiles = new String[] {
				// Static
				"Static:BreakinLogo", "/Assets/Graphics/Static_Sprites/Breakin.png", "Static:Tacursor",
				"/Assets/Graphics/Static_Sprites/Tacursor.png", "Static:ButtonTexture",
				"/Assets/Graphics/Static_Sprites/Button.png",

				// Animated
				"Anim:Mexican", "/Assets/Graphics/Spritesheets/Juan_2.png", "Anim:DestructedWallBrick",
				"/Assets/Graphics/Spritesheets//DestructedWallBrick_3.png" };
	}
}