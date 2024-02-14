package collision;

public class Projection {
    // projection's minimum
    private double min;
    // projection's maximum
    private double max;

    public Projection(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public boolean overlaps(Projection other) {
        return this.max >= other.min && this.min <= other.max;
    }
}
