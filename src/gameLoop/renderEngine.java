package gameLoop;

import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Shader;
import engine.graphics.Vertex;
import org.lwjgl.glfw.GLFW;
import engine.io.Window;
import org.ode4j.math.DVector3;

import java.util.Random;

public class renderEngine implements Runnable{
    public Window window;
    public final int width = 1280, height = 760;
    public Renderer renderer;

    public Shader shader;
    public Mesh mesh = new Mesh(new Vertex[] {
            new Vertex(new DVector3(-0.5, 0.5, 0.0)),
            new Vertex(new DVector3(0.5, 0.5, 0.0)),
            new Vertex(new DVector3(0.5, -0.5, 0.0)),
            new Vertex(new DVector3(-0.5, -0.5, 0.0))
    }, new int[] {
            0, 1, 2,
            0, 3, 2
    });

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
        shader = new Shader("/resources/shaders/mainVertex.glsl", "/resources/shaders/mainFragment.glsl");
        renderer = new Renderer(shader);
        window.setBackgroundColor(1.0f,0,0);
        window.create();
        mesh.create();
        shader.create();
    }

    public void run() {
        init();
        while (!(window.shouldClose() || Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE))) {
            update();
            render();
            // press f11 to switch fullscreen and not
            if (Input.isKeyDown(GLFW.GLFW_KEY_F11)) window.setFullscreen(!window.isFullscreen());
        }
        window.destroy();
    }

    private void tempChangeBackground() {
        window.setBackgroundColor(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
    }

    private void update() {
        System.out.println("Updating Game!");
        // temp test code
        tempChangeBackground();
        window.update();
        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) System.out.println("x: "+ Input.getScrollX() + ", y:" + Input.getScrollY());
    }

    private void render() {
        System.out.println("Rendering Game!");
        renderer.renderMesh(mesh);
        window.swapBuffers();
    }

    public static void main(String[] args) {
        new renderEngine().start();
    }

}
