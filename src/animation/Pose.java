package animation;

import engine.graphics.Mesh;
import engine.math.Matrix4f;
import org.ode4j.math.DVector3;
import org.ode4j.math.DQuaternion;

public class Pose {
    //Pose of a part of the model
    DQuaternion Rotation = new DQuaternion(0,0,0,0);
    DVector3 Translation = new DVector3(0,0,0);
    public Pose(DVector3 translation, DQuaternion rotation) {
        this.Translation = translation;
        this.Rotation = rotation;
    }

    public DQuaternion getRotation() {
        return Rotation;
    }

    public DVector3 getTranslation() {
        return Translation;
    }

    public Matrix4f getLocalTransform(){
        Matrix4f matrix = Matrix4f.identity();
        matrix = Matrix4f.translate(Translation);
        matrix = Matrix4f.multiply(matrix,toRotationMatrix(Rotation));
        //System.out.println(Matrix4f.toString(matrix));
        return matrix;
    }

    private Matrix4f toRotationMatrix(DQuaternion rotation) {
        Matrix4f matrix = Matrix4f.identity();
        final float xy = (float) (rotation.get1() * rotation.get2());
        final float xz = (float) (rotation.get1() * rotation.get3());
        final float xw = (float) (rotation.get1() * rotation.get0());
        final float yz = (float) (rotation.get2() * rotation.get3());
        final float yw = (float) (rotation.get2() * rotation.get0());
        final float zw = (float) (rotation.get3() * rotation.get0());
        final float xSquared = (float) (rotation.get1() * rotation.get1());
        final float ySquared = (float) (rotation.get2() * rotation.get2());
        final float zSquared = (float) (rotation.get3() * rotation.get3());
        matrix.set(0,0,1 - 2 * (ySquared + zSquared));
        matrix.set(0,1, 2 * (xy - zw));
        matrix.set(0,2,2 * (xz + yw));
        matrix.set(0,3, 0);
        matrix.set(1,0,2 * (xy + zw));
        matrix.set(1,1,1 - 2 * (xSquared + zSquared));
        matrix.set(0,2, 2 * (yz - xw));
        matrix.set(1,3, 0);
        matrix.set(2,0, 2 * (xz - yw));
        matrix.set(2,1, 2 * (yz + xw));
        matrix.set(2,2,1 - 2 * (xSquared + ySquared));
        matrix.set(2,3,0);
        matrix.set(3,0, 0);
        matrix.set(3,1, 0);
        matrix.set(3,2, 0);
        matrix.set(3,3, 1);
        return matrix;
    }

    public static Pose interpolateFrame (Pose frameA, Pose frameB, float progression){

        DVector3 pos = interpolatePosition(frameA.getTranslation(), frameB.getTranslation(), progression);
        DQuaternion rot = interpolateRotation(frameA.getRotation(), frameB.getRotation(),progression);

        //System.out.println(frameA.getTranslation());
        //System.out.println(frameB.getTranslation());
        //System.out.println(pos);
        return new Pose(pos,rot);
    }

    public static DVector3 interpolatePosition(DVector3 start, DVector3 end, float progression){
        float x = (float)(start.get0()) + (float)((end.get0() - start.get0())) * progression;
        float y = (float)(start.get1()) + (float)((end.get1() - start.get1())) * progression;
        float z = (float)(start.get2()) + (float)((end.get2() - start.get2())) * progression;
        return new DVector3(x, y, z);
    }

    public static DQuaternion interpolateRotation(DQuaternion a, DQuaternion b, float blend) {
        DQuaternion result = new DQuaternion(0, 0, 0, 1);
        float dot = (float) (a.get0() * b.get0() + a.get1() * b.get1() + a.get2() * b.get2() + a.get3() * b.get3());
        float blendI = 1f - blend;
        System.out.println(blendI);
        if (dot < 0) {
            result.set0(blendI * a.get0() + blend * -b.get0());
            result.set1( blendI * a.get1() + blend * -b.get1());
            result.set2( blendI * a.get2() + blend * -b.get2());
            result.set3( blendI * a.get3() + blend * -b.get3());
        } else {
            result.set0( blendI * a.get0() + blend * b.get0());
            result.set1( blendI * a.get1() + blend * b.get1());
            result.set2( blendI * a.get2() + blend * b.get2());
            result.set3( blendI * a.get3() + blend * b.get3());
        }

        result.safeNormalize4();
        return result;
    }
}
