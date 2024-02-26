package engine.objects;

import engine.graphics.Mesh;

import org.ode4j.math.DVector3;

public class GameObject {
    private DVector3 position, rotation, scale;
    private Mesh mesh;
    private double temp;

    public GameObject(DVector3 position, DVector3 rotation, DVector3 scale, Mesh mesh) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.mesh = mesh;
    }

    public void update() {
        //temp += 0.02;
        //position.set0((float) Math.sin(temp));
        //rotation.set((float) Math.sin(temp) * 360, (float) Math.sin(temp) * 360, (float) Math.sin(temp) * 360);
        //scale.set((float) Math.sin(temp), (float) Math.sin(temp), (float) Math.sin(temp));
        position.set2(position.get2() - 0.05f);
    }

    public DVector3 getPosition() {
        return position;
    }

    public DVector3 getRotation() {
        return rotation;
    }

    public DVector3 getScale() {
        return scale;
    }

    public Mesh getMesh() {
        return mesh;
    }
}