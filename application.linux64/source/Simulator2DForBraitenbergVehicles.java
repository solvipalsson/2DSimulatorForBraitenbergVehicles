import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import static javax.swing.JOptionPane.*; 

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
boolean showIUI;

int envMap = 0;


public void setup() {
    
    
    
    showIUI = true;
    heaters = new ArrayList<Heater>();
    lighters = new ArrayList<Lighter>();
    speakers = new ArrayList<Speaker>();
    vehicles = new ArrayList<Vehicle>();
}

public void draw() {
    background(255);
    
    while(showIUI) {
        startIUI();
        showIUI = false;
    }
    
    displayEnvironmentMap();
    
    for (Heater h : heaters) {
        h.display();
    }
    
    for (Lighter l : lighters) {
        l.display();
    }
    
    for (Speaker s : speakers) {
        s.display();
    }
    
    
    for (Vehicle v : vehicles) {
        v.steer(heatMap, lightMap, soundMap);
        v.display();
    }
    
}

private void startIUI() {
    int sOr = qStudentOrResearcher();
    //Student is 0 and REsearcher is 1
    if (sOr == 0) {
        //Automatic world is 0 and customized world is 1
        int cw = qCustomizeWorld();
        
        if (cw == 0) {
            buildAutomaticWorld();

        }
        
        else if (cw == 1) {
            startEnvironmentBuilder();
            startVehicleBuilder();
        }
    }
    
    else if (sOr == 1) {
        startEnvironmentBuilder();
        startVehicleBuilder();
    }
    
}


private void displayEnvironmentMap() {
    switch(envMap) {
        case 0:
            break;
        case 1:
            heatMap.display();
            break;
        case 2:
            lightMap.display();
            break;
        case 3:
            soundMap.display();
            break;
    } 
}

private int qCustomizeWorld() {
    Object[] options = {"No - Build it for me",
        "Yes"};
    int n = getOptionFromUser(options, "Would you like to customize the simulator?", "Automated or custom world");
    
    return n;
}

private int qStudentOrResearcher() {
    Object[] options = {"Student", "Researcher"};
    int n =  getOptionFromUser(options, "Are you a student or a researcher", "User type");
    
    return n;
}



private void buildAutomaticWorld() {
    // Heaters
    Heater h1 = new Heater(500.0f, 500.0f, 800.0f);
    heaters.add(h1);
    Heater h2 = new Heater(700.0f, 700.0f, 500.0f);
    heaters.add(h2);

    heatMap = new HeatMap(heaters, 10);
    heatMap.produce();
    envMap = 1;
    // V1
    Vehicle v1 = new Vehicle(100.0f, 500.0f);
    TemperatureSensor ts0 = new TemperatureSensor(1.0f, 1.0f, "right");
    v1.addTemperatureSensor(ts0);
    vehicles.add(v1);

    //V2a
    Vehicle v2a = new Vehicle(200.0f, 300.0f);
    TemperatureSensor ts1 = new TemperatureSensor(1.0f, 0.0f, "left");
    v2a.addTemperatureSensor(ts1);
    TemperatureSensor ts2 = new TemperatureSensor(0.0f, 1.0f, "right");
    v2a.addTemperatureSensor(ts2);
    vehicles.add(v2a);

    //V2b
    Vehicle v2b = new Vehicle(300.0f, 400.0f);
    TemperatureSensor ts3 = new TemperatureSensor(1.0f, 0.0f, "right");
    v2b.addTemperatureSensor(ts3);
    TemperatureSensor ts4 = new TemperatureSensor(0.0f, 1.0f, "left");
    v2b.addTemperatureSensor(ts4);
    vehicles.add(v2b);

    //V2c 
    Vehicle v2c = new Vehicle(400.0f, 500.0f);
    TemperatureSensor ts5 = new TemperatureSensor(1.0f, 1.0f, "right");
    v2c.addTemperatureSensor(ts5);
    TemperatureSensor ts6 = new TemperatureSensor(1.0f, 1.0f, "left");
    v2c.addTemperatureSensor(ts6);
    vehicles.add(v2c);

    //V3a
    Vehicle v3a = new Vehicle(500.0f, 600.0f);
    TemperatureSensor ts7 = new TemperatureSensor(0.0f, -1.0f, "right");
    v3a.addTemperatureSensor(ts7);
    TemperatureSensor ts8 = new TemperatureSensor(-1.0f, 0.0f, "left");
    v3a.addTemperatureSensor(ts8);
    vehicles.add(v3a);

    //V3b
    Vehicle v3b = new Vehicle(600.0f, 700.0f);
    TemperatureSensor ts9 = new TemperatureSensor(-1.0f, 0.0f, "right");
    v3b.addTemperatureSensor(ts9);
    TemperatureSensor ts10 = new TemperatureSensor(0.0f, -1.0f, "left");
    v3b.addTemperatureSensor(ts10);
    vehicles.add(v3b);

    // //V3c
    // Vehicle v3a = new Vehicle(500.0, 600.0);
    // TemperatureSensor ts7 = new TemperatureSensor(0.0, -1.0, "right");
    // v1.addTemperatureSensor(ts7);
    // TemperatureSensor ts8 = new TemperatureSensor(-1.0, 0.0, "left");
    // v1.addTemperatureSensor(ts8);
    // vehicles.add(v3a);
}

