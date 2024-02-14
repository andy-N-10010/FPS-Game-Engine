package collision;

import org.ode4j.math.DVector3;

public class MyCapsule {
    private DVector3 center; // capsule's center
    private double height;    // capsule's height. does not include the spheres
    private double radius;    // top and bottom's spheres' radius

    public MyCapsule(DVector3 center, double height, double radius) {
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
