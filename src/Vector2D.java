/**
 * Created by Eddie on 2018-05-04.
 */
public class Vector2D {
    private double x;
    private double y;

    public Vector2D(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double newX){
        this.x = newX;
    }

    public void setY(double newY){
        this.y = newY;
    }

    @Override
    public String toString(){
        return "(" + getX() + ", " + getY()+")";
    }

}
