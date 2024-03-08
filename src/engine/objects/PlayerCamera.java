package engine.objects;

import collision.MyOBB;
import gameLoop.Input;
import org.ode4j.math.DVector3;

public class PlayerCamera extends Camera{
    private GameObject playerObj;
    private MyOBB playerOBB;
    float x, z, dx, dy;

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

        x = (float) Math.sin(Math.toRadians(getRotation().get1())) * getMoveSpeed();
        z = (float) Math.cos(Math.toRadians(getRotation().get1())) * getMoveSpeed();

        dx = (float) (getNewMouseX() - getOldMouseX());
        dy = (float) (getNewMouseY() - getOldMouseY());

        getRotation().add(new DVector3(-dy * getMouseSensitivity(), -dx * getMouseSensitivity(), 0));

        setOldMouseX(getNewMouseX());
        setOldMouseY(getNewMouseY());
    }

    public void moveBack() {
        playerObj.getBody().setPosition(new DVector3(playerObj.getBody().getPosition()).add(new DVector3(x,0, z)));
        getPosition().add(new DVector3(x,0, z));
        playerObj.getPosition().add(new DVector3(x,0, z));
        playerOBB.update();
    }

    public void moveForward() {
        playerObj.getBody().setPosition(new DVector3(playerObj.getBody().getPosition()).add(new DVector3(-x,0,-z)));
        getPosition().add(new DVector3(-x,0,-z));
        playerObj.getPosition().add(new DVector3(-x,0,-z));
        playerOBB.update();
    }

    public void moveRight() {
        playerObj.getBody().setPosition(new DVector3(playerObj.getBody().getPosition()).add(new DVector3(z,0,-x)));
        getPosition().add(new DVector3(z,0,-x));
        playerObj.getPosition().add(new DVector3(z,0,-x));
        playerOBB.update();
    }

    public void moveLeft() {
        playerObj.getBody().setPosition(new DVector3(playerObj.getBody().getPosition()).add(new DVector3(-z,0, x)));
        getPosition().add(new DVector3(-z,0, x));
        playerObj.getPosition().add(new DVector3(-z,0, x));
        playerOBB.update();
    }
}
