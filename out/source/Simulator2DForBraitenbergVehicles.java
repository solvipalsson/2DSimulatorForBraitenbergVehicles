import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Simulator2DForBraitenbergVehicles extends PApplet {

// Main File

public ArrayList<Heater> heaters;
public ArrayList<Lighter> lighters;
public ArrayList<Speaker> speakers;

public void setup() {
    
    


    heaters = new ArrayList<Heater>();
    Heater h1 = new Heater(500.0f, 500.0f, 600.0f);
    heaters.add(h1);


    lighters = new ArrayList<Lighter>();
    Lighter l1 = new Lighter(300.0f, 200.0f, 500.0f);
    lighters.add(l1);

    speakers = new ArrayList<Speaker>();
    Speaker s1 = new Speaker(800.0f, 700.0f, 200.0f);
    speakers.add(s1);

}

public void draw() {
    background(255);

    for (Heater h : heaters){
        h.display();
    }

    for (Lighter l : lighters){
        l.display();
    }

    for (Speaker s : speakers){
        s.display();
    }
    
}
public class Heater extends Transmitter{
    private float degreesKelvin;
    
    Heater(float x, float y, float degreesK){
        setLocation(x, y);
        setColor(255, 0, 0);
        this.degreesKelvin = degreesK;
    }
}
public class Lighter extends Transmitter{
    private float lux;
    
    Lighter(float x, float y, float luxval){
        setLocation(x, y);
        setColor(0, 255, 0);
        this.lux = luxval;
    }
}
public class Speaker extends Transmitter{
    private float dB;
    
    Speaker(float x, float y, float decibel){
        setLocation(x, y);
        setColor(0, 0, 255);
        this.dB = dB;
    }
}

public abstract class Transmitter {
    protected PVector location;
    protected int col;

    protected void setColor(int r, int b, int g){
        this.col = color(r, b, g);
    }; 

    public void setLocation(float x, float y){
        this.location = new PVector(x, y);
    }; 

    protected void display() {
        pushMatrix(); // Push the current transformation matrix into the matrix stack.
        translate(location.x, location.y);
        fill(col);
        stroke(0);
        ellipseMode(CENTER);
        ellipse(0, 0, 20.0f, 20.0f);
        popMatrix();
    };
}
  public void settings() {  size(1000, 1000);  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Simulator2DForBraitenbergVehicles" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
