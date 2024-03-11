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

        result = Matrix4f.multiply(scaleMatrix, Matrix4f.multiply(rotationMatrix, translationMatrix));

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

    public static String toString(Matrix4f dest) {
        float m00 = dest.get(0,0);
        float m11 = dest.get(1,1);
        float m01 = dest.get(0,1);
        float m10 = dest.get(1,0);
        float m12 = dest.get(1,2);
        float m02 = dest.get(0,2);
        float m13 = dest.get(1,3);
        float m03 = dest.get(0,3);
        float m20 = dest.get(2,0);
        float m31 = dest.get(3,1);
        float m21 = dest.get(2,1);
        float m30 = dest.get(3,0);
        float m32 = dest.get(3,2);
        float m22 = dest.get(2,2);
        float m33 = dest.get(3,3);
        float m23 = dest.get(2,3);
        return "" + m00 + " " + m10 + " " + m20 + " " + m30 + "\n"  + m01 + " " + m11 + " " + m21 + " " + m31 + "\n" + m02 + " " + m12 + " " + m22 + " " + m32 + "\n" + m03 + " " + m13 + " " + m23 + " " + m33 + "\n";
    }

    public Matrix4f invert(Matrix4f dest) {
        float m00 = dest.get(0,0);
        float m11 = dest.get(1,1);
        float m01 = dest.get(0,1);
        float m10 = dest.get(1,0);
        float m12 = dest.get(1,2);
        float m02 = dest.get(0,2);
        float m13 = dest.get(1,3);
        float m03 = dest.get(0,3);
        float m20 = dest.get(2,0);
        float m31 = dest.get(3,1);
        float m21 = dest.get(2,1);
        float m30 = dest.get(3,0);
        float m32 = dest.get(3,2);
        float m22 = dest.get(2,2);
        float m33 = dest.get(3,3);
        float m23 = dest.get(2,3);

        float a = m00 * m11 - m01 * m10;
        float b = m00 * m12 - m02 * m10;
        float c = m00 * m13 - m03 * m10;
        float d = m01 * m12 - m02 * m11;
        float e = m01 * m13 - m03 * m11;
        float f = m02 * m13 - m03 * m12;
        float g = m20 * m31 - m21 * m30;
        float h = m20 * m32 - m22 * m30;
        float i = m20 * m33 - m23 * m30;
        float j = m21 * m32 - m22 * m31;
        float k = m21 * m33 - m23 * m31;
        float l = m22 * m33 - m23 * m32;
        float det = a * l - b * k + c * j + d * i - e * h + f * g;
        det = 1.0f / det;
        Matrix4f result = new Matrix4f();
        result.set(0,0,( m11 * l - m12 * k + m13 * j) * det);
        result.set(0,1,(-m01 * l + m02 * k - m03 * j) * det);
        result.set(0,2, (m31 * f - m32 * e + m33 * d) * det);
        result.set(0,3,(-m21 * f + m22 * e - m23 * d) * det);
        result.set(1,0,(-m10 * l + m12 * i - m13 * h) * det);
        result.set(1,1, (m00 * l - m02 * i + m03 * h) * det);
        result.set(1,2,(-m30 * f + m32 * c - m33 * b) * det);
        result.set(1,3, (m20 * f - m22 * c + m23 * b) * det);
        result.set(2,0, (m10 * k - m11 * i + m13 * g) * det);
        result.set(2,1,(-m00 * k + m01 * i - m03 * g) * det);
        result.set(2,2, (m30 * e - m31 * c + m33 * a) * det);
        result.set(2,3,(-m20 * e + m21 * c - m23 * a) * det);
        result.set(3,0,(-m10 * j + m11 * h - m12 * g) * det);
        result.set(3,1,( m00 * j - m01 * h + m02 * g) * det);
        result.set(3,2,(-m30 * d + m31 * b - m32 * a) * det);
        result.set(3,3,( m20 * d - m21 * b + m22 * a) * det);
        return result;
    }



}
