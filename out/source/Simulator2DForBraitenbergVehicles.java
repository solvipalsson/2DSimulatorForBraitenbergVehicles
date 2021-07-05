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
public ArrayList<Vehicle> vehicles;
public HeatMap heatMap;
public LightMap lightMap;
public SoundMap soundMap;
PVector mouse;


public void setup() {
    
    


    heaters = new ArrayList<Heater>();
    Heater h1 = new Heater(500.0f, 500.0f, 600.0f);
    heaters.add(h1);
    Heater h2 = new Heater(100.0f, 100.0f, 300.0f);
    heaters.add(h2);

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
    

    vehicles = new ArrayList<Vehicle>();
    Vehicle v1 = new Vehicle(200.0f, 500.0f);
    TemperatureSensor ts1 = new TemperatureSensor(1.0f, 0.0f, "right");
    v1.addTemperatureSensor(ts1);
    TemperatureSensor ts2 = new TemperatureSensor(0.0f, 1.0f, "left");
    v1.addTemperatureSensor(ts2);

    vehicles.add(v1);
}

public void draw() {
    background(255);

    heatMap.display();
    //lightMap.display();
    //soundMap.display();

    for (Heater h : heaters){
        h.display();
    }

    for (Lighter l : lighters){
        l.display();
    }

    for (Speaker s : speakers){
        s.display();
    }

    // mouse = new PVector(mouseX, mouseY);
    // ellipse(mouse.x, mouse.y, 24, 24);

    for (Vehicle v : vehicles){
        v.steer(heatMap);
        v.display();
    }
    
}

public abstract class EnvironmentMap {
    int resolution;
    float[][] field;
    int cols, rows;
    int col;

    protected void setResolution(int rs){
        this.resolution = rs;
    }

    protected int getResolution(){
        return this.resolution;
    }

    protected int getCols(){
        return this.cols;
    }

    protected int getRows(){
        return this.rows;
    }

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

