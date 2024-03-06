package engine.objects;

import collision.*;
import engine.graphics.Mesh;

import org.ode4j.math.DQuaternion;
import org.ode4j.math.DVector3;
import org.ode4j.ode.DBody;
import org.ode4j.ode.DMass;

public class GameObject {
    private DVector3 position, rotationEuler, scale;
    private DQuaternion rotationQuaternion;
    private DVector3 right, up, forward;
    private Mesh mesh;
    public DBody body;
    public DMass mass;
    //private MyShape collider;
    private MyAABB myAABB;
    private MySphere mySphere;
    private MyOBB myOBB;
    private double temp;

    public GameObject(DVector3 position, DVector3 rotation, DVector3 scale, Mesh mesh) {
        this.position = position;
        this.rotationEuler = new DVector3(rotation.get0() % 360, rotation.get1() % 360, rotation.get2() % 360);
        this.scale = scale;
        this.mesh = mesh;
        this.rotationQuaternion = eulerToQuaternion(rotation);
        updateXYZ();
    }

    public void generateAABB() {
        myAABB = new MyAABB(this,
                position.get0() - scale.get0() / 2, position.get0() + scale.get0() / 2,
                position.get1() - scale.get1() / 2, position.get1() + scale.get1() / 2,
                position.get2() - scale.get2() / 2, position.get2() + scale.get2() / 2);
    }

    public void generateOBB() {
        System.out.println(right);
        myOBB = new MyOBB(this, new DVector3(position), new DVector3[]{right, up, forward},
                new float[]{(float) scale.get0() / 2, (float) scale.get1() / 2, (float) scale.get2() / 2});
    }

    public void generateSphere() {
        mySphere = new MySphere(this, new DVector3(position) ,(float) scale.get0());
    }

    public void update() {
        updateXYZ();

        rotationQuaternion = eulerToQuaternion(rotationEuler);
        //temp += 0.02;
        //position.set0((float) Math.sin(temp));
        //rotation.set((float) Math.sin(temp) * 360, (float) Math.sin(temp) * 360, (float) Math.sin(temp) * 360);
        //scale.set((float) Math.sin(temp), (float) Math.sin(temp), (float) Math.sin(temp));
    }

    public MyAABB getMyAABB() {
        return myAABB;
    }

    public MySphere getMySphere() {
        return mySphere;
    }

    public MyOBB getMyOBB() {
        return myOBB;
    }

    public DVector3 getPosition() {
        return position;
    }

    public DVector3 getRotationEuler() {
        return rotationEuler;
    }

    public DVector3 getScale() {
        return scale;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void updateXYZ() {
        right = quaternionMulVec3(rotationQuaternion, new DVector3(1, 0, 0));
        up = quaternionMulVec3(rotationQuaternion, new DVector3(0, 1, 0));
        forward = quaternionMulVec3(rotationQuaternion, new DVector3(0, 0, 1));
    }

    public void setPosition(DVector3 v) {
        position = new DVector3(v);
    }

    public DVector3 getRight() {
        return right;
    }

    public DVector3 getUp() {
        return up;
    }

    public DVector3 getForward() {
        return forward;
    }

    public DQuaternion eulerToQuaternion(DVector3 rotationEuler) {
        double ex = rotationEuler.get0() % 360;
        double ey = rotationEuler.get1() % 360;
        double ez = rotationEuler.get2() % 360;

        double qx = Math.sin(ex/2) * Math.cos(ey/2) * Math.cos(ez/2) - Math.cos(ex/2) * Math.sin(ey/2) * Math.sin(ez/2);
        double qy = Math.cos(ex/2) * Math.sin(ey/2) * Math.cos(ez/2) + Math.sin(ex/2) * Math.cos(ey/2) * Math.sin(ez/2);
        double qz = Math.cos(ex/2) * Math.cos(ey/2) * Math.sin(ez/2) - Math.sin(ex/2) * Math.sin(ey/2) * Math.cos(ez/2);
        double qw = Math.cos(ex/2) * Math.cos(ey/2) * Math.cos(ez/2) + Math.sin(ex/2) * Math.sin(ey/2) * Math.sin(ez/2);

        DQuaternion quaternion = new DQuaternion();
        quaternion.set(qx, qy, qz, qw);

        return quaternion;
    }

    public DVector3 quaternionMulVec3(DQuaternion rotationQuaternion, DVector3 point)
    {
        double num1 = rotationQuaternion.get0() * 2f;
        double num2 = rotationQuaternion.get1() * 2f;
        double num3 = rotationQuaternion.get2() * 2f;
        double num4 = rotationQuaternion.get0() * num1;
        double num5 = rotationQuaternion.get1() * num2;
        double num6 = rotationQuaternion.get2() * num3;
        double num7 = rotationQuaternion.get0() * num2;
        double num8 = rotationQuaternion.get0() * num3;
        double num9 = rotationQuaternion.get1() * num3;
        double num10 = rotationQuaternion.get3() * num1;
        double num11 = rotationQuaternion.get3() * num2;
        double num12 = rotationQuaternion.get3() * num3;
        DVector3 result = new DVector3();
        result.set0((1f - (num5 + num6)) * point.get0() + (num7 - num12) * point.get1() + (num8 + num11) * point.get2());
        result.set1((num7 + num12) * point.get0() + (1f - (num4 + num6)) * point.get1() + (num9 - num10) * point.get2());
        result.set2((num8 - num11) * point.get0() + (num9 + num10) * point.get1() + (1f - (num4 + num5)) * point.get2());
        return result;
    }
}