package gameLoop;

import org.lwjgl.glfw.GLFW;
import engine.io.Window;

public class renderEngine implements Runnable{
    public static Window window;
    public static final int width = 1280, height = 760;
    public Thread game;
    public void start() {
        game = new Thread(this,"game");
        game.start();
    }

    public static void init() {
        System.out.println("Initializing Game!");
        window = new Window(width, height, "Game");
        window.create();
    }

    public void run() {
        init();
        while (!window.shouldClose()) {
            update();
            render();
            if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) return;
        }
        window.destory();
    }

    private void update() {
        System.out.println("Updating Game!");
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
