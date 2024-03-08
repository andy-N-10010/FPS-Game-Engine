package engine.objects;

import collision.MyOBB;
import gameLoop.Input;
import org.lwjgl.glfw.GLFW;
import org.ode4j.math.DVector3;

public class PlayerCamera extends Camera{
    private GameObject playerObj;
    private MyOBB playerOBB;

    public PlayerCamera(DVector3 position, DVector3 rotation, GameObject obj, MyOBB obb) {
        super(position, rotation);
        this.playerObj = obj;
        this.playerOBB = obb;
    }

    public MyOBB getPlayerOBB() {
        return playerOBB;
    }

    public void setPlayerOBB(MyOBB playerOBB) {
        this.playerOBB = playerOBB;
    }

    public GameObject getPlayerObj() {
        return playerObj;
    }

    public void setPlayerObj(GameObject playerObj) {
        this.playerObj = playerObj;
    }

    @Override
    public void update() {
        setNewMouseX(Input.getMouseX());
        setNewMouseY(Input.getMouseY());

        float x = (float) Math.sin(Math.toRadians(getRotation().get1())) * getMoveSpeed();
        float z = (float) Math.cos(Math.toRadians(getRotation().get1())) * getMoveSpeed();

        if (Input.isKeyDown(GLFW.GLFW_KEY_A)) {
            getPosition().add(new DVector3(-z,0,x));
            playerObj.getPosition().add(new DVector3(-z,0,x));
            playerOBB.update();
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_D)) {
            getPosition().add(new DVector3(z,0,-x));
            playerObj.getPosition().add(new DVector3(z,0,-x));
            playerOBB.update();
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_W)) {
            getPosition().add(new DVector3(-x,0,-z));
            playerObj.getPosition().add(new DVector3(-x,0,-z));
            playerOBB.update();
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_S)) {
            getPosition().add(new DVector3(x,0,z));
            playerObj.getPosition().add(new DVector3(x,0,z));
            playerOBB.update();
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
            playerObj.jump();
        }

        float dx = (float) (getNewMouseX() - getOldMouseX());
        float dy = (float) (getNewMouseY() - getOldMouseY());

        getRotation().add(new DVector3(-dy * getMouseSensitivity(), -dx * getMouseSensitivity(), 0));

        setOldMouseX(getNewMouseX());
        setOldMouseY(getNewMouseY());
    }
}
