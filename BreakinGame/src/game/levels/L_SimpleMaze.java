package game.levels;

import java.util.ArrayList;
import game.actors.EndIndicator;
import game.actors.bricks.IronBrick;
import other.G;



public class L_SimpleMaze extends Level {


	public L_SimpleMaze() {
		height = 10;

		boolean[][] futureMaze = new boolean[G.playarea_width][height];

		for (int x = 0; x < G.playarea_width; x++) {
			for (int y = 0; y < height; y++) {
				futureMaze[x][y] = false;
			}
		}
		int tries = 0;
		while (!isSolvable(futureMaze) && tries < 10000) {
			tries++;
			futureMaze[(int) G.p.random(G.playarea_width)][(int) G.p.random(height)] = true;
		}

		for (int x = 0; x < G.playarea_width; x++) {
			for (int y = 0; y < height; y++) {
				if (!futureMaze[x][y]) {
					IronBrick s = new IronBrick(G.gameServer.generate_uniqueID());
					s.set_pos(x + 0.5f, y);
					gameObjects.add(s);
				}
			}
		}

		gameObjects.add(new EndIndicator(G.gameServer.generate_uniqueID()).set_pos(0, 0));
	}



	boolean isSolvable(boolean[][] proposal) {


		int ySize = height;
		int xSize = G.playarea_width;

		class Pathway {

			ArrayList<Integer[]> past;
			public int currentX;
			public int currentY;



			Pathway(ArrayList<Integer[]> past, int currentX, int currentY) {
				this.past = past;
				this.currentX = currentX;
				this.currentY = currentY;
				this.past.add(new Integer[] { currentX, currentY });
			}



			ArrayList<Pathway> findNew() {
				ArrayList<Pathway> returnVals = new ArrayList<Pathway>();
				int tryX, tryY;

				tryX = currentX + 1;
				tryY = currentY;
				if (tryX < xSize && tryX >= 0 && tryY < ySize && tryY >= 0 && proposal[tryX][tryY] && !isInPast(tryX, tryY)) returnVals.add(new Pathway(past, tryX, tryY));

				tryX = currentX - 1;
				tryY = currentY;
				if (tryX < xSize && tryX >= 0 && tryY < ySize && tryY >= 0 && proposal[tryX][tryY] && !isInPast(tryX, tryY)) returnVals.add(new Pathway(past, tryX, tryY));

				tryX = currentX;
				tryY = currentY + 1;
				if (tryX < xSize && tryX >= 0 && tryY < ySize && tryY >= 0 && proposal[tryX][tryY] && !isInPast(tryX, tryY)) returnVals.add(new Pathway(past, tryX, tryY));

				tryX = currentX;
				tryY = currentY - 1;
				if (tryX < xSize && tryX >= 0 && tryY < ySize && tryY >= 0 && proposal[tryX][tryY] && !isInPast(tryX, tryY)) returnVals.add(new Pathway(past, tryX, tryY));
				return returnVals;
			}



			boolean isInPast(int x, int y) {
				for (Integer[] p : past) {
					if (p[0] == x && p[1] == y) return true;
				}
				return false;
			}



			public void printPast() {
				for (Integer[] i : past) {
					G.println("PRINTING PAST");
					G.println("Past Entry: " + i[0] + " / " + i[1]);
				}
			}
		}

		ArrayList<Pathway> paths = new ArrayList<Pathway>();

		for (int x = 0; x < xSize; x++) {
			if (proposal[x][ySize - 1]) paths.add(new Pathway(new ArrayList<Integer[]>(), x, ySize - 1));
		}


		while (!paths.isEmpty()) {
			for (int i = 0; i < paths.size(); i++) {
				Pathway p = paths.get(i);
				paths.addAll(p.findNew());

				paths.remove(i);
				i--;

				if (p.currentY == 0) {
					return true;
				}
			}
		}


		return false;
	}
}
