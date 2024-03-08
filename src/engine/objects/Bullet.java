package engine.objects;

import engine.graphics.Mesh;
import org.ode4j.math.DVector3;

public class Bullet extends GameObject{
    private GameObject target;

    public Bullet(DVector3 position, DVector3 rotation, DVector3 scale, Mesh mesh) {
        super(position, rotation, scale, mesh);
    }

    public GameObject getTarget() {
        return target;
    }

    public void setTarget(GameObject target) {
        this.target = target;
    }
}
