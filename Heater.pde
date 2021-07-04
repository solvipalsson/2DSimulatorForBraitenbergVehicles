public class Heater extends Transmitter{
    private float degreesKelvin;
    
    Heater(float x, float y, float degreesK){
        setLocation(x, y);
        setColor(255, 0, 0);
        this.degreesKelvin = degreesK;
    }
}