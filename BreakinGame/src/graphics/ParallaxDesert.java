package graphics;
import other.G;

public class ParallaxDesert {
	
	float counter = 0;
	float speed = 0.3f;
	
	public ParallaxDesert(){
	
	}
	
	public ParallaxDesert(int _speed){
		speed = _speed;
	}
	
	public void disp(){
		
		
		
		if(counter < G.p.height)counter += speed;
		else counter = 0;
		
		G.sprite.dispSprite("Static:DesertBG", G.p.width/2,   G.p.height/2 + (int)counter, G.p.width, G.p.height);
		G.sprite.dispSprite("Static:DesertBG", G.p.width/2, - G.p.height/2 + (int)counter, G.p.width, G.p.height);
		
	};
	
}