    protected float getFromField(int col, int row){
        return field[col][row];
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
                    float d = heaterLocation.dist(tileLocation) / 70;
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
        float gray = map(temperature, 0, 1000, 0, 255);
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
public abstract class Sensor {
    float leftEfficiency;
    float rightEfficiency;
    String mountedSide;
    PVector offset;

    protected void setLeftEfficiency(float leftEff){
        this.leftEfficiency = leftEff;
    }
    protected float getLeftEfficiency(){
        return this.leftEfficiency;
    }

    protected void setRightEfficiency(float rightEff){
        this.rightEfficiency = rightEff;
    }
    protected float getRightEfficiency(){
        return this.rightEfficiency;
    }

    protected void setMountedSide(String ms){
        this.mountedSide = ms;
    }

    protected String getMountedSide(){
        return this.mountedSide;
    }

    protected void setOffset(){
        switch (getMountedSide()) {
            case "right":
            this.offset = new PVector(12, 12);
            break;
            case "left":
            this.offset = new PVector(12, -12);
            break;
        } 
    }
    
    //protected abstract float readEnvironmentMap(EnvironmentMap em, PVector location, PVector velocity);

    protected PVector calculateSensorLocation(PVector location, PVector velocity)
    {
        float angle = velocity.heading();
        PVector rotatedOffset = rotateOffset(angle);
        PVector sensorLocation = PVector.add(location, rotatedOffset);
        return sensorLocation;
    }

    private PVector rotateOffset(float angle_)
    {
        float sin = sin(angle_);
        float cos = cos(angle_);

        float xnew = this.offset.x * cos - this.offset.y * sin;
        float ynew = this.offset.x * sin + this.offset.y * cos;

        return new PVector(xnew, ynew);
    }
}


public class TemperatureSensor extends Sensor{


    TemperatureSensor(float leftEff, float rightEff, String mountS){
        setLeftEfficiency(leftEff);
        setRightEfficiency(rightEff);
        setMountedSide(mountS);
        setOffset();
    }

    private float readEnvironmentMap(HeatMap hm, PVector location, PVector velocity){
        PVector sensorLocation = calculateSensorLocation(location, velocity);

        int column = PApplet.parseInt(constrain(sensorLocation.x / hm.getResolution(),0,hm.getCols() - 1));
        int row = PApplet.parseInt(constrain(sensorLocation.y / hm.getResolution(),0,hm.getRows() - 1));

        return hm.getFromField(column, row);
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
public class Vehicle {
    
    PVector location;
    PVector velocity;
    PVector acceleration;
    float leftMotor;
    float rightMotor;
    float maximumSpeed;
    float maximumForce;
    int size; 
    ArrayList<TemperatureSensor> tempSensors;
    
    

    
    Vehicle(float x, float y){
        setSize(20);
        setMaximumSpeed(2.0f);
        setMaximumForce(20.0f);
        setLeftMotor(0.0f);
        setRightMotor(0.0f);
        setInitialVelocity(new PVector(0.0f, 0.0f));
        setInitialAcceleration(new PVector(0.0f, 0.0f));
        setLocation(x, y);
        tempSensors = new ArrayList<TemperatureSensor>();
    }



    private void addTemperatureSensor(TemperatureSensor ts){
        this.tempSensors.add(ts);
    }

    private void readTemperatureSensors(HeatMap hm){
        float value = 0.0f;
        for (TemperatureSensor ts : tempSensors){
            value = ts.readEnvironmentMap(hm, this.location, this.velocity);
            updateLeftMotor(ts.getLeftEfficiency() * value);
            updateRightMotor(ts.getRightEfficiency() * value);
            value = 0.0f;
        }
    }


    public PVector generateTargetFromMotors()
    {
        float rm = this.rightMotor;
        float lm = this.leftMotor;

        if (lm == rm){
            return new PVector(100, 100);
        }

        float radiusToCenter = (this.size / 2) * (lm + rm) / (lm - rm);

        float d = lm + rm ;

        // float ratio = 0;
        // if (lm > rm){
        //     ratio = 1 - rm / lm; 
        // } else if (lm < rm){
        //     ratio = 1 - lm / rm
        // }

        float degreesToTurn = (20.0f * 360.0f) / (2.0f * (float)Math.PI * radiusToCenter);

        if (degreesToTurn > 60.0f){
            degreesToTurn = 60.0f;
        } else if ( degreesToTurn < -60.0f) {
            degreesToTurn = -60.0f;
        }

        float angle = (degreesToTurn * PI/180);
        float newX = (d/2) * cos(angle);
        float newY = (d/2) * sin(angle);

        PVector target = new PVector(newX, newY);
        return target;     
    }

    public PVector calculateTargetVector(){
        // readLightSensors(lm);
        // readSoundSensors(sm);


        PVector targetFromVehicle = generateTargetFromMotors();
        PVector calculatedVector = rotateVector(getAngle(), targetFromVehicle);
        PVector result = PVector.add(this.location, calculatedVector);


        displayVector(result);
        return result;
    }

    private PVector rotateVector(float angle_, PVector vector){
        float sin = sin(angle_);
        float cos = cos(angle_);

        float xnew = vector.x * cos - vector.y * sin;
        float ynew = vector.x * sin + vector.y * cos;

        return new PVector(xnew, ynew);
    }

    private PVector calculateDesiredVelocity(PVector target) {
  
        PVector desired = target.sub(this.location);
        
        desired.normalize();
        desired.mult(this.maximumSpeed);
        
        return desired;
    }

    private PVector calculateSteeringForce(PVector desired) {

        PVector steeringForce = desired.sub(this.velocity);
        
        if (steeringForce.mag() > maximumForce) {
            steeringForce.normalize();
            steeringForce.mult(maximumForce);
        }
        
        return steeringForce;
    }

    private void applyForce(PVector steeringForce) {
        this.acceleration.add(steeringForce);
    }

    private void update() {
        this.velocity.add(this.acceleration);
        this.velocity.limit(this.maximumSpeed);
        this.location.add(this.velocity);
        this.acceleration.mult(0);
        this.leftMotor = 0.0f;
        this.rightMotor = 0.0f;
    }

    private void steer(HeatMap hm){
        readTemperatureSensors(hm);
        println(this.leftMotor);
        println(this.rightMotor);
        PVector target = calculateTargetVector();
        PVector desiredVelocity = calculateDesiredVelocity(target);
        PVector steeringForce = calculateSteeringForce(desiredVelocity);
        applyForce(steeringForce);
        update();
    }


    public void displayVector(PVector vector){
        fill(60);
        stroke(0);
        rectMode(CENTER);
        pushMatrix();
        translate(vector.x, vector.y);
        
        strokeWeight(0);
        beginShape();
        triangle(size / 2, 0, 0, - size, - size / 2, 0);
        endShape(CLOSE);
        popMatrix();
    }

     public void display() {
        fill(127);
        stroke(0);
        rectMode(CENTER);
        pushMatrix();
        translate(this.location.x, this.location.y);
        rotate(this.velocity.heading() + PI/2);
        strokeWeight(0);
        beginShape();
        rect(size / 2 , 0,  size / 2, size / 2);
        rect( - size / 2 , 0,  size / 2, size / 2);
        triangle(size / 2, 0, 0, - size, - size / 2, 0);
        endShape(CLOSE);
        popMatrix();
    }


    private float getAngle(){
        return this.velocity.heading();
    }
    private void updateLeftMotor(float toAdd){
        this.leftMotor += toAdd;
    }

    private void updateRightMotor(float toAdd){
        this.rightMotor += toAdd;
    }

    private void setLocation(float x, float y) {
        this.location = new PVector(x, y);
    }
    
    private void setVelocity(float x, float y) {
        this.velocity = new PVector(x, y);
    }
    
    private void setAcceleration(float x, float y) {
        this.acceleration = new PVector(x, y);
    }
    
    private void setLeftMotor(float lm){
        this.leftMotor = lm;
    }

    private void setRightMotor(float rm){
        this.rightMotor = rm;
    }

    private void setSize(int s) {
        this.size = s;
    }
    
    private void setMaximumSpeed(float maxS) {
        this.maximumSpeed = maxS;
    }
    
    private void setMaximumForce(float maxF) {
        this.maximumForce = maxF;
    }

    private PVector getLocation() {
        return this.location;
    }
    
    private PVector getVelocity() {
        return this.velocity;
    }
    
    private PVector getAcceleration() {
        return this.acceleration;
    }
    
    private int getSize() {
        return this.size;
    }
    
    private float getMaximumSpeed() {
        return this.maximumSpeed;
    }
    
    private float getMaximumForce() {
        return this.maximumForce;
    }
    
    private void setInitialVelocity(PVector iv){
        this.velocity = iv;
    }

    private void setInitialAcceleration(PVector ia){
        this.acceleration = ia;
    }
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
