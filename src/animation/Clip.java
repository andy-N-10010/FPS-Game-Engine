package animation;

import java.util.List;
import animation.JointPose;
import engine.math.Matrix4f;

public class Clip {
    //Animation, List of Key Frames and the total time

    float length;
    List<JointPose> keyFrames;

    public Clip(float lengthSec, List<JointPose> frames){
        this.keyFrames = frames;
        this.length = lengthSec;
    }

    public float getLength(){
        return length;
    }
    public List<JointPose> getKeyFrames(){
        return keyFrames;
    }

}
