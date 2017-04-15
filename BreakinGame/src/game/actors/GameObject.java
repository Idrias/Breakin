package game.actors;

import java.util.ArrayList;
import game.actors.colliders.Collider;
import game.actors.colliders.NotCollider;
import network.utilities.NetworkEntity;
import other.G;
import processing.core.PVector;



public abstract class GameObject {

	// Each actor within the game inherits from the GameObject object.
	NetworkEntity ne;
	Collider c;
	int lastUpdate;



	public GameObject(NetworkEntity ne) {
		// Wenn der Client erstellt: NetworkEntity schon vorhanden!
		this.ne = ne;
		c = new NotCollider();
		lastUpdate = G.p.millis();
	}



	public GameObject(Class<?> actorType, int networkID) {
		// Wenn der Server erstellt: NetworkEntity muss generiert werden!
		this(new NetworkEntity(actorType, networkID));
	}



	///////////////////////////////////////////////////////////////////////////


	public PVector simpleMove(ArrayList<GameObject> others) {
		// Move with gravity -> general downwards movement
		int deltaT = G.p.millis() - lastUpdate;
		PVector pos = get_pos();

		set_speed(0, G.gravity);
		PVector speed = get_speed();

		if (pos != null && speed != null) {
			pos.x += speed.x * deltaT;
			pos.y += speed.y * deltaT;
			lastUpdate = G.p.millis();
			
			Collider.checkCollision(this, others);
			c.set_center(pos);
			return pos;
		}
		lastUpdate = G.p.millis();
		return null;
	}



	///////////////////////////////////////////////////////////////////////////

	public GameObject set_ne(NetworkEntity ne) {
		this.ne = ne;
		return this;
	}



	public GameObject set_speed(PVector speed) {
		if (speed != null) ne.set_speed(speed);
		return this;
	}



	// TODO use this function instead of PVector one
	public GameObject set_speed(float speedX, float speedY) {
		ne.set_speed(new PVector(speedX, speedY));
		return this;
	}



	public GameObject set_size(PVector size) {
		if (size != null) ne.set_size(size);
		return this;
	}



	public GameObject set_size(float sizeX, float sizeY) {
		ne.set_size(new PVector(sizeX, sizeY));
		return this;
	}



	public GameObject set_pos(PVector pos) {
		if (pos != null) ne.set_pos(pos);
		return this;
	}



	public GameObject set_pos(float posX, float posY) {
		ne.set_pos(new PVector(posX, posY));
		return this;
	}



	public GameObject set_collider(Collider c) {
		this.c = c;
		return this;
	}



	public NetworkEntity get_ne() {
		return ne;
	}



	public PVector get_speed() {
		return ne.get_speed() == null ? new PVector(0, 0) : ne.get_speed();
	}



	public PVector get_size() {
		return ne.get_size() == null ? new PVector(0, 0) : ne.get_size();
	}



	public PVector get_pos() {
		return ne.get_pos() == null ? new PVector(0, 0) : ne.get_pos();
	}



	public Collider get_collider() {
		return c;
	}



	public void touch() {
		lastUpdate = G.p.millis();
	}

	
	public GameObject setDefaultValues() {
		c = new NotCollider();
		set_size(1, 1);
		return this;
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	abstract public void update(ArrayList<GameObject> others);



	abstract public void draw();
}
