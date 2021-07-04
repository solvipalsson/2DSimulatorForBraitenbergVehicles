// Main File

public ArrayList<Heater> heaters;
public ArrayList<Lighter> lighters;
public ArrayList<Speaker> speakers;
public HeatMap heatMap;
public LightMap lightMap;
public SoundMap soundMap;


void setup() {
    size(1000, 1000);
    smooth();


    heaters = new ArrayList<Heater>();
    Heater h1 = new Heater(500.0, 500.0, 1000.0);
    heaters.add(h1);


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

}

void draw() {
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