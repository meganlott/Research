import processing.core.*;



public class Particle {

	Simulation env;
	private PApplet parent;
	PVector pos;
	PVector v;
	PVector a;
	PVector repulse;
	
	
  @SuppressWarnings("static-access")
public Particle(Simulation env, float x, float y, float v_x, float v_y) {
	  this.env = env;
	  this.parent = env.gui;
	  pos = new PVector((x * env.width), (y * env.height));
	  if (pos.x > (env.height-1.5)) pos.x -= 1.5;
	  v = new PVector(v_x * env.width, v_y * env.height);
	  a = new PVector(0,0);
	  repulse = new PVector(0, 0);
  }
  
  @SuppressWarnings("static-access")
void draw() {
    
	  parent.ellipse(pos.x, pos.y, 3, 3);
    //point(pos.x, pos.y);
    
    a = new PVector(0,0);
    
    if (env.gravity) {
      PVector temp = new PVector(0, env.grav_mag * env.height);
      a.add(temp);
    }
    
    //repulsion forces
    if (env.repulsion) {
	    for (Particle p : env.parts) {
	      float dist = parent.dist(pos.x, pos.y, p.pos.x, p.pos.y);
	      if (dist > 0 && dist <= env.radius) {
	        //REPULSE! 
	        float s_max = (float) ((env.radius-dist)/env.radius); // range of s_max [0,1)
	        PVector pMinusQ = new PVector(pos.x - p.pos.x, pos.y - p.pos.y);
	        float pMinusQMag = pMinusQ.mag();
	        PVector force = PVector.div(pMinusQ, pMinusQMag);
	        force.mult(s_max);
	        force.mult(env.width*3);
	        
	        a.add(force);
	        
	      }
	    }
    }
    calcVelocity();
     
  }
  
  public String toString() {
    String str = "Position: (" + pos.x + ", " + pos.y + ")";
    str += "\nVelocity: (" + v.x + ", " + v.y + v.y + ")";
    return str;
  }
  
  @SuppressWarnings("static-access")
void calcVelocity() {
    float new_vx = v.x + (env.delta_t * a.x);
    float new_vy = v.y + (env.delta_t * a.y);
    new_vx *= env.damp;
    new_vy *= env.damp;
    v.set(new_vx, new_vy);
    
  //if particle hits a vertical wall, negate velocity in the x direction
    if (pos.x <= 1.5 && v.x < 0) {
      new_vx = v.x * -(1-env.inellastic);
      v.set(new_vx, v.y);
    }
    else if (pos.x >= env.width-1.5 && v.x > 0) {
      new_vx = v.x * -(1-env.inellastic);
      v.set(new_vx, v.y);
    }
    //if particle hits a horizontal wall, negate velocity in the y direction
    if (pos.y <= 1.5 && v.y < 0) {
      new_vy = v.y * -(1-env.inellastic);
      v.set(v.x, new_vy); 
    }
    else if (pos.y >= env.height-1.5 && v.y > 0) {
      new_vy = v.y * -(1-env.inellastic);
      v.set(v.x, new_vy);
    }
     
    float new_x = pos.x + (env.delta_t * v.x);
    float new_y = pos.y + (env.delta_t * v.y);
    pos.set(new_x, new_y);
  }

}