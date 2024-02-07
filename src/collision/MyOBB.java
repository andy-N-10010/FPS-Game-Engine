package collision;

import org.ode4j.math.DVector3;

public class MyOBB {
    private DVector3 center; // center of OBB
    private DVector3[] localAxis; // "local" xyz
    private float[] axisRadius;  // each local axis's radius

    public MyOBB(DVector3 center, DVector3[] localAxis, float[] axisRadius) {
        this.center = center;
        this.localAxis = localAxis;
        this.axisRadius = axisRadius;
    }

    public DVector3 getCenter() {
        return center;
    }

    public void setCenter(DVector3 center) {
        this.center = center;
    }

    public DVector3[] getLocalAxis() {
        return localAxis;
    }

    public void setLocalX(DVector3 localX) {
        this.localAxis[0] = localX;
    }

    public void setLocalY(DVector3 localY) {
        this.localAxis[1] = localY;
    }

    public void setLocalZ(DVector3 localZ) {
        this.localAxis[2] = localZ;
    }

    public float[] getAxisRadius() {
        return axisRadius;
    }

    public void setLocalXRadius(float localXRadius) {
        this.axisRadius[0] = localXRadius;
    }

    public void setLocalYRadius(float localYRadius) {
        this.axisRadius[1] = localYRadius;
    }

    public void setLocalZRadius(float localZRadius) {
        this.axisRadius[2] = localZRadius;
    }


}
