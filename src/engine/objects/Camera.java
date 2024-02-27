package engine.objects;

import engine.math.Vector3f;
import gameLoop.Input;
import org.lwjgl.glfw.GLFW;
import org.ode4j.math.DVector3;

public class Camera {
    private DVector3 position, rotation;
    private float moveSpeed;

    public Camera(DVector3 position, DVector3 rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public void update() {
        //if (Input.isKeyDown(GLFW.GLFW_KEY_A)) position = DVector3.add(position,new DVector3(-moveSpeed,0,0));
    }

    public DVector3 getPosition() {
        return position;
    }

    public DVector3 getRotation() {
        return rotation;
    }
}
