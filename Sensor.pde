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


public class TemperatureSensor extends Sensor{


    TemperatureSensor(float leftEff, float rightEff, String mountS){
        setLeftEfficiency(leftEff);
        setRightEfficiency(rightEff);
        setMountedSide(mountS);
        setOffset();
    }

    private float readEnvironmentMap(HeatMap hm, PVector location, PVector velocity){
        PVector sensorLocation = calculateSensorLocation(location, velocity);

        int column = int(constrain(sensorLocation.x / hm.getResolution(),0,hm.getCols() - 1));
        int row = int(constrain(sensorLocation.y / hm.getResolution(),0,hm.getRows() - 1));

        return hm.getFromField(column, row);
    }
}

