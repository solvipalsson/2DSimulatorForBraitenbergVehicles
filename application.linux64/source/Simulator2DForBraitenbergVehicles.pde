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
import static javax.swing.JOptionPane.*;
int envMap = 0;


void setup() {
    size(1300, 1000);
    smooth();
    
    showIUI = true;
    heaters = new ArrayList<Heater>();
    lighters = new ArrayList<Lighter>();
    speakers = new ArrayList<Speaker>();
    vehicles = new ArrayList<Vehicle>();
}

void draw() {
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
    Heater h1 = new Heater(500.0, 500.0, 800.0);
    heaters.add(h1);
    Heater h2 = new Heater(700.0, 700.0, 500.0);
    heaters.add(h2);

    heatMap = new HeatMap(heaters, 10);
    heatMap.produce();
    envMap = 1;
    // V1
    Vehicle v1 = new Vehicle(100.0, 500.0);
    TemperatureSensor ts0 = new TemperatureSensor(1.0, 1.0, "right");
    v1.addTemperatureSensor(ts0);
    vehicles.add(v1);

    //V2a
    Vehicle v2a = new Vehicle(200.0, 300.0);
    TemperatureSensor ts1 = new TemperatureSensor(1.0, 0.0, "left");
    v2a.addTemperatureSensor(ts1);
    TemperatureSensor ts2 = new TemperatureSensor(0.0, 1.0, "right");
    v2a.addTemperatureSensor(ts2);
    vehicles.add(v2a);

    //V2b
    Vehicle v2b = new Vehicle(300.0, 400.0);
    TemperatureSensor ts3 = new TemperatureSensor(1.0, 0.0, "right");
    v2b.addTemperatureSensor(ts3);
    TemperatureSensor ts4 = new TemperatureSensor(0.0, 1.0, "left");
    v2b.addTemperatureSensor(ts4);
    vehicles.add(v2b);

    //V2c 
    Vehicle v2c = new Vehicle(400.0, 500.0);
    TemperatureSensor ts5 = new TemperatureSensor(1.0, 1.0, "right");
    v2c.addTemperatureSensor(ts5);
    TemperatureSensor ts6 = new TemperatureSensor(1.0, 1.0, "left");
    v2c.addTemperatureSensor(ts6);
    vehicles.add(v2c);

    //V3a
    Vehicle v3a = new Vehicle(500.0, 600.0);
    TemperatureSensor ts7 = new TemperatureSensor(0.0, -1.0, "right");
    v3a.addTemperatureSensor(ts7);
    TemperatureSensor ts8 = new TemperatureSensor(-1.0, 0.0, "left");
    v3a.addTemperatureSensor(ts8);
    vehicles.add(v3a);

    //V3b
    Vehicle v3b = new Vehicle(600.0, 700.0);
    TemperatureSensor ts9 = new TemperatureSensor(-1.0, 0.0, "right");
    v3b.addTemperatureSensor(ts9);
    TemperatureSensor ts10 = new TemperatureSensor(0.0, -1.0, "left");
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
        float xCoord = getFloatFromInput("Please insert the x coordinate of the transmitter", 0.0, width);
        float yCoord = getFloatFromInput("Please insert the y coordinate of the transmitter", 0.0, height);
        
        switch(type) {
            case 0:
                float kelvin = getFloatFromInput("Please type in the strength in Kelvin between 0.0 and 1000.0", 0.0, 1000.0);
                heaters.add(new Heater(xCoord, yCoord, kelvin));
                break;
            case 1:
                float lux = getFloatFromInput("Please type in the strenght in Lux between 0.0 and 2000.0", 0.0, 2000.0);
                lighters.add(new Lighter(xCoord, yCoord, lux));
                break;
            case 2:
                float dB = getFloatFromInput("Please type in the strength in decibel bewteen 0.0 and 150.0", 0.0, 150.0);
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
    float friction = getFloatFromInput("Please input a constant for the friction of the system between 0.0 and 1.0", 0.0, 1.0);
}

private void chooseEnvironmentMap() {
    Object[] options = {"No Map", "HeatMap", "LightMap", "SoundMap"};
    envMap = getOptionFromUser(options, "Please choose an environment map that you would like to see displayed", "Environment Map");
}


private void startVehicleBuilder() {
    float xCoord = getFloatFromInput("Please insert the initial x coordinate of the vehicle", 0.0, width);
    float yCoord = getFloatFromInput("Please insert the y coordinate of the vehicle", 0.0, height);
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
        float leftMotor = getFloatFromInput("Please wire the sensor to the left motor of the vehicle with a float between -1.0 and 1.0", -1.0, 1.0);
        float rightMotor = getFloatFromInput("Please wire the sensor to the right motor of the vehicle with a float between -1.0 and 1.0", -1.0, 1.0);
        
        
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
