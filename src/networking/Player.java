package networking;

import org.ode4j.math.DVector3;

public class Player {

    private final DVector3 position;
    private final DVector3 serverPosition;

    private final String username;

    private float pastTime;

    public Player(final String username) {
        this.position = new DVector3();
        this.serverPosition = new DVector3();

        this.username = username;
    }

    public void render() {

    }

    public void update(final float delta) {
        this.pastTime += delta;

        //final DVector3 interpolate = this.position.interpolate(this.serverPosition)

    }
}