
public class HeatMap extends EnvironmentMap {
    ArrayList<Heater> heaters;

    HeatMap(ArrayList<Heater> heaters_, int resolution_){
        this.resolution = resolution_;
        setColsRows();
        setColor(255, 0, 0);
        init();
        this.heaters = heaters_;
    }

    void addTransmitter(Heater h){
        heaters.add(h);
    }

    void produce() {
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


    void display() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                drawTile(field[i][j],i * resolution,j * resolution);
            }
        }
    }
    
    //Renders a vector object 'v' as an arrow and a position 'x,y'
    void drawTile(float temperature, float x, float y) {
        pushMatrix();
        float gray = map(temperature, 0, 1000, 0, 255);
        float b = map(temperature, 0, 1000, 0, 30);
        fill(gray, b, b);
        rect(x, y, resolution, resolution);
        popMatrix();
    }
}