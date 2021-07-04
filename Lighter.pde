public class Lighter extends Transmitter{
    private float lux;
    
    Lighter(float x, float y, float luxval){
        setLocation(x, y);
        setColor(0, 255, 0);
        this.lux = luxval;
    }
}