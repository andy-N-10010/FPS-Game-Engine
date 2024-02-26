package engine.objects;

import org.ode4j.math.DVector3;

public class Camera {
    private DVector3 position, rotation;

    public Camera(DVector3 position, DVector3 rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public DVector3 getPosition() {
        return position;
    }

    public DVector3 getRotation() {
        return rotation;
    }
}
