package networking;

import org.ode4j.math.DVector3;

public class Player {

    private final DVector3 position;
    private final DVector3 serverPosition;

    //private final String username;

    public String username;

    public int id;
    public float x,y,z;

    private float pastTime;

    public Player() {
        this.position = new DVector3();
        this.serverPosition = new DVector3();

        //this.username = username;
    }

    public void render() {

    }

    public void update(final float delta) {
        this.pastTime += delta;

        //final DVector3 interpolate = this.position.interpolate(this.serverPosition)

    }
}
