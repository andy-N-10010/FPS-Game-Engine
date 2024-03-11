package animation;

import java.util.ArrayList;
import java.util.List;
import animation.Bone;
import engine.io.Window;
import engine.math.Matrix4f;
import gameLoop.Input;
import org.lwjgl.glfw.GLFW;
import org.ode4j.math.DQuaternion;
import org.ode4j.math.DVector3;
import animation.animation;

public class Skeleton {
    Bone rootBone;
    int boneCount;
    public animation animator;
    public Skeleton(int boneCount, Bone rootBone){
        this.rootBone = rootBone;
        this.boneCount = boneCount;
        this.animator = new animation(this);
        rootBone.calcDefaultInverseTransform(Matrix4f.identity());

    }
    public Bone getRootBone(){
        return rootBone;
    }
    public int getBoneCount(){return boneCount;}
    public animation getAnimator(){
        return animator;
    }

    public void doAnimation(Clip clip){
        animator.startClip(clip);
    }

    public void update(){
        animator.updateDelta();
        animator.update();
    }

    public List <Matrix4f> getBoneTransforms() {
        List <Matrix4f> boneMatrices = new ArrayList<Matrix4f>();
        addBonesToArray(rootBone, boneMatrices);
        return boneMatrices;
    }

    private void addBonesToArray(Bone headBone, List <Matrix4f> boneMatrices) {
        boneMatrices.add(headBone.getAnimatedTransform());
        if (!headBone.children.isEmpty()){
            for (int i = 0; i< headBone.children.size();i++) {
                addBonesToArray(headBone.children.get(i), boneMatrices);
            }
        }
    }

}
