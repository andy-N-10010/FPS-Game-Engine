package gameLoop;

import com.esotericsoftware.kryonet.Client;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Shader;
import engine.graphics.Vertex;
import engine.io.Window;
import engine.objects.Bullet;
import engine.objects.GameObject;
import engine.objects.PlayerCamera;
import org.lwjgl.glfw.GLFW;
import org.ode4j.math.DVector3;
import org.ode4j.ode.DBody;
import org.ode4j.ode.DMass;
import org.ode4j.ode.DWorld;
import org.ode4j.ode.OdeHelper;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class PlayerSimulation implements Runnable{
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

    public GameObject objectSpin = new GameObject(new DVector3(2.9,4,0),new DVector3(0,0,45),new DVector3(1,1,1),mesh);
    public GameObject objectNotSpin = new GameObject(new DVector3(0,2,0),new DVector3(0,0,0),new DVector3(1,1,1),mesh);
    public GameObject objectPlane = new GameObject(new DVector3(0,-1,0),new DVector3(0,0,0),new DVector3(20,1,20),mesh);

    public PlayerCamera camera = new PlayerCamera(objectNotSpin.getPosition(), new DVector3(0, 0, 0), objectNotSpin, objectNotSpin.getMyOBB());
    public DWorld world = OdeHelper.createWorld();
    public Queue<Bullet> bulletQueue = new LinkedList<>();

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

        world.setGravity(0, -9.8, 0);

        DBody body1 = OdeHelper.createBody(world);
        DMass mass1 = OdeHelper.createMass();
        mass1.setMass(1);
        mass1.setSphere(1, 0.05);
        body1.setMass(mass1);
        body1.setPosition(objectSpin.getPosition());
        objectSpin.setBody(body1);
        objectSpin.setMass(mass1);

        DBody body2 = OdeHelper.createBody(world);
        DMass mass2 = OdeHelper.createMass();
        mass2.setMass(1);
        mass2.setSphere(1, 0.05);
        body2.setMass(mass2);
        body2.setPosition(objectNotSpin.getPosition());
        objectNotSpin.setBody(body2);
        objectNotSpin.setMass(mass2);

        //object.generateAABB();
        //object3.generateAABB();
        objectSpin.generateOBB();
        objectNotSpin.generateOBB();
        objectPlane.generateOBB();

        objectSpin.generateInitialSphereColliderWithOBB();
        objectNotSpin.generateInitialSphereColliderWithOBB();
        objectPlane.generateInitialSphereColliderWithOBB();

        camera.setPosition(objectNotSpin.getPosition());
        camera.setPlayerOBB(objectNotSpin.getMyOBB());
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
        System.out.println("Updating Game!");

        window.update();
        objectSpin.update();
        objectNotSpin.update();
        objectPlane.update();
        //boolean resultAABB = object.getMyAABB().intersects(object3.getMyAABB());

        // if the initial collision detection is false, we don't need to care about OBB's collision detection calculation
        boolean resultOBB13 = objectSpin.getMySphere().intersects(objectPlane.getMySphere()) && objectSpin.getMyOBB().intersects(objectPlane.getMyOBB());
        boolean resultOBB12 = objectSpin.getMySphere().intersects(objectSpin.getMySphere()) && objectSpin.getMyOBB().intersects(objectNotSpin.getMyOBB());
        boolean resultOBB23 = objectNotSpin.getMySphere().intersects(objectPlane.getMySphere()) && objectNotSpin.getMyOBB().intersects(objectPlane.getMyOBB());

        if (resultOBB13) {
            objectSpin.getBody().setLinearVel(0, 0, 0);
            objectSpin.getBody().setPosition(objectSpin.getPosition());
        }
        else {
            objectSpin.setPosition((DVector3) objectSpin.getBody().getPosition());
            objectSpin.getMyOBB().update();
        }

        if (resultOBB12) {
            objectSpin.getBody().setLinearVel(0, 0, 0);
            objectSpin.getBody().setPosition(objectSpin.getPosition());
        }
        else {
            objectSpin.setPosition((DVector3) objectSpin.getBody().getPosition());
            objectSpin.getMyOBB().update();
            objectNotSpin.getMyOBB().update();
        }

        if (resultOBB23) {
            objectNotSpin.getBody().setLinearVel(0, 0, 0);
            objectNotSpin.getBody().setPosition(objectNotSpin.getPosition());
            camera.setPosition(objectNotSpin.getPosition());
            objectNotSpin.canJump = true;
        }
        else {
            objectNotSpin.setPosition((DVector3) objectNotSpin.getBody().getPosition());
            objectNotSpin.getMyOBB().update();
            camera.setPosition(objectNotSpin.getPosition());
        }

        //System.out.println("AABB: " + resultAABB);
        System.out.println("OBB12: " + resultOBB12);

        camera.update();
        if (Input.isKeyDown(GLFW.GLFW_KEY_A)) {
            camera.moveLeft();
            if (objectSpin.getMyOBB().intersects(objectNotSpin.getMyOBB())) {
                camera.moveRight();
            }
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_D)) {
            camera.moveRight();
            if (objectSpin.getMyOBB().intersects(objectNotSpin.getMyOBB())) {
                camera.moveLeft();
            }
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_W)) {
            camera.moveForward();
            if (objectSpin.getMyOBB().intersects(objectNotSpin.getMyOBB())) {
                camera.moveBack();
            }
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_S)) {
            camera.moveBack();
            if (objectSpin.getMyOBB().intersects(objectNotSpin.getMyOBB())) {
                camera.moveForward();
            }
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
            camera.getPlayerObj().jump();
        }

        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) System.out.println("x: "+ Input.getScrollX() + ", y:" + Input.getScrollY());

        // object 3 doesn't move
        world.step(0.01);
    }

    private void render() {
        System.out.println("Rendering Game!");
        renderer.renderMesh(objectSpin, camera);
        //renderer.renderMesh(objectNotSpin, camera);
        renderer.renderMesh(objectPlane, camera);

        for (Bullet bullet: bulletQueue) {
            renderer.renderMesh(bullet, camera);
        }

        window.swapBuffers();
    }

    private void close() {
        window.destroy();
        mesh.destroy();
        shader.destroy();
        world.destroy();
    }

    public static void main(String[] args) {
        new PlayerSimulation().start();
    }
}
