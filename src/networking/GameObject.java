package networking;

import org.ode4j.math.DVector3;

public class GameObject {

    private final DVector3 position;
    private final DVector3 serverPosition;

    //private final String username;

    public String name;

    public int id;
    public float x,y,z;

    public GameObject() {
        this.position = new DVector3();
        this.serverPosition = new DVector3();

        //this.username = username;
    }


}
