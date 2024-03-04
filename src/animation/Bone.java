package animation;

import animation.Pose;

import java.util.List;

public class Bone {
    Pose defaultPose;
    String name;
    int parent;
    List<Integer> children;
}
