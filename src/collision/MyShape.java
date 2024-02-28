package collision;

import engine.objects.GameObject;

public abstract class MyShape {
    private GameObject object;

    public MyShape(GameObject object) {
        this.object = object;
    }

    public GameObject getObject() {
        return object;
    }

    public abstract void update();
}
