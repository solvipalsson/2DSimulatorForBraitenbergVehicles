public class SoundSensor extends Sensor{

    SoundSensor(float leftEff, float rightEff, String mountS){
        setLeftEfficiency(leftEff);
        setRightEfficiency(rightEff);
        setMountedSide(mountS);
        setOffset();
    }

    private float readEnvironmentMap(SoundMap sm, PVector location, PVector velocity){
        PVector sensorLocation = calculateSensorLocation(location, velocity);

        int column = int(constrain(sensorLocation.x / sm.getResolution(),0,sm.getCols() - 1));
        int row = int(constrain(sensorLocation.y / sm.getResolution(),0,sm.getRows() - 1));

        return sm.getFromField(column, row);
    }
}
