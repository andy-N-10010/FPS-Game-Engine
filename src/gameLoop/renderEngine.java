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
        while (true) {
            update();
            render();
        }
    }

    private void update() {
        System.out.println("Updating Game!");
        window.update();
    }

    private void render() {
        System.out.println("Rendering Game!");
        window.swapBuffers();
    }

    public static void main(String[] args) {
        new renderEngine().start();
    }

}
