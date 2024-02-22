package engine.graphics;

import org.ode4j.math.DVector3;

public class Vertex {
    private DVector3 position;

    public Vertex(DVector3 position) {
        this.position = position;
    }

    public DVector3 getPosition() {
        return position;
    }
}
