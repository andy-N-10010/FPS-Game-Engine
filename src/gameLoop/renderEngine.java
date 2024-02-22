package gameLoop;

import org.lwjgl.glfw.GLFW;
import engine.io.Window;

import java.util.Random;

public class renderEngine implements Runnable{
    public static Window window;
    public static final int width = 1280, height = 760;
    public Thread game;
    private Random rand;
    public void start() {
        game = new Thread(this,"game");
        game.start();
        rand = new Random();
    }

    public static void init() {
        System.out.println("Initializing Game!");
        window = new Window(width, height, "Game");
        window.setBackgroundColor(0,0,256);
        window.create();
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
        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) System.out.println("x: "+ Input.getMouseX() + ", y:" + Input.getMouseY());
    }

    private void render() {
        System.out.println("Rendering Game!");
        window.swapBuffers();
    }

    public static void main(String[] args) {
        new renderEngine().start();
    }

}
