

public class LightSensor extends Sensor{


    LightSensor(float leftEff, float rightEff, String mountS){
        setLeftEfficiency(leftEff);
        setRightEfficiency(rightEff);
        setMountedSide(mountS);
        setOffset();
    }

    private float readEnvironmentMap(LightMap lm, PVector location, PVector velocity){
        PVector sensorLocation = calculateSensorLocation(location, velocity);

        int column = int(constrain(sensorLocation.x / lm.getResolution(),0,lm.getCols() - 1));
        int row = int(constrain(sensorLocation.y / lm.getResolution(),0,lm.getRows() - 1));

        return lm.getFromField(column, row);
    }
}
