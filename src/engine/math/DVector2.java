package engine.math;

public class DVector2 {
    private float x, y;

    public DVector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static DVector2 add(DVector2 vector1, DVector2 vector2) {
        return new DVector2(vector1.getX() + vector2.getX(), vector1.getY() + vector2.getY());
    }

    public static DVector2 subtract(DVector2 vector1, DVector2 vector2) {
        return new DVector2(vector1.getX() - vector2.getX(), vector1.getY() - vector2.getY());
    }

    public static DVector2 multiply(DVector2 vector1, DVector2 vector2) {
        return new DVector2(vector1.getX() * vector2.getX(), vector1.getY() * vector2.getY());
    }

    public static DVector2 divide(DVector2 vector1, DVector2 vector2) {
        return new DVector2(vector1.getX() / vector2.getX(), vector1.getY() / vector2.getY());
    }

    public static float length(DVector2 vector) {
        return (float) Math.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY());
    }

    public static DVector2 normalize(DVector2 vector) {
        float len = DVector2.length(vector);
        return DVector2.divide(vector, new DVector2(len, len));
    }

    public static float dot(DVector2 vector1, DVector2 vector2) {
        return vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(x);
        result = prime * result + Float.floatToIntBits(y);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DVector2 other = (DVector2) obj;
        if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
            return false;
        if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
            return false;
        return true;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
