package gameLoop;
import gameState.*;
/**
 * loop
 */
public class loop {

    public static void main(String[] args) {

        gameEngine g = new gameState.gameEngine();
        while (!quit) {
            pollForOSMessages();
            I = getInput(&quit);

            //update as often as possible
            if (timeForUpdatingPhysics()) {
                G.updateGameState(I);
            }
            // Use to update health, ammo, used abilities, etc
            updateStatistics();  
            // 30 or 60 fps
            if (timeForRendering()) {
                G.render();
            }
            FPScontrol();
        }
        delete G;
    }

    
}