private void startEnvironmentBuilder() { 
    setTransmitters();
    produceEnvironmentMaps();
    setFriction();
    chooseEnvironmentMap();
}

private void produceEnvironmentMaps() {
    heatMap = new HeatMap(heaters, 10);
    heatMap.produce();
    
    lightMap = new LightMap(lighters, 10);
    lightMap.produce();
    
    soundMap = new SoundMap(speakers, 10);
    soundMap.produce();
}

private void setTransmitters() {
    Object[] options = {"Heater", "Light", "Speaker"};
    Object[] options1 = {"Yes", "No"};
    while(true) {
        //Heater = 0, Light = 1, Speaker = 2
        int type = getOptionFromUser(options, "Please choose the type of transmitter", "Type of Transmitter");
        float xCoord = getFloatFromInput("Please insert the x coordinate of the transmitter", 0.0f, width);
        float yCoord = getFloatFromInput("Please insert the y coordinate of the transmitter", 0.0f, height);
        
        switch(type) {
            case 0:
                float kelvin = getFloatFromInput("Please type in the strength in Kelvin between 0.0 and 1000.0", 0.0f, 1000.0f);
                heaters.add(new Heater(xCoord, yCoord, kelvin));
                break;
            case 1:
                float lux = getFloatFromInput("Please type in the strenght in Lux between 0.0 and 2000.0", 0.0f, 2000.0f);
                lighters.add(new Lighter(xCoord, yCoord, lux));
                break;
            case 2:
                float dB = getFloatFromInput("Please type in the strength in decibel bewteen 0.0 and 150.0", 0.0f, 150.0f);
                speakers.add(new Speaker(xCoord, yCoord, dB));
                break;
        }
        
        int cont = getOptionFromUser(options1, "Would you like to insert another transmitter?", "Another transmitter");
        if (cont == 0) {
            continue;
        } 
        else if (cont == 1) {
            break;
        }
    } 
}


private void setFriction() {
    float friction = getFloatFromInput("Please input a constant for the friction of the system between 0.0 and 1.0", 0.0f, 1.0f);
}

private void chooseEnvironmentMap() {
    Object[] options = {"No Map", "HeatMap", "LightMap", "SoundMap"};
    envMap = getOptionFromUser(options, "Please choose an environment map that you would like to see displayed", "Environment Map");
}


private void startVehicleBuilder() {
    float xCoord = getFloatFromInput("Please insert the initial x coordinate of the vehicle", 0.0f, width);
    float yCoord = getFloatFromInput("Please insert the y coordinate of the vehicle", 0.0f, height);
    Vehicle v = new Vehicle(xCoord, yCoord);
    setSensor(v);
    
    vehicles.add(v);
}


