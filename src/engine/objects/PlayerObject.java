package engine.objects;

import engine.graphics.Mesh;
import org.ode4j.math.DVector3;

public class PlayerObject extends GameObject{
    Camera camera;

    public PlayerObject(DVector3 position, DVector3 rotation, DVector3 scale, Mesh mesh) {
        super(position, rotation, scale, mesh);
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera() {
        return camera;
    }
}
