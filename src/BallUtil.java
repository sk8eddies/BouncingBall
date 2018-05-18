/**
 * Created by Eddie on 2018-05-04.
 */
public class BallUtil {
    static PolarCoords cartesianToPolar(Vector2D v){
        return new PolarCoords((Math.sqrt(v.getX()*v.getX()+v.getY()*v.getY())), Math.atan(v.getY()/v.getX()));
    }

    static Vector2D polarToCartesian(PolarCoords p){
        return new Vector2D(Math.cos(p.angle)*p.magnitude,Math.sin(p.angle)*p.magnitude);
    }

    static Vector2D rotateCoordinateSystem(Vector2D vector, double angle){
        double newX = vector.getX()*Math.cos(-angle) - vector.getY()*Math.sin(-angle);
        double newY = vector.getX()*Math.sin(-angle) + vector.getY()*Math.cos(-angle);
        return new Vector2D(newX, newY);
    }

    static Vector2D rotateBackwards(Vector2D vector, double angle){
        return  rotateCoordinateSystem(vector, -angle);
    }

}
