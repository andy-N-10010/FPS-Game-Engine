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

    public DVector3[] getVertices() {
        // an array of 8
        DVector3[] vertices = new DVector3[8];
        int i = 0;

        // to distinguish negative and positive x (also for y and z)
        for (int x = -1; x <= 1; x += 2) {
            for (int y = -1; y <= 1; y += 2) {
                for (int z = -1; z <= 1; z += 2) {
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

    public boolean intersects(MyOBB other) {
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

        for (DVector3 axis : axes) {
            if (axis.lengthSquared() < 1e-7) {
                System.out.println("Warning: axis value is not illegal");
                continue;
            }

            Projection p1 = projectOntoAxis(this, axis);
            Projection p2 = projectOntoAxis(other, axis);

            if (!p1.overlaps(p2)) {
                return false;
            }
        }

        return true;
    }

    private Projection projectOntoAxis(MyOBB obb, DVector3 axis) {
        double min = axis.dot(obb.getLocalAxis()[0]);
        double max = min;

        for (int i = 1; i < obb.getLocalAxis().length; i++) {
            double value = axis.dot(obb.getLocalAxis()[i]);
            min = Math.min(min, value);
            max = Math.max(max, value);
        }

        return new Projection(min, max);
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
}
