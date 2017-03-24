package game.actors;

import network.utilities.NetworkEntity;
import other.G;
import processing.core.PVector;



public abstract class GameObject {

	// Each actor within the game inherits from the GameObject object.
	public NetworkEntity ne; // TODO NOT PUBLIC
	int lastUpdate;



	public GameObject(NetworkEntity ne) {
		// Wenn der Client erstellt: NetworkEntity schon vorhanden!
		this.ne = ne;
		lastUpdate = G.p.millis();
	}



	public GameObject(int actorType, int networkID) {
		// Wenn der Server erstellt: NetworkEntity muss generiert werden!
		this(new NetworkEntity(actorType, networkID));
	}



	public void simpleMove(int deltaT) {
		PVector pos = get_pos();
		PVector speed = get_speed();
		if (pos != null && speed != null) {
			pos.x += speed.x * deltaT;
			pos.y += speed.y * deltaT;
			if (pos.x > G.p.width || pos.x < 0) {
				set_speed(new PVector(speed.x * -1, speed.y));
				set_pos(new PVector(G.p.width / 2, G.p.height / 2));
			}
			if (pos.y > G.p.height || pos.y < 0) {
				set_speed(new PVector(speed.x, speed.y * -1));
				set_pos(new PVector(G.p.width / 2, G.p.height / 2));
			}
		}
	}



	public void set_speed(PVector speed) {
		ne.set_speed(speed);
	}



	public void set_pos(PVector pos) {
		ne.set_pos(pos);
	}



	public PVector get_speed() {
		return ne.get_speed() == null ? new PVector(0, 0) : ne.get_speed();
	}



	public PVector get_pos() {
		return ne.get_pos() == null ? new PVector(0, 0) : ne.get_pos();
	}



	public NetworkEntity ne() {
		return ne;
	}



	abstract public void update();



	abstract public void draw();
}
