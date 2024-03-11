package animation;

import animation.Pose;
import engine.math.Matrix4f;
import engine.objects.GameObject;
import org.ode4j.math.DQuaternion;
import org.ode4j.math.DVector3;

import java.util.ArrayList;
import java.util.List;

public class Bone {
    public int index;
    public String name;
    GameObject object;
    public List<Bone> children = new ArrayList<>();
    private Matrix4f animatedTransform = Matrix4f.identity();
    private Matrix4f defaultLocalTransform;
    private Matrix4f defaultInverseTransform = Matrix4f.identity();

    public Bone(int index, Matrix4f defaultLocalTransform,GameObject object) {
        this.index = index;
        this.object = object;
        this.defaultLocalTransform = defaultLocalTransform;
        animatedTransform = defaultLocalTransform;
    }

    public GameObject getObject(){
        return object;
    }
    public int getIndex(){return index;}
    public void addChild(Bone child){
        this.children.add(child);
    }
    public List<Bone> getChildren(){
        return children;
    }

    public Matrix4f getAnimatedTransform() {
        return animatedTransform;
    }
    public void setAnimationTransform(Matrix4f animationTransform) {
        this.animatedTransform = animationTransform;
    }

    public Matrix4f getDefaultInverseTransform() {
        return defaultInverseTransform;
    }
    public void calcDefaultInverseTransform (Matrix4f defaultParentTransform) {
        Matrix4f defaultTransform = Matrix4f.multiply(defaultParentTransform, defaultLocalTransform);
        defaultInverseTransform = defaultTransform.invert(defaultTransform);
        if (!children.isEmpty()){
            for (int i = 0; i< children.size();i++) {
                children.get(i).calcDefaultInverseTransform(defaultTransform);
            }
        }
    }
}
