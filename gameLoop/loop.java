package gameLoop;
import gameState.*;
/**
 * loop
 */
public class loop {

    public void getInput() {
        //get player input 
    }

    public void timeForUpdatingPhysics(){
        int currentTime = ;
        int redrawingPeriod = 20;
        int maxFrameSkip = 10;
        boolean needToRedraw = true;

        // check if it is time to update
    }
    
    public static void main(String[] args) {

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
    }

    
}
