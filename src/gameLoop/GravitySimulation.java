package gameLoop;

import org.ode4j.ode.DBody;
import org.ode4j.ode.DMass;
import org.ode4j.ode.DWorld;
import org.ode4j.ode.OdeHelper;

public class GravitySimulation {
    public static void main(String[] args) {
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
    }
}
