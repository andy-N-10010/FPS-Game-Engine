package gameLoop;

import com.esotericsoftware.kryonet.Client;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Shader;
import engine.graphics.Vertex;
import engine.io.Window;
import engine.objects.Camera;
import engine.objects.GameObject;
import org.lwjgl.glfw.GLFW;
import org.ode4j.math.DVector3;
import org.ode4j.ode.DBody;
import org.ode4j.ode.DMass;
import org.ode4j.ode.DWorld;
import org.ode4j.ode.OdeHelper;

import java.util.Random;

public class GravitySimulation implements Runnable{
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

    public GameObject object = new GameObject(new DVector3(0.9,0.9,0),new DVector3(0,0,45),new DVector3(1,1,1),mesh);
    //public GameObject object = new GameObject(new DVector3(0,0,0),new DVector3(1.05,358.46,124.13),new DVector3(1,1,1),mesh);
    public GameObject object2 = new GameObject(new DVector3(0,0,0),new DVector3(0,0,0),new DVector3(1,1,1),mesh);
    public GameObject object3 = new GameObject(new DVector3(0,-1,0),new DVector3(0,0,0),new DVector3(20,1,20),mesh);

    public Camera camera = new Camera(new DVector3(0, 0, 1), new DVector3(0, 0, 0));
    public DWorld world = OdeHelper.createWorld();

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

        world.setGravity(0, -2, 0);

        DBody body1 = OdeHelper.createBody(world);
        DMass mass1 = OdeHelper.createMass();
        mass1.setSphere(1, 0.05);
        body1.setMass(mass1);
        body1.setPosition(object.getPosition());
        body1.setMass(mass1);
        object.setBody(body1);
        object.setMass(mass1);

        DBody body2 = OdeHelper.createBody(world);
        DMass mass2 = OdeHelper.createMass();
        mass2.setSphere(1, 0.05);
        body2.setMass(mass1);
        body2.setPosition(object2.getPosition());
        body2.setMass(mass1);
        object2.setBody(body2);
        object2.setMass(mass2);

        //object.generateAABB();
        //object3.generateAABB();
        object.generateOBB();
        object2.generateOBB();
        //object3.generateOBB();
    }

    public void run() {
        init();
        while (!(window.shouldClose() || Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE))) {
            world.step((double) 1/30);
            object.setPosition((DVector3) object.getBody().getPosition());
            object2.setPosition((DVector3) object2.getBody().getPosition());
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
        camera.update();
        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) System.out.println("x: "+ Input.getScrollX() + ", y:" + Input.getScrollY());
        object.update();
        object2.update();
        //object3.update();
        //boolean resultAABB = object.getMyAABB().intersects(object3.getMyAABB());
        //boolean resultOBB = object.getMyOBB().intersects(object3.getMyOBB());
        boolean resultOBB2 = object.getMyOBB().intersects(object2.getMyOBB());
        //System.out.println("AABB: " + resultAABB);
        System.out.println("OBB: " + resultOBB2);
    }

    private void render() {
        System.out.println("Rendering Game!");
        renderer.renderMesh(object, camera);
        renderer.renderMesh(object2, camera);
        renderer.renderMesh(object3, camera);
        window.swapBuffers();
    }

    private void close() {
        window.destroy();
        mesh.destroy();
        shader.destroy();
    }

    public void generateBody() {
        DWorld world = OdeHelper.createWorld();
        DBody body = OdeHelper.createBody(world);
        DMass mass = OdeHelper.createMass();

        world.setGravity(0, -9.8, 0);

        mass.setSphere(1, 0.05);
        body.setMass(mass);

        body.setPosition(0, 10, 0);

        for (int i = 0; i < 1000; i++) {
            world.step(0.01);
            double[] position = body.getPosition().toDoubleArray();
            System.out.println("t=" + (i * 0.01) + "s, y=" + position[1]);
        }
        //body.setLinearVel(0, 0, 0);
    }

    public static void main(String[] args) {
        new GravitySimulation().start();
    }
}
