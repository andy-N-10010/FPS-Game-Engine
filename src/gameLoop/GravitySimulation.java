package gameLoop;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Shader;
import engine.graphics.Vertex;
import engine.io.Window;
import engine.objects.Bullet;
import engine.objects.Camera;
import engine.objects.GameObject;
import networking.Network;
import networking.PositionClient;
import org.lwjgl.glfw.GLFW;
import org.ode4j.math.DVector3;
import org.ode4j.ode.DBody;
import org.ode4j.ode.DMass;
import org.ode4j.ode.DWorld;
import org.ode4j.ode.OdeHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class GravitySimulation implements Runnable{
    public Window window;
    public final int width = 1280, height = 760;
    public Renderer renderer;

    Client client;
    PositionClient.UI ui;
    String username;

    boolean jumped = false;

    HashMap<Integer, networking.GameObject> objects = new HashMap<>();

    int objectCounter = 0;

    public GravitySimulation() {
        client = new Client();
        client.start();

        Network.register(client);

        client.addListener(new Listener() {
            public void connected (Connection connection) {
            }

            public void received (Connection connection, Object object) {
                if (object instanceof Network.SomeResponse) {
                    Network.SomeResponse response = (Network.SomeResponse)object;
                    System.out.println(response.text);
                }

                if (object instanceof Network.RegistrationRequired) {
                    Network.Register register = new Network.Register();
                    register.username = username;
                    client.sendTCP(register);
                }

                if (object instanceof Network.AddPlayer) {
                    Network.AddPlayer msg = (Network.AddPlayer)object;
                    ui.addPlayer(msg.player);
                    return;
                }

                if (object instanceof Network.AddObject)  {
                    Network.AddObject msg = (Network.AddObject)object;
                    ui.addObject(msg.object);
                    return;
                }

                if (object instanceof Network.UpdatePlayer) {
                    ui.updatePlayer((Network.UpdatePlayer) object);
                    return;
                }

                if (object instanceof Network.UpdateGameObject) {

                    jumped = true;

//                    ui.updateGameObject((Network.UpdateGameObject) object);
//                    return;

                }


                if (object instanceof Network.RemovePlayer) {
                    Network.RemovePlayer msg = (Network.RemovePlayer)object;
                    ui.removePlayer(msg.id);
                    return;
                }


            }

            public void disconnected (Connection connection) {
                System.exit(0);
            }
        });

        ui = new PositionClient.UI();

        String host = ui.inputHost();


        try {
            client.connect(5000, host, Network.port);
            // Server communication after connection can go here, or in Listener#connected().
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        username = ui.inputName();

        Network.Login login = new Network.Login();
        login.username = username;
        client.sendTCP(login);


        //Network.AddObject msg = (Network.AddObject)object;
        //ui.addObject(msg.object);

        networking.GameObject gameObject = new networking.GameObject();

        objectCounter += 1;

        gameObject.id = objectCounter;
        gameObject.name = "objectNotSpin";
        gameObject.x = 0;
        gameObject.y = 2;
        gameObject.z = 0;

        ui.addObject(gameObject);


    }

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



    //networking.GameObject networkObject = new networking.GameObject();
    //GravitySimulation.addObject(networkObject);

    //Network.AddObject();

    //    Network.AddPlayer msg = (Network.AddPlayer)object;
//                        ui.addPlayer(msg.player);
//                        return;

    public GameObject objectPlane = new GameObject(new DVector3(0,-1,0),new DVector3(0,0,0),new DVector3(20,1,20),mesh);

    //Player object
    public Camera camera = new Camera(new DVector3(0, 0, 1), new DVector3(0, 0, 0));
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
        //System.out.println("Initializing Game!");
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

        Network.MoveObject msg = new Network.MoveObject();

        window.update();
        camera.update();
        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT))
            System.out.println("x: " + Input.getScrollX() + ", y:" + Input.getScrollY());
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
        } else {
            objectSpin.setPosition((DVector3) objectSpin.getBody().getPosition());
            objectSpin.getMyOBB().update();
        }

        if (resultOBB12) {
            objectSpin.getBody().setLinearVel(0, 0, 0);
            objectSpin.getBody().setPosition(objectSpin.getPosition());
        } else {
            objectSpin.setPosition((DVector3) objectSpin.getBody().getPosition());
            objectSpin.getMyOBB().update();
            objectNotSpin.getMyOBB().update();
        }

        if (resultOBB23) {
            objectNotSpin.getBody().setLinearVel(0, 0, 0);
            objectNotSpin.getBody().setPosition(objectNotSpin.getPosition());
            objectNotSpin.canJump = true;
        } else {
            objectNotSpin.setPosition((DVector3) objectNotSpin.getBody().getPosition());
            objectNotSpin.getMyOBB().update();
        }

        if (jumped) {

            objectNotSpin.jump();

            jumped = false;



        } else {

            if (Input.isKeyDown(GLFW.GLFW_KEY_J) && objectNotSpin.canJump) {
                objectNotSpin.jump();
                msg.x = 0.00f;
                msg.y = 0.2f;
                msg.z = 0.00f;
                client.sendTCP(msg);

            }
            if (Input.isKeyDown(GLFW.GLFW_KEY_UP)) {
                objectNotSpin.moveForward();
            }
            if (Input.isKeyDown(GLFW.GLFW_KEY_DOWN)) {
                objectNotSpin.moveBack();
            }
            if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT)) {
                objectNotSpin.moveLeft();
            }
            if (Input.isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
                objectNotSpin.moveRight();
            }


            for (Bullet bullet : bulletQueue) {
                //bullet.setPosition((DVector3) bullet.getBody().getPosition());
                bullet.getMyOBB().update();
            }

            //System.out.println("AABB: " + resultAABB);
            //System.out.println("OBB12: " + resultOBB12);

            shoot();
            //System.out.println("Camera rotation: " + camera.getRotation());
            // object 3 doesn't move
            world.step(0.01);
        }
    }

    private void render() {
        //System.out.println("Rendering Game!");
        renderer.renderMesh(objectSpin, camera);
        renderer.renderMesh(objectNotSpin, camera);
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

    public void addObject(networking.GameObject object) {
        objects.put(object.id, object);

        System.out.println(object.name + "added at "+ object.x + ", "+ object.y + ", "+object.z);



    }

    private void shoot() {
        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {

            System.out.println("Bullet generated.");
           // Bullet bullet = new Bullet(new DVector3(camera.getPosition()), new DVector3(camera.getRotation()), new DVector3(0.1, .1, .1), mesh);
            //DBody bulletBody = OdeHelper.createBody(world);
            //bulletBody.setPosition(bullet.getPosition());
            //bulletBody.setQuaternion(bullet.eulerToQuaternion(bullet.getRotationEuler()));
            //DMass bulletMass = OdeHelper.createMass();
            //bullet.setBody(bulletBody);
            //bullet.setMass(bulletMass);
            /*
            bullet.generateOBB();

            //bullet.getBody().addForce(bullet.getRotationEuler().normalize());
            if (bulletQueue.size() > 10) {
                bulletQueue.remove();
            }
            bulletQueue.add(bullet);*/
        }
    }

    public static void main(String[] args) {
        new GravitySimulation().start();
    }
}
