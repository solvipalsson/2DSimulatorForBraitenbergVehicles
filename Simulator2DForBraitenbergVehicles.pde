// Main File

public ArrayList<Heater> heaters;
public ArrayList<Lighter> lighters;
public ArrayList<Speaker> speakers;

void setup() {
    size(1000, 1000);
    smooth();


    heaters = new ArrayList<Heater>();
    Heater h1 = new Heater(500.0, 500.0, 600.0);
    heaters.add(h1);


    lighters = new ArrayList<Lighter>();
    Lighter l1 = new Lighter(300.0, 200.0, 500.0);
    lighters.add(l1);

    speakers = new ArrayList<Speaker>();
    Speaker s1 = new Speaker(800.0, 700.0, 200.0);
    speakers.add(s1);

}

void draw() {
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