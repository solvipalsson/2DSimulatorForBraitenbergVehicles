
public abstract class EnvironmentMap {
    int resolution;
    float[][] field;
    int cols, rows;
    color col;

    protected void setResolution(int rs){
        this.resolution = rs;
    }

    protected int getResolution(){
        return this.resolution;
    }

    protected int getCols(){
        return this.cols;
    }

    protected int getRows(){
        return this.rows;
    }

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

    protected float getFromField(int col, int row){
        return field[col][row];
    }

    protected abstract void display();
    protected abstract void produce();

}

