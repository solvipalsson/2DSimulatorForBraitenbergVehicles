public class Vehicle {
    
    PVector location;
    PVector velocity;
    PVector acceleration;
    float leftMotor;
    float rightMotor;
    float maximumSpeed;
    float maximumForce;
    int size; 
    ArrayList<TemperatureSensor> tempSensors;
    ArrayList<LightSensor> lightSensors;
    ArrayList<SoundSensor> soundSensors;
    

    
    Vehicle(float x, float y){
        setSize(20);
        setMaximumSpeed(5.0);
        setMaximumForce(0.1);
        setLeftMotor(0.0);
        setRightMotor(0.0);
        setInitialVelocity(new PVector(0.0, 0.0));
        setInitialAcceleration(new PVector(0.0, 0.0));
        setLocation(x, y);
        tempSensors = new ArrayList<TemperatureSensor>();
        lightSensors = new ArrayList<LightSensor>();
        soundSensors = new ArrayList<SoundSensor>();
    }



    private void addTemperatureSensor(TemperatureSensor ts){
        this.tempSensors.add(ts);
    }

    private void addLightSensor(LightSensor ls){
        this.lightSensors.add(ls);
    }

    private void addSoundSensor(SoundSensor ss){
        this.soundSensors.add(ss);
    }

    private void readTemperatureSensors(HeatMap hm){
        float value = 0.0;
        for (TemperatureSensor ts : tempSensors){
            value = ts.readEnvironmentMap(hm, this.location, this.velocity);
            updateLeftMotor(ts.getLeftEfficiency() * value);
            updateRightMotor(ts.getRightEfficiency() * value);
            value = 0.0;
        }
    }

    private void readLightSensors(LightMap lm){
        float value = 0.0;
        for (LightSensor ls : lightSensors){
            value = ls.readEnvironmentMap(lm, this.location, this.velocity);
            updateLeftMotor(ls.getLeftEfficiency() * value);
            updateRightMotor(ls.getRightEfficiency() * value);
            value = 0.0;
        }
    }

    private void readSoundSensors(SoundMap sm){
        float value = 0.0;
        for (SoundSensor ss : soundSensors){
            value = ss.readEnvironmentMap(sm, this.location, this.velocity);
            updateLeftMotor(ss.getLeftEfficiency() * value);
            updateRightMotor(ss.getRightEfficiency() * value);
            value = 0.0;
        }
    }




    public PVector generateTargetFromMotors()
    {
        float rm = this.rightMotor;
        float lm = this.leftMotor;


        if (rm < 0.0){
            rm = 0.0;
        }
        if (lm < 0.0){
            lm = 0.0;
        }
        float degreesToTurn = 0.0;
        float d = lm + rm ;

        if (lm != rm){
            float radiusToCenter = (this.size / 2) * (lm + rm) / (lm - rm);
            degreesToTurn = (20.0 * 360.0) / (2.0 * (float)Math.PI * radiusToCenter);

            if (degreesToTurn > 60.0){
                degreesToTurn = 60.0;
            } else if ( degreesToTurn < -60.0) {
                degreesToTurn = -60.0;
            }
        }

        

        float angle = (degreesToTurn * PI/180);
        float newX = (d/2) * cos(angle);
        float newY = (d/2) * sin(angle);

        PVector target = new PVector(newX, newY);
        return target;     
    }

    public PVector calculateTargetVector(){
        // readLightSensors(lm);
        // readSoundSensors(sm);


        PVector targetFromVehicle = generateTargetFromMotors();
        PVector calculatedVector = rotateVector(getAngle(), targetFromVehicle);
        PVector result = PVector.add(this.location, calculatedVector);


        displayVector(result);
        return result;
    }

    private PVector rotateVector(float angle_, PVector vector){
        float sin = sin(angle_);
        float cos = cos(angle_);

        float xnew = vector.x * cos - vector.y * sin;
        float ynew = vector.x * sin + vector.y * cos;

        return new PVector(xnew, ynew);
    }

    private PVector calculateDesiredVelocity(PVector target) {
  
        PVector desired = target.sub(this.location);
        
        desired.normalize();
        desired.mult(this.maximumSpeed);
        
        return desired;
    }

    private PVector calculateSteeringForce(PVector desired) {

        PVector steeringForce = desired.sub(this.velocity);
        
        if (steeringForce.mag() > maximumForce) {
            steeringForce.normalize();
            steeringForce.mult(maximumForce);
        }
        
        return steeringForce;
    }

    private void applyForce(PVector steeringForce) {
        this.acceleration.add(steeringForce);
    }

    private void update() {
        this.velocity.add(this.acceleration);
        this.velocity.limit(this.maximumSpeed);
        this.location.add(this.velocity);
        this.acceleration.mult(0);
        this.leftMotor = 0.0;
        this.rightMotor = 0.0;
    }

    private void steer(HeatMap hm, LightMap lm, SoundMap sm){
        readSensors(hm, lm, sm);
        PVector target = calculateTargetVector();
        PVector desiredVelocity = calculateDesiredVelocity(target);
        PVector steeringForce = calculateSteeringForce(desiredVelocity);
        applyForce(steeringForce);
        update();
    }

    private void readSensors(HeatMap hm, LightMap lm, SoundMap sm){
        readTemperatureSensors(hm);
        readLightSensors(lm);
        readSoundSensors(sm);
    }


    void displayVector(PVector vector){
        fill(60);
        stroke(0);
        rectMode(CENTER);
        pushMatrix();
        translate(vector.x, vector.y);
        
        strokeWeight(0);
        beginShape();
        triangle(size / 2, 0, 0, - size, - size / 2, 0);
        endShape(CLOSE);
        popMatrix();
    }

     void display() {
        fill(127);
        stroke(0);
        rectMode(CENTER);
        pushMatrix();
        translate(this.location.x, this.location.y);
        rotate(this.velocity.heading() + PI/2);
        strokeWeight(0);
        beginShape();
        rect(size / 2 , 0,  size / 2, size / 2);
        rect( - size / 2 , 0,  size / 2, size / 2);
        triangle(size / 2, 0, 0, - size, - size / 2, 0);
        endShape(CLOSE);
        popMatrix();
    }


    private float getAngle(){
        return this.velocity.heading();
    }
    private void updateLeftMotor(float toAdd){
        this.leftMotor += toAdd;
    }

    private void updateRightMotor(float toAdd){
        this.rightMotor += toAdd;
    }

    private void setLocation(float x, float y) {
        this.location = new PVector(x, y);
    }
    
    private void setVelocity(float x, float y) {
        this.velocity = new PVector(x, y);
    }
    
    private void setAcceleration(float x, float y) {
        this.acceleration = new PVector(x, y);
    }
    
    private void setLeftMotor(float lm){
        this.leftMotor = lm;
    }

    private void setRightMotor(float rm){
        this.rightMotor = rm;
    }

    private void setSize(int s) {
        this.size = s;
    }
    
    private void setMaximumSpeed(float maxS) {
        this.maximumSpeed = maxS;
    }
    
    private void setMaximumForce(float maxF) {
        this.maximumForce = maxF;
    }

    private PVector getLocation() {
        return this.location;
    }
    
    private PVector getVelocity() {
        return this.velocity;
    }
    
    private PVector getAcceleration() {
        return this.acceleration;
    }
    
    private int getSize() {
        return this.size;
    }
    
    private float getMaximumSpeed() {
        return this.maximumSpeed;
    }
    
    private float getMaximumForce() {
        return this.maximumForce;
    }
    
    private void setInitialVelocity(PVector iv){
        this.velocity = iv;
    }

    private void setInitialAcceleration(PVector ia){
        this.acceleration = ia;
    }
}