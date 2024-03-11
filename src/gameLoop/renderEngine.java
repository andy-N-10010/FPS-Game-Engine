package gameLoop;

import animation.Pose;
import animation.Skeleton;
import animation.JointPose;
import animation.Clip;
import animation.Bone;

import com.esotericsoftware.kryonet.Client;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Shader;
import engine.graphics.Vertex;
import engine.math.Matrix4f;
import org.lwjgl.glfw.GLFW;
import engine.io.Window;
import org.ode4j.math.DQuaternion;
import org.ode4j.math.DVector3;
import engine.math.DVector2;
import engine.objects.GameObject;
import engine.objects.Camera;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class renderEngine implements Runnable{
    public Window window;
    public final int width = 1280, height = 760;
    public Renderer renderer;

    //Client from kryonet
    private Client client;

    public Shader shader;
    public Mesh mesh = new Mesh(new Vertex[] {
            //Back face
            new Vertex(new DVector3(-0.5f,  0.5f, -0.5f), new DVector3(0.0f,0.0f,0)),
            new Vertex(new DVector3(-0.5f, -0.5f, -0.5f), new DVector3(0.0f, 1.0f,0)),
            new Vertex(new DVector3( 0.5f, -0.5f, -0.5f), new DVector3(1.0f, 0.0f,1)),
            new Vertex(new DVector3( 0.5f,  0.5f, -0.5f), new DVector3(1.0f, 0.0f,0)),

            //Front face
            new Vertex(new DVector3(-0.5f,  0.5f,  0.5f), new DVector3(0.0f, 0.0f,1)),
            new Vertex(new DVector3(-0.5f, -0.5f,  0.5f), new DVector3(0.0f, 1.0f,1)),
            new Vertex(new DVector3( 0.5f, -0.5f,  0.5f), new DVector3(1.0f, 1.0f,0)),
            new Vertex(new DVector3( 0.5f,  0.5f,  0.5f), new DVector3(1.0f, 0.0f,1)),

            //Right face
            new Vertex(new DVector3( 0.5f,  0.5f, -0.5f), new DVector3(0.0f, 0.0f,1)),
            new Vertex(new DVector3( 0.5f, -0.5f, -0.5f), new DVector3(0.0f, 1.0f,0)),
            new Vertex(new DVector3( 0.5f, -0.5f,  0.5f), new DVector3(1.0f, 1.0f,0)),
            new Vertex(new DVector3( 0.5f,  0.5f,  0.5f), new DVector3(1.0f, 0.0f,0)),

            //Left face
            new Vertex(new DVector3(-0.5f,  0.5f, -0.5f), new DVector3(0.0f, 0.0f,1)),
            new Vertex(new DVector3(-0.5f, -0.5f, -0.5f), new DVector3(0.0f, 1.0f,1)),
            new Vertex(new DVector3(-0.5f, -0.5f,  0.5f), new DVector3(1.0f, 0.0f,1)),
            new Vertex(new DVector3(-0.5f,  0.5f,  0.5f), new DVector3(1.0f, 0.0f,1)),

            //Top face
            new Vertex(new DVector3(-0.5f,  0.5f,  0.5f), new DVector3(1.0f, 0.0f,1)),
            new Vertex(new DVector3(-0.5f,  0.5f, -0.5f), new DVector3(0.0f, 1.0f,0)),
            new Vertex(new DVector3( 0.5f,  0.5f, -0.5f), new DVector3(1.0f, 0.0f,1)),
            new Vertex(new DVector3( 0.5f,  0.5f,  0.5f), new DVector3(1.0f, 0.0f,0)),

            //Bottom face
            new Vertex(new DVector3(-0.5f, -0.5f,  0.5f), new DVector3(1.0f, 0.0f,0)),
            new Vertex(new DVector3(-0.5f, -0.5f, -0.5f), new DVector3(0.0f, 1.0f,0)),
            new Vertex(new DVector3( 0.5f, -0.5f, -0.5f), new DVector3(0.0f, 0.0f,1)),
            new Vertex(new DVector3( 0.5f, -0.5f,  0.5f), new DVector3(0.3f, 1.0f,1)),
    }, new int[] {
            //Back face
            0, 1, 3,
            3, 1, 2,

            //Front face
            4, 5, 7,
            7, 5, 6,

            //Right face
            8, 9, 11,
            11, 9, 10,

            //Left face
            12, 13, 15,
            15, 13, 14,

            //Top face
            16, 17, 19,
            19, 17, 18,

            //Bottom face
            20, 21, 23,
            23, 21, 22
    });

    boolean animationRunning = false;
    public GameObject object = new GameObject(new DVector3(0.9,0.9,0),new DVector3(0,0,45),new DVector3(1,1,1),mesh);
    //public GameObject object = new GameObject(new DVector3(0,0,0),new DVector3(1.05,358.46,124.13),new DVector3(1,1,1),mesh);
    public GameObject object2 = new GameObject(new DVector3(0,0,0),new DVector3(0,0,0),new DVector3(1,1,1),mesh);
    public GameObject object3 = new GameObject(new DVector3(0,-1,0),new DVector3(0,0,0),new DVector3(20,1,20),mesh);

    public GameObject Torso = new GameObject(new DVector3(0,0.5,0),new DVector3(0,0,0),new DVector3(0.5,0.5,0.25),mesh);
    public GameObject RightLeg = new GameObject(new DVector3(0.1,0,0),new DVector3(0,0,0),new DVector3(0.18,0.5,0.18),mesh);
    public GameObject LeftLeg = new GameObject(new DVector3(-0.1,0,0),new DVector3(0,0,0),new DVector3(0.18,0.5,0.18),mesh);
    public GameObject RightArm = new GameObject(new DVector3(-0.33,0.5,0),new DVector3(0,0,0),new DVector3(0.15,0.5,0.15),mesh);
    public GameObject LeftArm = new GameObject(new DVector3(0.33,0.5,0),new DVector3(0,0,0),new DVector3(0.15,0.5,0.15),mesh);
    public GameObject Head = new GameObject(new DVector3(0,0.9,0),new DVector3(0,0,0),new DVector3(0.3,0.3,0.3),mesh);

    public Bone torsoBone = new Bone(0, Matrix4f.identity(),Torso);
    public Bone LeftlegBone = new Bone(1, Matrix4f.identity(),LeftLeg);
    public Bone RightlegBone = new Bone(2, Matrix4f.identity(),RightLeg);
    public Bone LeftarmBone = new Bone(3, Matrix4f.identity(),LeftArm);
    public Bone RightarmBone = new Bone(4, Matrix4f.identity(),RightArm);
    public Bone headBone = new Bone(5, Matrix4f.identity(),Head);
    public Skeleton playerSkeleton = new Skeleton(6,torsoBone);

    public JointPose keyFrame1 = new JointPose(0,Arrays.asList(new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0.00)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0))));
    public JointPose keyFrame2 = new JointPose(1,Arrays.asList(new Pose(new DVector3(-10,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0))));

    public JointPose keyFrame3 = new JointPose(2,Arrays.asList(new Pose(new DVector3(-10,5,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0))));
    public JointPose keyFrame4 = new JointPose(3,Arrays.asList(new Pose(new DVector3(0,5,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0))));
    public JointPose keyFrame5 = new JointPose(4,Arrays.asList(new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0))));

    public Clip movement = new Clip(4,Arrays.asList(keyFrame1,keyFrame2,keyFrame3,keyFrame4,keyFrame5));

    public JointPose keyFrame1_2 = new JointPose(0,Arrays.asList(new Pose(new DVector3(0,0,0),new DQuaternion(0.99,0,0,0.01)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0))));
    public JointPose keyFrame2_2 = new JointPose(1,Arrays.asList(new Pose(new DVector3(0,0,0),new DQuaternion(0.45,0,0,0.15)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0.966,0,0,0.2588)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0))));
    public JointPose keyFrame3_2 = new JointPose(2,Arrays.asList(new Pose(new DVector3(0,0,0),new DQuaternion(0.966,0,0,0.2588)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0.966,0,0,0.2588)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0))));
    public JointPose keyFrame4_2 = new JointPose(3,Arrays.asList(new Pose(new DVector3(0,0,0),new DQuaternion(0.966,0,0,0.2588)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0.45,0,0,0.2588)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0))));
    public JointPose keyFrame5_2 = new JointPose(4,Arrays.asList(new Pose(new DVector3(0,0,0),new DQuaternion(0.45,0,0,0.15)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0)),
            new Pose(new DVector3(0,0,0),new DQuaternion(0,0,0,0))));

    public Clip movement_2 = new Clip(4,Arrays.asList(keyFrame1_2,keyFrame2_2,keyFrame3_2,keyFrame4_2,keyFrame5_2));
    public Camera camera = new Camera(new DVector3(0, 0, 1), new DVector3(0, 0, 0));


    public Thread game;
    private Random rand;
    public void start() {
        game = new Thread(this,"game");
        game.start();
        rand = new Random();
    }

    public void init() {
        System.out.println("Initializing Game!");
        window = new Window(width, height, "Game");
        shader = new Shader("src/resources/shaders/mainVertex.glsl", "src/resources/shaders/mainFragment.glsl");
        renderer = new Renderer(window,shader);
        window.setBackgroundColor(0.5f,1,1);
        window.create();
        mesh.create();
        shader.create();

        //object.generateAABB();
        //object3.generateAABB();
        object.generateOBB();
        object2.generateOBB();
        //object3.generateOBB();

        torsoBone.addChild(LeftlegBone);
        torsoBone.addChild(RightlegBone);
        torsoBone.addChild(LeftarmBone);
        torsoBone.addChild(RightarmBone);
        torsoBone.addChild(headBone);
    }

    public void run() {
        init();
        while (!(window.shouldClose() || Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE))) {
            update();
            render();
            // press f11 to switch fullscreen and not
            if (Input.isKeyDown(GLFW.GLFW_KEY_F11)) window.setFullscreen(!window.isFullscreen());
            //if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) window.mouseState(true);
            window.mouseState(true);
        }
        close();
    }

    public Client getClient() {
        return client;
    }

    private void tempChangeBackground() {
        window.setBackgroundColor(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
    }

    private void update() {
        //System.out.println("Updating Game!");
        window.update();
        camera.update();
        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) System.out.println("x: "+ Input.getScrollX() + ", y:" + Input.getScrollY());
        object.update();
        object2.update();
        //object3.update();
        //boolean resultAABB = object.getMyAABB().intersects(object3.getMyAABB());
        //boolean resultOBB = object.getMyOBB().intersects(object3.getMyOBB());
        boolean resultOBB2 = object.getMyOBB().intersects(object2.getMyOBB());
        //System.out.println("AABB: " + resultAABB);
        //System.out.println("OBB: " + resultOBB2);


        if (Input.isKeyDown(GLFW.GLFW_KEY_P) && !animationRunning) {
            playerSkeleton.doAnimation(movement);
            System.out.println("animation started");
        }

        if (Input.isKeyDown(GLFW.GLFW_KEY_O) && !animationRunning) {
            playerSkeleton.doAnimation(movement_2);
            System.out.println("animation_2 started");
        }

        playerSkeleton.update();
        //System.out.println(Matrix4f.toString(playerSkeleton.getRootBone().getAnimatedTransform()));




    }

    private void render() {
        //System.out.println("Rendering Game!");
        renderer.renderMesh(object, camera);
        //renderer.renderMesh(object2, camera);
        renderer.renderMesh(object3, camera);

        renderPlayer(playerSkeleton.getRootBone());
        renderer.setLocalTransform(Matrix4f.identity());

        window.swapBuffers();
    }

    private void close() {
        window.destroy();
        mesh.destroy();
        shader.destroy();
    }

    private void renderPlayer(Bone bone){
        System.out.println(Matrix4f.toString((playerSkeleton.getBoneTransforms().get(bone.getIndex()))));
        renderer.setLocalTransform(playerSkeleton.getBoneTransforms().get(bone.getIndex()));
        renderer.renderMesh(bone.getObject(), camera);
        for (Bone childBone : bone.children) {
            renderPlayer(childBone);
        }
    }

    public static void main(String[] args) {
        new renderEngine().start();
    }

}
