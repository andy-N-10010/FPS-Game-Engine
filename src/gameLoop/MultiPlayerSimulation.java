package gameLoop;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import engine.objects.Camera;
import networking.Network.MovePlayer;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Shader;
import engine.graphics.Vertex;
import engine.io.Window;
import engine.objects.Bullet;
import engine.objects.GameObject;
import engine.objects.PlayerCamera;
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

public class MultiPlayerSimulation implements Runnable{
    public Window window;
    public final int width = 1280, height = 760;
    public Renderer renderer;


    Client client;
    PositionClient.UI ui;
    String username;

    int PlayerCounter = 0;

    HashMap<Integer, GameObject> players = new HashMap<>();
    HashMap<Integer, PlayerCamera> playerCameras = new HashMap<>();

    public MultiPlayerSimulation() {
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
                    renderPlayer();
                    return;
                }

                if (object instanceof Network.UpdatePlayer) {
                    ui.updatePlayer((Network.UpdatePlayer) object);
                    System.out.println(((Network.UpdatePlayer) object).id);
                    renderPlayers(((Network.UpdatePlayer) object).id,((Network.UpdatePlayer) object).x,((Network.UpdatePlayer) object).y, ((Network.UpdatePlayer) object).z);
                    return;
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

    //Player Object
//    public GameObject objectNotSpin = new GameObject(new DVector3(0,2,0),new DVector3(0,0,0),new DVector3(1,1,1),mesh);
    public GameObject objectPlane = new GameObject(new DVector3(0,-1,0),new DVector3(0,0,0),new DVector3(20,1,20),mesh);

//    public PlayerCamera camera = new PlayerCamera(objectNotSpin.getPosition(), new DVector3(0, 0, 0), objectNotSpin, objectNotSpin.getMyOBB());
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
//        body2.setPosition(objectNotSpin.getPosition());
//        objectNotSpin.setBody(body2);
//        objectNotSpin.setMass(mass2);

        //Previous comment
        //object.generateAABB();
        //object3.generateAABB();



        objectSpin.generateOBB();
//        objectNotSpin.generateOBB();
        objectPlane.generateOBB();

        objectSpin.generateInitialSphereColliderWithOBB();
//        objectNotSpin.generateInitialSphereColliderWithOBB();
        objectPlane.generateInitialSphereColliderWithOBB();

//        camera.setPosition(objectNotSpin.getPosition());
//        camera.setPlayerOBB(objectNotSpin.getMyOBB());
    }

    public void renderPlayer() {

        PlayerCounter += 1;

        GameObject objectNotSpin = new GameObject(new DVector3(0,2,0),new DVector3(0,0,0),new DVector3(0.1,1,0.1),mesh);

        PlayerCamera camera = new PlayerCamera(objectNotSpin.getPosition(), new DVector3(0, 0, 0), objectNotSpin, objectNotSpin.getMyOBB());


        DBody body2 = OdeHelper.createBody(world);
        DMass mass2 = OdeHelper.createMass();
        mass2.setMass(1);
        mass2.setSphere(1, 0.05);
        body2.setMass(mass2);
        body2.setPosition(objectNotSpin.getPosition());
        objectNotSpin.setBody(body2);
        objectNotSpin.setMass(mass2);

        objectNotSpin.generateOBB();
        objectNotSpin.generateInitialSphereColliderWithOBB();
        camera.setPosition(objectNotSpin.getPosition());
        camera.setPlayerOBB(objectNotSpin.getMyOBB());

        players.put(PlayerCounter,objectNotSpin);
        playerCameras.put(PlayerCounter,camera);


    }