private void setSensor(Vehicle v_) {
    Object[] typeOptions = {"Temperature Sensor", "Light Sensor", "Sound Sensor"};
    Object[] sideOptions = {"Left", "Right"};
    Object[] optionsYN = {"Yes", "No"};
    while(true) {
        //Heater = 0, Light = 1, Speaker = 2
        int type = getOptionFromUser(typeOptions, "Please choose the type of Sensor", "Type of Sensor");
        String mountedSide = mountedSideToString(getOptionFromUser(sideOptions, "Please choose which side of the vehicle the sensor should be mounted on", "Mounted Side"));
        float leftMotor = getFloatFromInput("Please wire the sensor to the left motor of the vehicle with a float between -1.0 and 1.0", -1.0f, 1.0f);
        float rightMotor = getFloatFromInput("Please wire the sensor to the right motor of the vehicle with a float between -1.0 and 1.0", -1.0f, 1.0f);
        
        
        switch(type) {
            case 0:
                TemperatureSensor ts = new TemperatureSensor(leftMotor, rightMotor, mountedSide);
                v_.addTemperatureSensor(ts);
                break;
            case 1:
                LightSensor ls = new LightSensor(leftMotor, rightMotor, mountedSide);
                v_.addLightSensor(ls);
                break;
            case 2:
                SoundSensor ss = new SoundSensor(leftMotor, rightMotor, mountedSide);
                v_.addSoundSensor(ss);
                break;
        };
        
        int cont = getOptionFromUser(optionsYN, "Would you like to insert another Sensor?", "Another Sensor Option");
        if (cont == 0) {
            continue;
        } 
        else if (cont == 1) {
            break;
        }
    } 
}

private String mountedSideToString(int ms) {
    switch(ms) {
        case 0:
            return "left";
        case 1:
            return "right";
        default:
            return "left";
    }
}

private float getFloatFromInput(String question, float lowerLimit, float upperLimit) {
    while(true) {
        try {
            String answer = showInputDialog(question);
            if (answer == null) {
                Object[]options = {"Yes", "No"};
                int n = getOptionFromUser(options, "You are about to terminate the program and all progress will be lost, are you sure", "Confirm Termination");
                if (n ==  0) {
                    System.exit(1);
                } else{
                    continue;
                }
            }
            
            float f = Float.parseFloat(answer);
            
            if (f < lowerLimit || f > upperLimit) {
                throw new ArithmeticException(String.format("The input is invalid, it must be a float (0.00) between %.2f and %.2f", lowerLimit, upperLimit));
            }
            
            return f;
        }
        catch(Exception e) {
            System.out.println(e);
            showMessageDialog(null, String.format("The input is invalid, it must be a float (0.00) between %.2f and %.2f", lowerLimit, upperLimit));
            continue;
        }
    }   
}

