package animation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import animation.Skeleton;
import engine.math.Matrix4f;
import gameLoop.Input;
import org.lwjgl.glfw.GLFW;
import animation.JointPose;

public class animation {
    Skeleton skeleton;
    Clip currentAnimation;
    float animationTime = 0;

    public float delta;
    public float currentFrameTime = 0;
    public float lastFrameTime = 0;

    public animation(Skeleton skeleton){
        this.skeleton = skeleton;
    }

    public void startClip(Clip animation){
        this.animationTime = 0;
        this.currentAnimation = animation;
    }

    public void updateDelta(){
        currentFrameTime = System.currentTimeMillis();
        delta = (currentFrameTime - lastFrameTime);
        lastFrameTime = currentFrameTime;
        //System.out.println(currentFrameTime);
    }

    public void update(){
        if(currentAnimation == null){
            return;
        }
        increaseAnimationTime();
        List<Matrix4f> currentPose = calculateCurrentAnimationPose();
        applyPoseToJoints(currentPose,skeleton.getRootBone(),Matrix4f.identity());
    }

    public void increaseAnimationTime(){
        animationTime += 0.01;
        if (animationTime > currentAnimation.getLength()){
            this.animationTime %= currentAnimation.getLength();
        }
    }

    public List<Matrix4f> calculateCurrentAnimationPose(){
        List<JointPose> frames = getPreviousAndNextFrames();
        float progression = calcProgression(frames.get(0), frames.get(1));
        //System.out.println(progression);
        return interpolatePoses(frames.get(0), frames.get(1),progression);
    }

    public void applyPoseToJoints(List<Matrix4f> currentPose,Bone bone, Matrix4f parentTransform){
        //System.out.println(Matrix4f.toString(currentPose.get(1)));
        Matrix4f currentLocalTransform = currentPose.get(bone.getIndex());
        //System.out.println(bone.getIndex());
        Matrix4f currentTransform = Matrix4f.multiply(parentTransform, currentLocalTransform);
        //System.out.println(Matrix4f.toString(parentTransform));
        for (Bone childBone : bone.getChildren()) {
            applyPoseToJoints(currentPose, childBone, currentTransform);
        }
        //System.out.println(Matrix4f.toString(currentTransform));
        //System.out.println(Matrix4f.toString(bone.getDefaultInverseTransform()));

        bone.setAnimationTransform(Matrix4f.multiply(currentTransform, bone.getDefaultInverseTransform()));
    }

    public List<JointPose> getPreviousAndNextFrames() {
        List<JointPose> allFrames = currentAnimation.getKeyFrames();
        JointPose previousFrame = allFrames.get(0);
        JointPose nextFrame = allFrames.get(0);
        for (int i = 0; i < allFrames.size(); i++) {
            nextFrame = allFrames.get(i);
            if (nextFrame.getTimeStamp() > animationTime) {
                break;
            }
            previousFrame = allFrames.get(i);
        }
        return Arrays.asList(previousFrame,nextFrame);
    }

    public float calcProgression(JointPose previousFrame, JointPose nextFrame) {
        float totalTime = nextFrame.getTimeStamp() - previousFrame.getTimeStamp();
        float currentTime = animationTime - previousFrame.getTimeStamp();
        return currentTime / totalTime;
    }

    public List<Matrix4f> interpolatePoses(JointPose previousFrame, JointPose nextFrame, float progression) {
        List<Matrix4f> currentPose = new ArrayList<Matrix4f>();
        for (int i = 0; i  < previousFrame.getPoses().size(); i++) {
            Pose previousTransform = previousFrame.getPoses().get(i);
            Pose nextTransform = nextFrame.getPoses().get(i);
            Pose currentTransform = Pose.interpolateFrame(previousTransform, nextTransform, progression);
            currentPose.add(currentTransform.getLocalTransform());
        }
        return currentPose;
    }


}