    public void renderPlayers(int id, float x,float y,float z) {

        //players.get(id).set


        GameObject objectNotSpin = new GameObject(new DVector3(x,y,z),new DVector3(0,0,0),new DVector3(1,1,1),mesh);

        PlayerCamera camera = new PlayerCamera(objectNotSpin.getPosition(), new DVector3(0, 0, 0), objectNotSpin, objectNotSpin.getMyOBB());

        players.get(id).setPosition(objectNotSpin.getPosition());
        playerCameras.get(id).setPosition(camera.getPosition());

//
//        PlayerCamera camera = new PlayerCamera(objectNotSpin.getPosition(), new DVector3(0, 0, 0), objectNotSpin, objectNotSpin.getMyOBB());
//
//
//        DBody body2 = OdeHelper.createBody(world);
//        DMass mass2 = OdeHelper.createMass();
//        mass2.setMass(1);
//        mass2.setSphere(1, 0.05);
//        body2.setMass(mass2);
//        body2.setPosition(objectNotSpin.getPosition());
//        objectNotSpin.setBody(body2);
//        objectNotSpin.setMass(mass2);
//
//        objectNotSpin.generateOBB();
//        objectNotSpin.generateInitialSphereColliderWithOBB();
//        camera.setPosition(objectNotSpin.getPosition());
//        camera.setPlayerOBB(objectNotSpin.getMyOBB());
//
//        players.put(PlayerCounter,objectNotSpin);
//        playerCameras.put(PlayerCounter,camera);


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
        objectSpin.update();
        objectPlane.update();

        if (!players.isEmpty()) {

            for (GameObject player : players.values()) {

                //System.out.println(player);

                for (PlayerCamera playerCamera : playerCameras.values()) {


                    MovePlayer msg = new MovePlayer();


                    player.update();
                    boolean resultOBB12 = objectSpin.getMySphere().intersects(objectSpin.getMySphere()) && objectSpin.getMyOBB().intersects(player.getMyOBB());
                    boolean resultOBB23 = player.getMySphere().intersects(objectPlane.getMySphere()) && player.getMyOBB().intersects(objectPlane.getMyOBB());

                    if (resultOBB12) {
                        objectSpin.getBody().setLinearVel(0, 0, 0);
                        objectSpin.getBody().setPosition(objectSpin.getPosition());
                    }
                    else {
                        objectSpin.setPosition((DVector3) objectSpin.getBody().getPosition());
                        objectSpin.getMyOBB().update();
                        player.getMyOBB().update();
                    }

                    if (resultOBB23) {
                        player.getBody().setLinearVel(0, 0, 0);
                        player.getBody().setPosition(player.getPosition());
                        playerCamera.setPosition(player.getPosition());
                        player.canJump = true;
                    }
                    else {
                        player.setPosition((DVector3) player.getBody().getPosition());
                        player.getMyOBB().update();
                        playerCamera.setPosition(player.getPosition());
                    }

                    //System.out.println("AABB: " + players.get(PlayerCounter));
                    //System.out.println("OBB12: " + resultOBB12);

                    playerCamera.update();
                    if (Input.isKeyDown(GLFW.GLFW_KEY_A)) {
                        playerCamera.moveLeft();
                        msg.x = 0.05f;
                        msg.y = 0;
                        msg.z = -0.05f;
                        client.sendTCP(msg);

                        if (objectSpin.getMyOBB().intersects(player.getMyOBB())) {
                            playerCamera.moveRight();
                            msg.x = -0.05f;
                            msg.y = 0;
                            msg.z = 0.05f;
                            client.sendTCP(msg);
                        }
                    }
                    if (Input.isKeyDown(GLFW.GLFW_KEY_D)) {
                        playerCamera.moveRight();
                        msg.x = -0.05f;
                        msg.y = 0;
                        msg.z = 0.05f;
                        client.sendTCP(msg);

                        if (objectSpin.getMyOBB().intersects(player.getMyOBB())) {
                            playerCamera.moveLeft();
                            msg.x = 0.05f;
                            msg.y = 0;
                            msg.z = -0.05f;
                            client.sendTCP(msg);
                        }
                    }
                    if (Input.isKeyDown(GLFW.GLFW_KEY_W)) {
                        playerCamera.moveForward();
                        msg.x = -0.05f;
                        msg.y = 0;
                        msg.z = -0.05f;
                        client.sendTCP(msg);
                        if (objectSpin.getMyOBB().intersects(player.getMyOBB())) {
                            playerCamera.moveBack();
                            msg.x = 0.05f;
                            msg.y = 0;
                            msg.z = 0.05f;
                            client.sendTCP(msg);
                        }
                    }
                    if (Input.isKeyDown(GLFW.GLFW_KEY_S)) {
                        playerCamera.moveBack();
                        msg.x = 0.05f;
                        msg.y = 0;
                        msg.z = 0.05f;
                        client.sendTCP(msg);
                        if (objectSpin.getMyOBB().intersects(player.getMyOBB())) {
                            playerCamera.moveForward();
                            msg.x = -0.05f;
                            msg.y = 0;
                            msg.z = -0.05f;
                            client.sendTCP(msg);
                        }
                    }
                    if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
                        playerCamera.getPlayerObj().jump();

                    }

        }

            }

        }


        // if the initial collision detection is false, we don't need to care about OBB's collision detection calculation
        boolean resultOBB13 = objectSpin.getMySphere().intersects(objectPlane.getMySphere()) && objectSpin.getMyOBB().intersects(objectPlane.getMyOBB());


        if (resultOBB13) {
            objectSpin.getBody().setLinearVel(0, 0, 0);
            objectSpin.getBody().setPosition(objectSpin.getPosition());
        }
        else {
            objectSpin.setPosition((DVector3) objectSpin.getBody().getPosition());
            objectSpin.getMyOBB().update();
        }


        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) System.out.println("x: "+ Input.getScrollX() + ", y:" + Input.getScrollY());

        // object 3 doesn't move
        world.step(0.01);
    }

    private void render() {
        //System.out.println("Rendering Game!");

        //System.out.println(players);

        if (!players.isEmpty()) {

            for (GameObject player : players.values()) {

                for (PlayerCamera playerCamera : playerCameras.values()) {

                    //System.out.println(playerCamera);

                    renderer.renderMesh(objectSpin, playerCamera);
                    //renderer.renderMesh(objectNotSpin);
//        //renderer.renderMesh(objectNotSpin, camera);

                    renderer.renderMesh(player,playerCamera);

                    renderer.renderMesh(objectPlane, playerCamera);
//
                    for (Bullet bullet : bulletQueue) {
                        renderer.renderMesh(bullet, playerCamera);
                    }


                }

            }

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
        new MultiPlayerSimulation().start();
    }
}
