package collision;

import engine.objects.GameObject;
import org.ode4j.math.*;

public class MySphere extends MyShape{
    private DVector3 center; // sphere center
    private double radius; // radius

    public MySphere(GameObject object, DVector3 center, float radius) {
        super(object);
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

    // test pass!
    public boolean intersects(MySphere other) {
        // calculate the distance between two sphere centers
        double distance = this.center.copy().sub(other.center).length();

        return distance <= (this.radius + other.radius);
    }

    @Override
    public void update() {
        center = new DVector3(getObject().getPosition());
    }
}
