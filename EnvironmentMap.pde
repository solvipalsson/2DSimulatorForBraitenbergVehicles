
public abstract class EnvironmentMap {
    int resolution;
    float[][] field;
    int cols, rows;
    color col;

    protected void setColsRows(){
        this.cols = width / this.resolution;
        this.rows = height / this.resolution;
    }

    protected void setColor(int r, int b, int g){
        this.col = color(r, b, g);
    }; 

    protected void init(){
        this.field = new float[this.cols][this.rows];
    }

    protected abstract void display();
    protected abstract void produce();

}

