package collision;

import org.ode4j.math.DVector3;
import org.ode4j.ode.DAABBC;

public class MyAABB implements DAABBC {
    double min0;
    double max0;
    double min1;
    double max1;
    double min2;
    double max2;

    public MyAABB(double min0, double max0, double min1, double max1, double min2, double max2) {
        this.min0 = min0;
        this.max0 = max0;
        this.min1 = min1;
        this.max1 = max1;
        this.min2 = min2;
        this.max2 = max2;
    }

    @Override
    public double getMin0() {
        return min0;
    }

    @Override
    public double getMin1() {
        return min1;
    }

    @Override
    public double getMin2() {
        return min2;
    }

    @Override
    public double getMax0() {
        return max0;
    }

    @Override
    public double getMax1() {
        return max1;
    }

    @Override
    public double getMax2() {
        return max2;
    }

    @Override
    public boolean isDisjoint(DAABBC daabbc) {
        return false;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public double len0() {
        return max0 - min0;
    }

    @Override
    public double len1() {
        return max1 - min1;
    }

    @Override
    public double len2() {
        return max2 - min2;
    }

    @Override
    public double getMax(int i) {
        return 0;
    }

    @Override
    public double getMin(int i) {
        return 0;
    }

    @Override
    public DVector3 getLengths() {
        return new DVector3(len0(), len1(), len2());
    }

    @Override
    public DVector3 getCenter() {
        return new DVector3((max0 + min0) / 2, (max1 + min1) / 2, (max2 + min2) / 2);
    }
}
