package animation;

import java.util.List;
import animation.JointPose;

public class Clip {
    List<JointPose> keyPoses;
    List<Double> times;
    double globalTimeAtStart;
    double localTime;
    double timeSpeed;
    JointPose update(double t) {
        return null;
    }
}
