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