private int getOptionFromUser(Object[] options, String question, String header) {    
    int n = showOptionDialog(frame,
        question,
        header,
        DEFAULT_OPTION,
        QUESTION_MESSAGE,
        null,   //donot use a custom Icon
        options,//the titles of buttons
        options[0]);//default button title
    
    return n;
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
                    float d = lighterLocation.dist(tileLocation) / 20;
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


public class LightSensor extends Sensor{


    LightSensor(float leftEff, float rightEff, String mountS){
        setLeftEfficiency(leftEff);
        setRightEfficiency(rightEff);
        setMountedSide(mountS);
        setOffset();
    }

    private float readEnvironmentMap(LightMap lm, PVector location, PVector velocity){
        PVector sensorLocation = calculateSensorLocation(location, velocity);

        int column = PApplet.parseInt(constrain(sensorLocation.x / lm.getResolution(),0,lm.getCols() - 1));
        int row = PApplet.parseInt(constrain(sensorLocation.y / lm.getResolution(),0,lm.getRows() - 1));

        return lm.getFromField(column, row);
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
public class SoundSensor extends Sensor{

    SoundSensor(float leftEff, float rightEff, String mountS){
        setLeftEfficiency(leftEff);
        setRightEfficiency(rightEff);
        setMountedSide(mountS);
        setOffset();
    }

    private float readEnvironmentMap(SoundMap sm, PVector location, PVector velocity){
        PVector sensorLocation = calculateSensorLocation(location, velocity);

        int column = PApplet.parseInt(constrain(sensorLocation.x / sm.getResolution(),0,sm.getCols() - 1));
        int row = PApplet.parseInt(constrain(sensorLocation.y / sm.getResolution(),0,sm.getRows() - 1));

        return sm.getFromField(column, row);
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
    ArrayList<LightSensor> lightSensors;
    ArrayList<SoundSensor> soundSensors;
    

    
    Vehicle(float x, float y){
        setSize(20);
        setMaximumSpeed(5.0f);
        setMaximumForce(0.1f);
        setLeftMotor(0.0f);
        setRightMotor(0.0f);
        setInitialVelocity(new PVector(0.0f, 0.0f));
        setInitialAcceleration(new PVector(0.0f, 0.0f));
        setLocation(x, y);
        tempSensors = new ArrayList<TemperatureSensor>();
        lightSensors = new ArrayList<LightSensor>();
        soundSensors = new ArrayList<SoundSensor>();
    }



    private void addTemperatureSensor(TemperatureSensor ts){
        this.tempSensors.add(ts);
    }

    private void addLightSensor(LightSensor ls){
        this.lightSensors.add(ls);
    }

    private void addSoundSensor(SoundSensor ss){
        this.soundSensors.add(ss);
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

    private void readLightSensors(LightMap lm){
        float value = 0.0f;
        for (LightSensor ls : lightSensors){
            value = ls.readEnvironmentMap(lm, this.location, this.velocity);
            updateLeftMotor(ls.getLeftEfficiency() * value);
            updateRightMotor(ls.getRightEfficiency() * value);
            value = 0.0f;
        }
    }

    private void readSoundSensors(SoundMap sm){
        float value = 0.0f;
        for (SoundSensor ss : soundSensors){
            value = ss.readEnvironmentMap(sm, this.location, this.velocity);
            updateLeftMotor(ss.getLeftEfficiency() * value);
            updateRightMotor(ss.getRightEfficiency() * value);
            value = 0.0f;
        }
    }




    public PVector generateTargetFromMotors()
    {
        float rm = this.rightMotor;
        float lm = this.leftMotor;


        if (rm < 0.0f){
            rm = 0.0f;
        }
        if (lm < 0.0f){
            lm = 0.0f;
        }
        float degreesToTurn = 0.0f;
        float d = lm + rm ;

        if (lm != rm){
            float radiusToCenter = (this.size / 2) * (lm + rm) / (lm - rm);
            degreesToTurn = (20.0f * 360.0f) / (2.0f * (float)Math.PI * radiusToCenter);

            if (degreesToTurn > 60.0f){
                degreesToTurn = 60.0f;
            } else if ( degreesToTurn < -60.0f) {
                degreesToTurn = -60.0f;
            }
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

    private void steer(HeatMap hm, LightMap lm, SoundMap sm){
        readSensors(hm, lm, sm);
        PVector target = calculateTargetVector();
        PVector desiredVelocity = calculateDesiredVelocity(target);
        PVector steeringForce = calculateSteeringForce(desiredVelocity);
        applyForce(steeringForce);
        update();
    }

    private void readSensors(HeatMap hm, LightMap lm, SoundMap sm){
        readTemperatureSensors(hm);
        readLightSensors(lm);
        readSoundSensors(sm);
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
  public void settings() {  size(1300, 1000);  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "Simulator2DForBraitenbergVehicles" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
