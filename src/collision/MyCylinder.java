package collision;

import org.ode4j.math.DVector3;

public class MyCylinder {
    private DVector3 center; // cylinder's center
    private double height;    // cylinder's height
    private double radius;    // top and bottom's circles' radius

    public MyCylinder(DVector3 center, double height, double radius) {
        this.center = center;
        this.height = height;
        this.radius = radius;
    }

    public DVector3 getCenter() {
        return center;
    }

    public void setCenter(DVector3 center) {
        this.center = center;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
