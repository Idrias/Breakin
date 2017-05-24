package other;

public class Resources {

	static final String[] audioFiles;
	static final String[] imageFiles;

	static {
		audioFiles = new String[] {   
				
				//SFX
				"SFX:Game:Bounce", "/Assets/Audio/SFX/game/Bounce.mp3",
				"SFX:Game:BrickBurst", "/Assets/game/MexicanAttack_1.mp3",
				"SFX:Game:MexicanAttack2", "/Assets/Audio/SFX/gts/Audio/SFX/game/BrickBurst.mp3",
				"SFX:Game:MexicanAttack1","/Assets/Audio/SFX/ame/MexicanAttack_2.mp3", 
				"SFX:Game:MexicanAttack3", "/Assets/Audio/SFX/game/MexicanAttack_3.mp3", 
				"SFX:Game:MexicanAttack4", "/Assets/Audio/SFX/game/MexicanAttack_4.mp3", 
				"SFX:Game:MexicanAttack5", "/Assets/Audio/SFX/game/MexicanAttack_5.mp3", 
				"SFX:Game:MexicanLost", "/Assets/Audio/SFX/game/MexicanLost.mp3", 
				"SFX:Menu:Helicopter", "/Assets/Audio/SFX/game/Helicopter.mp3",
				"SFX:Menu:Back", "/Assets/Audio/SFX/menu/back.mp3", 
				"SFX:Menu:Hover", "/Assets/Audio/SFX/menu/hover.mp3",
				"SFX:Menu:Next", "/Assets/Audio/SFX/menu/next.mp3",
				"SFX:Menu:Buzzer", "/Assets/Audio/SFX/menu/buzzer.mp3",
				
				//Music
				"Music:Trump", "/Assets/Audio/Music/Trump.mp3", 
				"Music:MainMenu", "/Assets/Audio/Music/bgmMainMenu.mp3" };
				
				
		imageFiles = new String[] {
				// Static
				"Static:BreakinLogo", "/Assets/Graphics/Static_Sprites/Breakin.png", 
				"Static:DesertBG", "/Assets/Graphics/Static_Sprites/Desert.png", 
				"Static:ButtonTexture", "/Assets/Graphics/Static_Sprites/Button.png",
				"Static:Tacursor", "/Assets/Graphics/Static_Sprites/Tacursor.png",
				"Static:SimpleBrick", "/Assets/Graphics/Static_Sprites/WallBrick.png",
				"Static:IronBrick", "/Assets/Graphics/Static_Sprites/IronBrick.png",
				"Static:TNTBrick", "/Assets/Graphics/Static_Sprites/TNTBrick.png",
				
				// Animated
				"Anim:Mexican", "/Assets/Graphics/Spritesheets/Juan_2.png", 
				"Anim:Sombrero", "/Assets/Graphics/Spritesheets/Sombrero_2.png",
				"Anim:Rocket", "/Assets/Graphics/Spritesheets/rocket_2.png",
				"Anim:Helicopter", "/Assets/Graphics/Spritesheets/Helicopter_2.png",
				"Anim:DestructedWallBrick", "/Assets/Graphics/Spritesheets/DestructedWallBrick_3.png" };
	}
}
