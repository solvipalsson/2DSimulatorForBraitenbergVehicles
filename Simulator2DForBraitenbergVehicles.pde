// Main File

public ArrayList<Heater> heaters;
public ArrayList<Lighter> lighters;
public ArrayList<Speaker> speakers;
public ArrayList<Vehicle> vehicles;
public HeatMap heatMap;
public LightMap lightMap;
public SoundMap soundMap;
PVector mouse;


void setup() {
    size(1000, 1000);
    smooth();


    heaters = new ArrayList<Heater>();
    Heater h1 = new Heater(500.0, 500.0, 600.0);
    heaters.add(h1);
    Heater h2 = new Heater(100.0, 100.0, 300.0);
    heaters.add(h2);

    lighters = new ArrayList<Lighter>();
    Lighter l1 = new Lighter(300.0, 200.0, 500.0);
    lighters.add(l1);

    speakers = new ArrayList<Speaker>();
    Speaker s1 = new Speaker(800.0, 700.0, 500.0);
    speakers.add(s1);

    heatMap = new HeatMap(heaters, 10);
    heatMap.produce();

    lightMap = new LightMap(lighters, 10);
    lightMap.produce();

    soundMap = new SoundMap(speakers, 10);
    soundMap.produce();
    

    vehicles = new ArrayList<Vehicle>();
    Vehicle v1 = new Vehicle(200.0, 500.0);
    TemperatureSensor ts1 = new TemperatureSensor(1.0, 0.0, "right");
    v1.addTemperatureSensor(ts1);
    TemperatureSensor ts2 = new TemperatureSensor(0.0, 1.0, "left");
    v1.addTemperatureSensor(ts2);

    vehicles.add(v1);
}

void draw() {
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