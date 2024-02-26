package engine.math;

import org.ode4j.math.DVector3;

public class Matrix4f {
    public static final int SIZE = 4;
    private float[] elements = new float[SIZE * SIZE];

    public static Matrix4f identity() {
        Matrix4f result = new Matrix4f();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                result.set(i, j, 0);
            }
        }

        result.set(0, 0, 1);
        result.set(1, 1, 1);
        result.set(2, 2, 1);
        result.set(3, 3, 1);

        return result;
    }

    public static Matrix4f translate(DVector3 translate) {
        Matrix4f result = Matrix4f.identity();

        result.set(3, 0, (float)(translate.get0()));
        result.set(3, 1, (float)(translate.get1()));
        result.set(3, 2, (float)(translate.get2()));

        return result;
    }

    public static Matrix4f rotate(float angle, DVector3 axis) {
        Matrix4f result = Matrix4f.identity();

        float cos = (float) Math.cos(Math.toRadians(angle));
        float sin = (float) Math.sin(Math.toRadians(angle));
        float C = 1 - cos;

        result.set(0, 0, (float)(cos + axis.get0() * axis.get0() * C));
        result.set(0, 1, (float)(axis.get0() * axis.get1() * C - axis.get2() * sin));
        result.set(0, 2, (float)(axis.get0() * axis.get2() * C + axis.get1() * sin));
        result.set(1, 0, (float)(axis.get1() * axis.get0() * C + axis.get2() * sin));
        result.set(1, 1, (float)(cos + axis.get1() * axis.get1() * C));
        result.set(1, 2, (float)(axis.get1() * axis.get2() * C - axis.get0() * sin));
        result.set(2, 0, (float)(axis.get2() * axis.get0() * C - axis.get1() * sin));
        result.set(2, 1, (float)(axis.get2() * axis.get1() * C + axis.get0() * sin));
        result.set(2, 2, (float)(cos + axis.get2() * axis.get2() * C));

        return result;
    }

    public static Matrix4f scale(DVector3 scalar) {
        Matrix4f result = Matrix4f.identity();

        result.set(0, 0, (float)(scalar.get0()));
        result.set(1, 1, (float)(scalar.get1()));
        result.set(2, 2, (float)(scalar.get2()));

        return result;
    }

    public static Matrix4f transform(DVector3 position, DVector3 rotation, DVector3 scale) {
        Matrix4f result = Matrix4f.identity();

        Matrix4f translationMatrix = Matrix4f.translate(position);
        Matrix4f rotXMatrix = Matrix4f.rotate((float)(rotation.get0()), new DVector3(1, 0, 0));
        Matrix4f rotYMatrix = Matrix4f.rotate((float)(rotation.get1()), new DVector3(0, 1, 0));
        Matrix4f rotZMatrix = Matrix4f.rotate((float)(rotation.get2()), new DVector3(0, 0, 1));
        Matrix4f scaleMatrix = Matrix4f.scale(scale);

        Matrix4f rotationMatrix = Matrix4f.multiply(rotXMatrix, Matrix4f.multiply(rotYMatrix, rotZMatrix));

        result = Matrix4f.multiply(translationMatrix, Matrix4f.multiply(rotationMatrix, scaleMatrix));

        return result;
    }

    public static Matrix4f projection(float fov, float aspect, float near, float far) {
        Matrix4f result = Matrix4f.identity();

        float tanFOV = (float) Math.tan(Math.toRadians(fov / 2));
        float range = far - near;

        result.set(0, 0, 1.0f / (aspect * tanFOV));
        result.set(1, 1, 1.0f / tanFOV);
        result.set(2, 2, -((far + near) / range));
        result.set(2, 3, -1.0f);
        result.set(3, 2, -((2 * far * near) / range));
        result.set(3, 3, 0.0f);

        return result;
    }

    public static Matrix4f view(DVector3 position, DVector3 rotation) {
        Matrix4f result = Matrix4f.identity();

        DVector3 negative = new DVector3(-position.get0(), -position.get1(), -position.get2());
        Matrix4f translationMatrix = Matrix4f.translate(negative);
        Matrix4f rotXMatrix = Matrix4f.rotate((float)(rotation.get0()), new DVector3(1, 0, 0));
        Matrix4f rotYMatrix = Matrix4f.rotate((float)(rotation.get1()), new DVector3(0, 1, 0));
        Matrix4f rotZMatrix = Matrix4f.rotate((float)(rotation.get2()), new DVector3(0, 0, 1));

        Matrix4f rotationMatrix = Matrix4f.multiply(rotYMatrix, Matrix4f.multiply(rotZMatrix, rotXMatrix));

        result = Matrix4f.multiply(translationMatrix, rotationMatrix);

        return result;
    }

    public static Matrix4f multiply(Matrix4f matrix, Matrix4f other) {
        Matrix4f result = Matrix4f.identity();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                result.set(i, j, matrix.get(i, 0) * other.get(0, j) +
                        matrix.get(i, 1) * other.get(1, j) +
                        matrix.get(i, 2) * other.get(2, j) +
                        matrix.get(i, 3) * other.get(3, j));
            }
        }

        return result;
    }

    public float get(int x, int y) {
        return elements[y * SIZE + x];
    }

    public void set(int x, int y, float value) {
        elements[y * SIZE + x] = value;
    }

    public float[] getAll() {
        return elements;
    }
}
