package game.actors.dynamic;

import java.util.ArrayList;
import game.actors.GameObject;
import graphics.Trail;
import network.utilities.NetworkEntity;
import other.G;
import other.Helper;
import processing.core.PVector;

public class Sombrero extends GameObject {
	
	Trail trail;
	
	public Sombrero(int networkID) {
			super(Sombrero.class, networkID);
			setDefaultValues();
			
			
		}
		
		
		
		public Sombrero(NetworkEntity ne) {
			super(ne);
			setDefaultValues();
		}
		
		public GameObject setDefaultValues(){
			float defaultWidth = 0.7f * 9 / 16;
			float defaultHeight = 0.7f;
			set_size(defaultWidth, defaultHeight);
			
			return this;
		}
		
		
		public void update(ArrayList<GameObject> others) {
			simpleMove(others);
		}
		
		
		
		public void draw() {
			
			if(trail == null)trail = new Trail(0);   //Edit Line 43 in Trail.java to enable colored Trails
			
			PVector pos = Helper.GameToDrawPos(get_pos());
			PVector size = Helper.GameToDrawSize(get_size());
			trail.disp(G.p.mouseX, G.p.mouseY);
			
			G.sprite.dispAnimation("Anim:Sombrero", G.p.mouseX, G.p.mouseY, size.x, size.y, 80, 4);
			//G.sprite.dispAnimation("Anim:Helicopter", G.p.mouseX, G.p.mouseY, size.x*6, size.y*6, 40, 4);
		}
		
}
