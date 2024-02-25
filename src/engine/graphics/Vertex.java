package engine.graphics;

import org.ode4j.math.DVector3;

public class Vertex {
    private DVector3 position, color;

    public Vertex(DVector3 position, DVector3 color) {
        this.position = position;
        this.color = color;
    }

    public DVector3 getPosition() {
        return position;
    }

    public DVector3 getColor() {
        return color;
    }
}
