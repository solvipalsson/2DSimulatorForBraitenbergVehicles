public class LightMap extends EnvironmentMap {
    ArrayList<Lighter> lighters;

    LightMap(ArrayList<Lighter> lighters_, int resolution_){
        this.resolution = resolution_;
        setColsRows();
        setColor(255, 0, 0);
        init();
        this.lighters = lighters_;
    }

    void addTransmitter(Lighter h){
        lighters.add(h);
    }

    void produce() {
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


    void display() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                drawTile(field[i][j],i * resolution,j * resolution);
            }
        }
    }
    
    //Renders a vector object 'v' as an arrow and a position 'x,y'
    void drawTile(float lux, float x, float y) {
        pushMatrix();
        float gray = map(lux, 0, 2000, 0, 255);
        float b = map(lux, 0, 1000, 0, 30);
        fill(b, gray, b);
        rect(x, y, resolution, resolution);
        popMatrix();
    }
}