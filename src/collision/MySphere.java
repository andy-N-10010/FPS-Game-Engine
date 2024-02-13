package collision;

import org.ode4j.math.*;

class MySphere{
    private DVector3 center; // sphere center
    private double radius; // radius

    public MySphere(DVector3 center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    public DVector3 getCenter() {
        return center;
    }

    public void setCenter(DVector3 center) {
        this.center = center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public boolean intersects(MySphere other) {
        // calculate the distance between two sphere centers
        double distance = this.center.sub(other.center).length();

        return distance <= (this.radius + other.radius);
    }
}
