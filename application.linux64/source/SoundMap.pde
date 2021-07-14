public class SoundMap extends EnvironmentMap {
    ArrayList<Speaker> speakers;

    SoundMap(ArrayList<Speaker> speakers_, int resolution_){
        this.resolution = resolution_;
        setColsRows();
        setColor(255, 0, 0);
        init();
        this.speakers = speakers_;
    }

    void addTransmitter(Speaker h){
        speakers.add(h);
    }

    void produce() {
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


    void display() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                drawTile(field[i][j],i * resolution,j * resolution);
            }
        }
    }
    
    //Renders a vector object 'v' as an arrow and a position 'x,y'
    void drawTile(float dB, float x, float y) {
        pushMatrix();
        float gray = map(dB, 0, 2000, 0, 255);
        float b = map(dB, 0, 1000, 0, 30);
        fill(b, b, gray);
        rect(x, y, resolution, resolution);
        popMatrix();
    }
}
