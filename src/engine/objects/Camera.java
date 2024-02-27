package engine.objects;

import engine.math.DVector2;
import gameLoop.Input;
import org.lwjgl.glfw.GLFW;
import org.ode4j.math.DVector3;




public class Camera {
    private DVector3 position, rotation;
    private float moveSpeed = 0.05f, mouseSensitivity = 0.15f;
    private double oldMouseX = 0, oldMouseY = 0, newMouseX, newMouseY;

    public Camera(DVector3 position, DVector3 rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public void update() {
        newMouseX = Input.getMouseX();
        newMouseY = Input.getMouseY();

        float x = (float) Math.sin(Math.toRadians(rotation.get1())) * moveSpeed;
        float z = (float) Math.cos(Math.toRadians(rotation.get1())) * moveSpeed;

        if (Input.isKeyDown(GLFW.GLFW_KEY_A)) position = position.add(new DVector3(-z,0,x));
        if (Input.isKeyDown(GLFW.GLFW_KEY_D)) position = position.add(new DVector3(z,0,-x));
        if (Input.isKeyDown(GLFW.GLFW_KEY_W)) position = position.add(new DVector3(-x,0,-z));
        if (Input.isKeyDown(GLFW.GLFW_KEY_S)) position = position.add(new DVector3(x,0,z));
        if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) position = position.add(new DVector3(0,moveSpeed,0));
        if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) position = position.add(new DVector3(0,-moveSpeed,0));

        float dx = (float) (newMouseX - oldMouseX);
        float dy = (float) (newMouseY - oldMouseY);

        rotation = rotation.add(new DVector3(-dy * mouseSensitivity, -dx * mouseSensitivity, 0));

        oldMouseX = newMouseX;
        oldMouseY = newMouseY;
    }

    public DVector3 getPosition() {
        return position;
    }

    public DVector3 getRotation() {
        return rotation;
    }
}
