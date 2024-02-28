package collision;

import engine.objects.GameObject;
import org.ode4j.math.DVector3;

public class MyOBB extends MyShape{
    private DVector3 center; // center of OBB
    private DVector3[] localAxis; // "local" xyz
    private float[] axisRadius;  // each local axis's radius

    public MyOBB(GameObject object, DVector3 center, DVector3[] localAxis, float[] axisRadius) {
        super(object);
        this.center = center;
        this.localAxis = localAxis;
        this.axisRadius = axisRadius;
        for (float r: axisRadius
             ) {
            System.out.println(r);
        }
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

    public DVector3[] getVertices()
    {
        // an array of 8
        DVector3[] vertices = new DVector3[8];
        int i = 0;

        // to distinguish negative and positive x (also for y and z)
        for (int x = -1; x <= 1; x += 2)
        {
            for (int y = -1; y <= 1; y += 2)
            {
                for (int z = -1; z <= 1; z += 2)
                {
                    // the copy of center
                    DVector3 vertex = new DVector3(center);
                    // the copy of three local axes for scaling and adding to the center
                    DVector3 axisScaledX = new DVector3(localAxis[0]);
                    DVector3 axisScaledY = new DVector3(localAxis[1]);
                    DVector3 axisScaledZ = new DVector3(localAxis[2]);
                    // scale all direction's distance by multiplying each axis's radius
                    axisScaledX.scale(x * axisRadius[0]);
                    axisScaledY.scale(y * axisRadius[1]);
                    axisScaledZ.scale(z * axisRadius[2]);
                    vertex.add(axisScaledX);
                    vertex.add(axisScaledY);
                    vertex.add(axisScaledZ);
                    // add the vertex to the array
                    vertices[i++] = vertex;
                }
            }
        }

        return vertices;
    }

    public boolean intersects(MyOBB other)
    {
        DVector3[] axes = new DVector3[]{
                // this OBB's local axes
                localAxis[0],
                localAxis[1],
                localAxis[2],
                // other OBB's axes
                other.getLocalAxis()[0],
                other.getLocalAxis()[1],
                other.getLocalAxis()[2],
                // cross product
                localAxis[0].cross(other.getLocalAxis()[0]),
                localAxis[0].cross(other.getLocalAxis()[1]),
                localAxis[0].cross(other.getLocalAxis()[2]),
                localAxis[1].cross(other.getLocalAxis()[0]),
                localAxis[1].cross(other.getLocalAxis()[1]),
                localAxis[1].cross(other.getLocalAxis()[2]),
                localAxis[2].cross(other.getLocalAxis()[0]),
                localAxis[2].cross(other.getLocalAxis()[1]),
                localAxis[2].cross(other.getLocalAxis()[2])
        };

        for (DVector3 axe : axes) {
            if (NotInteractiveOBB(getVertices(), other.getVertices(), axe)) {
                return false;
            }
        }
        return true;
    }

    private boolean NotInteractiveOBB(DVector3[] vertices1, DVector3[] vertices2, DVector3 axis)
    {
        float[] limit1 = GetProjectionLimit(vertices1, axis);
        float[] limit2 = GetProjectionLimit(vertices2, axis);
        return limit1[0] > limit2[1] || limit2[0] > limit1[1];
    }

    private float[] GetProjectionLimit(DVector3[] vertices, DVector3 axis)
    {
        float[] result = { 999999, -999999 };
        for (DVector3 vertex : vertices) {
            float dot = (float) vertex.dot(axis);
            result[0] = Math.min(dot, result[0]);
            result[1] = Math.max(dot, result[1]);
        }
        return result;
    }

    public boolean intersects(MySphere sphere) {
        DVector3 sphereCenter = sphere.getCenter();
        double sphereRadius = sphere.getRadius();

        // calculate the sphere center on the OBB. find the closest point on OBB
        DVector3 pointInOBB = getClosestPoint(sphereCenter);

        // calculate the distance between this point and the sphere center
        double distance = sphereCenter.distance(pointInOBB);

        return distance <= sphereRadius;
    }

    public DVector3 getClosestPoint(DVector3 point) {
        DVector3 result = new DVector3(point);

        // use OBB's local coordinate
        DVector3 localPoint = point.sub(center);

        for (int i = 0; i < 3; ++i) {
            double distance = localPoint.dot(getLocalAxis()[i]);

            // limit the distance so that the point is still on OBB (won't larger than the radius on this axis)
            if (distance > axisRadius[i]) {
                distance = axisRadius[i];
            }
            if (distance < -axisRadius[i]) {
                distance = -axisRadius[i];
            }

            // move the point to the boundary
            result.add(getLocalAxis()[i].scale(distance));
        }

        return result;
    }

    @Override
    public void update() {
        center = new DVector3(getObject().getPosition());
        localAxis[0] = new DVector3(getObject().getRight());
        localAxis[1] = new DVector3(getObject().getUp());
        localAxis[2] = new DVector3(getObject().getForward());
    }
}
