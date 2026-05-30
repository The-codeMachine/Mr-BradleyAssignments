package enterprise;

import device.*;
import ship.*;

/**
 * The Enterprise is the player's ship.
 *
 * Additional functionality:
 *  - energy
 *  - torpedoes
 *  - docking
 *  - movement
 *
 */
public class Enterprise extends Ship {
    public Enterprise(double shields, double health, int x, int y) {
        super(shields, health, x, y);

        energy = 3000;
        torpedoes = 10;
        docked = false;

        addDevice(new Device(1, "Warp Engines"));
        addDevice(new Device(2, "Short Range Sensors"));
        addDevice(new Device(3, "Long Range Sensors"));
        addDevice(new Device(4, "Phaser Control"));
        addDevice(new Device(5, "Torpedo Control"));
        addDevice(new Device(6, "Shield Control"));
        addDevice(new Device(7, "Damage Control"));
        addDevice(new Device(8, "Computer Systems"));
    }

    /**
     * 
     * Gets the ship's energy
     * 
     */
    public int getEnergy() {
        return energy;
    }

    /**
     *
     * Gets the ship's torpedoes 
     * 
     */
    public int getTorpedoes() {
        return torpedoes;
    }

    /**
     * 
     * Gets the ship's dock status
     * 
     */
    public boolean isDocked() {
        return docked;
    }

    /**
     * 
     * Moves the enterprise to a new X, and Y position,
     * will become more accurate to the original later,
     * currently just a WIP just to have the function.
     * 
     * @param newX
     * @param newY
     * @param warpFactor
     * 
     */
    public void move(int newX, int newY, double warpFactor) {
        assert warpFactor >= 0;

        setPosition(newX, newY);

        energy -= (int)(warpFactor * 10);

        // travelling repairs devices
        repairAllDevices(Math.min(warpFactor, 1));

        randomDeviceEvent();
    }

    /**
     * 
     * Docks the enterprise and replenishes all of its
     * supplies.
     * 
     */
    public void dock() {
        docked = true;

        energy = 3000;
        torpedoes = 10;

        resetDevices();
    }

    /**
     * 
     * Another WIP function, it is just something to have 
     * so that we understand all the functions that have to
     * be here, I will make in more like the original later. 
     * 
     * @param amount
     * 
     */
    public void firePhasers(double amount) {
        assert amount >= 0;
        assert amount <= energy;

        energy -= amount;
    }

    /**
     * 
     * You are probably getting used to all of the WIP 
     * functions. Here is another one, minimal functionality,
     * just to show what we need to do. 
     * 
     */
    public void fireTorpedo() {
        if (torpedoes < 1)
            return;

        torpedoes--;
    }

    @Override
    public void takeDamage(double phaserEnergy, double distance) {
        if (docked)
            return;

        super.takeDamage(phaserEnergy, distance);
    }

    /**
     * 
     * Tests the enterprise's private functions
     * 
     */
    public static void whiteBoxTest() {
        System.out.println("Enterprise white box test");
            
        Enterprise e = new Enterprise(1000.0, 1000.0, 0, 0);
        
        assert e.getEnergy() == 3000 : "Ship did not generate with the correct energy";
        assert e.getTorpedoes() == 10 : "Ship did not generate with the correct torpedoes";
        
        e.firePhasers(500);
        assert e.getEnergy() == 2500 : "Ship does not have the correct energy amount";
        
        e.fireTorpedo();
        assert e.getTorpedoes() == 9 : "Ship does not have the correct torpedoes amount";
        
        e.move(5, 5, 3);
        assert e.getX() == 5 : "Ship did not move correctly";
        assert e.getY() == 5 : "Ship did not move correctly";
        
        e.dock();
        assert e.getEnergy() == 3000 : "Ship did not replenish its energy";
        assert e.getTorpedoes() == 10 : "Ship did not replenish its energy";
        
        System.out.println(e);
        System.out.println("Enterprise white box test success");
    }

    @Override
    public String toString() {
        String out = "";

        out += super.toString();

        out += "Energy: " + Integer.toString(energy) + "\n";
        out += "Torpedoes: " + Integer.toString(torpedoes) + "\n";
        out += "Docked: " + (docked ? "Yes" : "No") + "\n";

        return out;
    }


    private int energy;
    private int torpedoes;
    private boolean docked;
}
