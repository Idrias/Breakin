package game.actors.dynamic;

import java.util.ArrayList;
import game.actors.GameObject;
import game.actors.colliders.Collider;
import game.actors.colliders.CollisionReport;
import game.actors.colliders.PolygonCollider;
import game.actors.colliders.NotCollider;
import graphics.Trail;
import network.utilities.NetworkEntity;
import other.G;
import other.Helper;
import processing.core.PApplet;
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



	public GameObject setDefaultValues() {
		float defaultWidth = 0.7f * 9 / 16;
		float defaultHeight = 0.7f;
		set_size(defaultWidth, defaultHeight);
		//set_pos(G.playarea_width/2, G.playarea_height/2);
		//set_speed(G.sombrerospeed, -G.sombrerospeed);
		set_pos(G.playarea_width/2, 0);
		set_speed(-G.sombrerospeed*1.2f, G.sombrerospeed/1.3f);
		set_collider(
				new PolygonCollider(get_pos().copy())
				.addPointRelative(-0.1844f, 0f)
				.addPointRelative(-0.1844f, 0.2667f)
				.addPointRelative(0f, 0.35f)
				.addPointRelative(0.1844f, 0.2667f)
				.addPointRelative(0.1844f, 0f)
				.addPointRelative(0f, -0.1167f)
		);
		
		
		return this;
	}



	public void update(ArrayList<GameObject> others) {
		/*
		 * First check if we got hit by someone else
		 * */
		ArrayList<CollisionReport> enemyhits = c.get_hits();
		if(!enemyhits.isEmpty()) {
			// OMG! We got hit!
			CollisionReport lastHit = enemyhits.get(enemyhits.size() - 1);
			PVector attackVector = lastHit.generateResponseVector(this);

			// TODO
			int y = 0;
			while (Collider.checkCollision(this, others)) {
				G.println("OH NO!");
				set_pos(get_pos().add(attackVector));
				c.set_center(get_pos());
				y++; if(y>10) break;
			}

			set_pos(get_pos().add(attackVector.mult(10)));
			c.set_center(get_pos());
			c.clearHits();
		}
		
		int deltaT = getDeltaT(); 
		PVector movement = get_speed().mult(deltaT); 
		
		set_pos(get_pos().add(movement));
		
		c.set_center(get_pos());
		
		boolean collided = Collider.checkCollision(this, others);
		if(collided) {
			ArrayList<CollisionReport> hits = c.get_hits();
			if(hits.size() > 0) {
				CollisionReport lastHit = hits.get(hits.size()-1);
				PVector otherSurface = lastHit.get_otherSurface();
				PVector normSurf = otherSurface.copy().rotate(PApplet.HALF_PI);
				
				float angle = (G.p.HALF_PI - PVector.angleBetween(normSurf, get_speed()) % G.p.HALF_PI);
				float anglepure = PVector.angleBetween(normSurf, get_speed());
				float angleSurf = (G.p.HALF_PI - PVector.angleBetween(otherSurface, get_speed()) % G.p.HALF_PI);
				float angleSurfpure = PVector.angleBetween(otherSurface, get_speed());
				
				G.println("-------");
				G.println("Angle: " + G.p.degrees(angle));
				G.println("Angle Pure: " + G.p.degrees(anglepure));
				G.println("");
				G.println("AngleSurf: " + G.p.degrees(angleSurf));
				G.println("AngleSurf Pure: " + G.p.degrees(angleSurfpure));
				
				if(angleSurfpure < PApplet.HALF_PI) angle *= -1;
				PVector newS = get_speed().rotate(2*angle).mult(-1);
				
				G.p.println("Old Speed: " + get_speed());
				set_speed(newS);
				G.p.println("New Speed: " + get_speed());
				G.p.println("Other surface: " + otherSurface);
				G.p.println("Normal surface: " + normSurf);
				
				int i = 0;
				while(Collider.checkCollision(this, others)) {
					set_pos(get_pos().add(get_speed().mult(100)));
					c.set_center(get_pos());
					i++;
					if(i>10) break;
				}
			}
			c.clearHits();
		}
		
		if(G.p.mousePressed) {
			PVector mouse = Helper.DrawToGamePos(new PVector(G.p.mouseX, G.p.mouseY));
			set_pos(mouse);
			set_collider(
					new PolygonCollider(get_pos().copy())
					.addPointRelative(-0.1844f, 0f)
					.addPointRelative(-0.1844f, 0.2667f)
					.addPointRelative(0f, 0.35f)
					.addPointRelative(0.1844f, 0.2667f)
					.addPointRelative(0.1844f, 0f)
					.addPointRelative(0f, -0.1167f)
			);
			c.set_center(mouse);
			
			if(G.p.frameCount % 20 == 0 && G.p.mouseButton == PApplet.RIGHT)
			set_speed(get_speed().rotate(G.p.radians(10)));
		}
	}



	public void draw() {
		c.draw();
		if (trail == null) trail = new Trail(0); // Edit Line 43 in Trail.java
													// to enable colored Trails

		PVector pos = Helper.GameToDrawPos(get_pos());
		PVector size = Helper.GameToDrawSize(get_size());
		
		//trail.disp((int)pos.x, (int)pos.y);
		G.sprite.dispAnimation("Anim:Sombrero", (int) pos.x, (int) pos.y, size.x, size.y, 80, 4);
		
		PVector speed = Helper.GameToDrawPos(get_speed()).mult(1000);
		G.p.stroke(255, 0, 0);
		G.p.strokeWeight(3);
		G.p.line(pos.x, pos.y, pos.x + speed.x, pos.y+speed.y);
		// G.sprite.dispAnimation("Anim:Helicopter", G.p.mouseX, G.p.mouseY,
		// size.x*6, size.y*6, 40, 4);
		
		/*
		// TODO DEV Debug Tool for finding hitbox points!
		PVector mouse = Helper.DrawToGamePos(new PVector(G.p.mouseX, G.p.mouseY));
		if(get_pos().y > 0)
		G.p.println("Mouse is ingame at: " + mouse, "Relative to Block: " + mouse.sub(get_pos()));
		G.p.fill(255,0,0);
		G.p.ellipse(G.p.mouseX, G.p.mouseY, 5, 5);
		*/
	}

}
