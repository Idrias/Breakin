package game.levels;

import game.actors.EndIndicator;
import game.actors.SimpleBrick;
import other.G;



public class L_Test extends Level {
	
	static public final int m1 = 1;
	String[] model1 = new String[] { 
			"xxxxxxxxxxxxoxx", 
			"xxxxxxxxxxxxoxx", 
			"xxxxxxxxxxxxoxx", 
			"xxxxxxxxxxxxoxx", 
			"xxxxxxxxxxxxoxx", 
			"xxxoooooooxxoxx", 
			"xxxoxxxxxoxxoxx",
			"xxxoxxxxxooooxx", 
			"xxxoooxxxxxxxxx", 
			"xxxxxoxxxxxxxxx",
			"xxxoooxxxxxxxxx", 
			"xxxoxxxxxxxxxxx", 
			"xxxoxxxxxxxxxxx", 
			"xxxoxxxxxxxxxxx", 
			};
	
	static public final int m2 = 2;
	String[] model2 = new String[] {
			"oxooooooooooooo", 
			"xoooooooooooooo", 
			"oxooooooooooooo", 
			"xoooooooooooooo", 
			"oxooooooooooooo", 
			"xoooooooooooooo", 
			"oxooooooooooooo",
			"xoooooooooooooo", 
			"oxooooooooooooo", 
			"xoooooooooooooo",
			"oxooooooooooooo", 
			"xoooooooooooooo", 
			"oxooooooooooooo", 
			"xoooooooooooooo", 
	};
	
	public L_Test(int m) {
		String[] model;
		if(m == m1) model = model1; 
		else if(m == m2) model = model2;
		else return;
		
		height = model.length;
		gameObjects.add(new EndIndicator(G.gameServer.generate_uniqueID()).set_pos(0,0));
		
		for (int y = 0; y < model.length; y++) {
			for (int x = 0; x < model[y].length(); x++) {
				switch (model[y].charAt(x)) {
				case 'x':
					gameObjects.add(new SimpleBrick(G.gameServer.generate_uniqueID()).set_pos(x + 0.5f, y));
					break;
				}
			}
		}
	}

}
