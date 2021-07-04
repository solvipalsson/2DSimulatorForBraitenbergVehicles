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
public HeatMap heatMap;
public LightMap lightMap;
public SoundMap soundMap;


public void setup() {
    
    


    heaters = new ArrayList<Heater>();
    Heater h1 = new Heater(500.0f, 500.0f, 1000.0f);
    heaters.add(h1);


    lighters = new ArrayList<Lighter>();
    Lighter l1 = new Lighter(300.0f, 200.0f, 500.0f);
    lighters.add(l1);

    speakers = new ArrayList<Speaker>();
    Speaker s1 = new Speaker(800.0f, 700.0f, 500.0f);
    speakers.add(s1);

    heatMap = new HeatMap(heaters, 10);
    heatMap.produce();

    lightMap = new LightMap(lighters, 10);
    lightMap.produce();

    soundMap = new SoundMap(speakers, 10);
    soundMap.produce();

}

public void draw() {
    background(255);

    //heatMap.display();
    //lightMap.display();
    soundMap.display();

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

public abstract class EnvironmentMap {
    int resolution;
    float[][] field;
    int cols, rows;
    int col;

    protected void setColsRows(){
        this.cols = width / this.resolution;
        this.rows = height / this.resolution;
    }

    protected void setColor(int r, int b, int g){
        this.col = color(r, b, g);
    }; 

    protected void init(){
        this.field = new float[this.cols][this.rows];
    }

    protected abstract void display();
    protected abstract void produce();

}


public class HeatMap extends EnvironmentMap {
    ArrayList<Heater> heaters;

    HeatMap(ArrayList<Heater> heaters_, int resolution_){
        this.resolution = resolution_;
        setColsRows();
        setColor(255, 0, 0);
        init();
        this.heaters = heaters_;
    }

    public void addTransmitter(Heater h){
        heaters.add(h);
    }

    public void produce() {
        float xVal = 0;
        float yVal = 0;
        for (Heater heater : heaters){
            PVector heaterLocation = heater.getLocation();
            for (int i = 0; i < cols; i++) {
                xVal = resolution*i;
                for (int j = 0; j < rows; j++)
                {
                    yVal = resolution*j;
                    PVector tileLocation = new PVector(xVal, yVal);
                    float d = heaterLocation.dist(tileLocation) / 200;
                    if (d < 1)
                    {
                        d=1;
                    }
                    float t = heater.degreesKelvin / pow(d, 2);
                    field[i][j] = Math.max(field[i][j], t);
                }
            }
        }
    }


    public void display() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                drawTile(field[i][j],i * resolution,j * resolution);
            }
        }
    }
    
    //Renders a vector object 'v' as an arrow and a position 'x,y'
    public void drawTile(float temperature, float x, float y) {
        pushMatrix();
        float gray = map(temperature, 0, 2000, 0, 255);
        float b = map(temperature, 0, 1000, 0, 30);
        fill(gray, b, b);
        rect(x, y, resolution, resolution);
        popMatrix();
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
public class LightMap extends EnvironmentMap {
    ArrayList<Lighter> lighters;

    LightMap(ArrayList<Lighter> lighters_, int resolution_){
        this.resolution = resolution_;
        setColsRows();
        setColor(255, 0, 0);
        init();
        this.lighters = lighters_;
    }

    public void addTransmitter(Lighter h){
        lighters.add(h);
    }

    public void produce() {
        float xVal = 0;
        float yVal = 0;
        for (Lighter lighter : lighters){
            PVector lighterLocation = lighter.getLocation();
            for (int i = 0; i < cols; i++) {
                xVal = resolution*i;
                for (int j = 0; j < rows; j++)
                {
                    yVal = resolution*j;
                    PVector tileLocation = new PVector(xVal, yVal);
                    float d = lighterLocation.dist(tileLocation) / 200;
                    if (d < 1)
                    {
                        d=1;
                    }
                    float t = lighter.lux / pow(d, 2);
                    field[i][j] = Math.max(field[i][j], t);
                }
            }
        }
    }


    public void display() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                drawTile(field[i][j],i * resolution,j * resolution);
            }
        }
    }
    
    //Renders a vector object 'v' as an arrow and a position 'x,y'
    public void drawTile(float lux, float x, float y) {
        pushMatrix();
        float gray = map(lux, 0, 2000, 0, 255);
        float b = map(lux, 0, 1000, 0, 30);
        fill(b, gray, b);
        rect(x, y, resolution, resolution);
        popMatrix();
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
public class SoundMap extends EnvironmentMap {
    ArrayList<Speaker> speakers;

    SoundMap(ArrayList<Speaker> speakers_, int resolution_){
        this.resolution = resolution_;
        setColsRows();
        setColor(255, 0, 0);
        init();
        this.speakers = speakers_;
    }

    public void addTransmitter(Speaker h){
        speakers.add(h);
    }

    public void produce() {
        float xVal = 0;
        float yVal = 0;
        for (Speaker speaker : speakers){
            PVector speakerLocation = speaker.getLocation();
            for (int i = 0; i < cols; i++) {
                xVal = resolution*i;
                for (int j = 0; j < rows; j++)
                {
                    yVal = resolution*j;
                    PVector tileLocation = new PVector(xVal, yVal);
                    float d = speakerLocation.dist(tileLocation) / 200;
                    if (d < 1)
                    {
                        d=1;
                    }
                    float t = speaker.dB / pow(d, 2);
                    field[i][j] = Math.max(field[i][j], t);
                }
            }
        }
    }


    public void display() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                drawTile(field[i][j],i * resolution,j * resolution);
            }
        }
    }
    
    //Renders a vector object 'v' as an arrow and a position 'x,y'
    public void drawTile(float dB, float x, float y) {
        pushMatrix();
        float gray = map(dB, 0, 2000, 0, 255);
        float b = map(dB, 0, 1000, 0, 30);
        fill(b, b, gray);
        rect(x, y, resolution, resolution);
        popMatrix();
    }
}
public class Speaker extends Transmitter{
    private float dB;
    
    Speaker(float x, float y, float decibel){
        setLocation(x, y);
        setColor(0, 0, 255);
        this.dB = decibel;
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

    protected PVector getLocation(){
        return this.location;
    }

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
