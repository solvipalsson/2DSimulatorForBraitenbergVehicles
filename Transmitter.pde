
public abstract class Transmitter {
    protected PVector location;
    protected color col;

    protected void setColor(int r, int b, int g){
        this.col = color(r, b, g);
    }; 

    public void setLocation(float x, float y){
        this.location = new PVector(x, y);
    }; 

    protected PVector getLocation(){
        return this.location;
    }

    protected void display() {
        pushMatrix(); // Push the current transformation matrix into the matrix stack.
        translate(location.x, location.y);
        fill(col);
        stroke(0);
        ellipseMode(CENTER);
        ellipse(0, 0, 20.0, 20.0);
        popMatrix();
    };
}