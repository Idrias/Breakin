package game.actors;

import network.utilities.NetworkEntity;
import other.G;
import processing.core.PVector;



public abstract class GameObject {

	// Each actor within the game inherits from the GameObject object.
	NetworkEntity ne;
	int lastUpdate;


	public GameObject(NetworkEntity ne) {
		// Wenn der Client erstellt: NetworkEntity schon vorhanden!
		this.ne = ne;
		lastUpdate = G.p.millis();
	}



	public GameObject(Class<?> actorType, int networkID) {
		// Wenn der Server erstellt: NetworkEntity muss generiert werden!
		this(new NetworkEntity(actorType, networkID));
	}



	///////////////////////////////////////////////////////////////////////////


	public PVector simpleMove() {
		int deltaT = G.p.millis() - lastUpdate;
		PVector pos = get_pos();
		
		set_speed(0, G.gravity);
		PVector speed = get_speed();

		if (pos != null && speed != null) {
			pos.x += speed.x * deltaT;
			pos.y += speed.y * deltaT;
			lastUpdate = G.p.millis();
			return pos;
		}
		lastUpdate = G.p.millis();
		return null;
	}



	///////////////////////////////////////////////////////////////////////////

	public void set_ne(NetworkEntity ne) {
		this.ne = ne;
	}



	public void set_speed(PVector speed) {
		if(speed!=null) ne.set_speed(speed);
	}



	// TODO use this function instead of PVector one
	public void set_speed(float speedX, float speedY) {
		ne.set_speed(new PVector(speedX, speedY));
	}



	public void set_pos(PVector pos) {
		if(pos!=null) ne.set_pos(pos);
	}



	public void set_pos(float posX, float posY) {
		ne.set_pos(new PVector(posX, posY));
	}



	public NetworkEntity get_ne() {
		return ne;
	}



	public PVector get_speed() {
		return ne.get_speed() == null ? new PVector(0, 0) : ne.get_speed();
	}



	public PVector get_pos() {
		return ne.get_pos() == null ? new PVector(0, 0) : ne.get_pos();
	}



	///////////////////////////////////////////////////////////////////////////

	abstract public void update();



	abstract public void draw();
}
