package animation;

import animation.Pose;
import java.util.ArrayList;
import java.util.List;

public class JointPose {
    //All of the Poses for a Single Keyframe and their time in the animation
    float timeStamp;
    List<Pose> poses = new ArrayList<Pose>();

    public JointPose (float timeStamp, List<Pose> keyFrame){
        this.timeStamp = timeStamp;
        this.poses = keyFrame;
    }

    public float getTimeStamp(){
        return timeStamp;
    }

    public List<Pose> getPoses() {
        return poses;
    }
}
