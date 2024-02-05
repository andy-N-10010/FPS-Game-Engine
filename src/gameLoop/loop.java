package gameLoop;

import gameState.gameEngine;

/**
 * loop
 */
public class loop {

    public void getInput() {
        //get player input 
    }

    public boolean timeForUpdatingPhysics(){
        int currentTime = 0;
        int redrawingPeriod = 20;
        int maxFrameSkip = 10;
        boolean needToRedraw = true;

        // check if it is time to update
        return needToRedraw;
    }

    private void updateStatistics() {
    }

    private boolean timeForRendering() {

        return true;
    }

    private void FPScontrol() {
    }

    
    public static void main(String[] args) {

        loop gameLoop = new loop();

        gameEngine g = new gameState.gameEngine();

        boolean quit = false;


        while (!quit) {


            if (gameLoop.timeForUpdatingPhysics()) {

                g.updatePhysics();

                //gameLoop.updatePhysics();

            }
            gameLoop.updateStatistics();

            if (gameLoop.timeForRendering()){

                g.render();


            }

            gameLoop.FPScontrol();

            quit = true;
        }



    /*
        gameEngine g = new gameState.gameEngine();
        while (!quit) {
            pollForOSMessages();
            I = getInput();

            //update as often as possible
            if (timeForUpdatingPhysics()) {
                g.updateGameState(I);
            }
            // Use to update health, ammo, used abilities, etc
            g.updateStatistics();  
            
            // 30 or 60 fps
            if (timeForRendering()) {
                g.render();
            }
            FPScontrol();
        }
        //delete g; is not necessary in Java
        */
        System.out.println("Hello World!");
    }


}
