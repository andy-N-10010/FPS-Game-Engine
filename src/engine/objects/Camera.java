package engine.objects;

import engine.math.DVector2;
import engine.math.Vector3f;
import gameLoop.Input;
import org.lwjgl.glfw.GLFW;
import org.ode4j.math.DVector3;

import org.lwjgl.glfw.GLFW;



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

        float x = (float) Math.sin(Math.toRadians(rotation.get0())) * moveSpeed;
        float z = (float) Math.cos(Math.toRadians(rotation.get2())) * moveSpeed;

        if (Input.isKeyDown(GLFW.GLFW_KEY_A)) position = position.set2(-0.5f);
        if (Input.isKeyDown(GLFW.GLFW_KEY_D)) position = position.set2( 0.5f);
        if (Input.isKeyDown(GLFW.GLFW_KEY_W)) position = position.set0(0.5f);
        if (Input.isKeyDown(GLFW.GLFW_KEY_S)) position = position.set0(-0.5f);
        if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) position = position.set1(moveSpeed);
        if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) position = position.set1(-moveSpeed);

        float dx = (float) (newMouseX - oldMouseX);
        float dy = (float) (newMouseY - oldMouseY);

        rotation.set(-dy * mouseSensitivity,  -dx * mouseSensitivity, 0);

        //rotation = rotation.add(rotation, new DVector3(-dy * mouseSensitivity, -dx * mouseSensitivity, 0));

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
