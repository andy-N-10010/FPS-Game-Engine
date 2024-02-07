package collision;

import org.ode4j.math.DVector3;

public class MyAABB{
    private DVector3 center;
    // 0, 1, 2 represent x, y, z
    private double min0;
    private double max0;
    private double min1;
    private double max1;
    private double min2;
    private double max2;

    public MyAABB(DVector3 center, double min0, double max0, double min1, double max1, double min2, double max2) {
        this.center = center;
        this.min0 = min0;
        this.max0 = max0;
        this.min1 = min1;
        this.max1 = max1;
        this.min2 = min2;
        this.max2 = max2;
    }

    public DVector3 getCenter() {
        return center;
    }

    public double getMin0() {
        return min0;
    }

    public double getMin1() {
        return min1;
    }

    public double getMin2() {
        return min2;
    }

    public double getMax0() {
        return max0;
    }

    public double getMax1() {
        return max1;
    }

    public double getMax2() {
        return max2;
    }

    public void setCenter(DVector3 center) {
        this.center = center;
    }

    public void setMin0(double min0) {
        this.min0 = min0;
    }

    public void setMax0(double max0) {
        this.max0 = max0;
    }

    public void setMin1(double min1) {
        this.min1 = min1;
    }

    public void setMax1(double max1) {
        this.max1 = max1;
    }

    public void setMin2(double min2) {
        this.min2 = min2;
    }

    public void setMax2(double max2) {
        this.max2 = max2;
    }

    public double len0() {
        return max0 - min0;
    }

    public double len1() {
        return max1 - min1;
    }

    public double len2() {
        return max2 - min2;
    }

    public DVector3 getLengths() {
        return new DVector3(len0(), len1(), len2());
    }

    // intersect between AABB and AABB
    public boolean intersects(MyAABB other) {
        if (this.max0 < other.min0 || this.min0 > other.max0) return false;
        if (this.max1 < other.min1 || this.min1 > other.max1) return false;
        if (this.max2 < other.min2 || this.min2 > other.max2) return false;
        return true;
    }
}
