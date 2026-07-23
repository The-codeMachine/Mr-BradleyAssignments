package Game;

import common.GameLib;
import common.IO;

public class Main {
    private static void testMove(Game game, double warp, double direction, String name) {
        IO.printf("%s\n", name);
        IO.printf("Warp %.2f  Direction %.1f\n", warp, direction);

        boolean success = game.move(warp, direction);

        IO.printf("Move Successful: %b\n", success);

        IO.println("Current Quadrant:");
        IO.println(game.at(
                GameLib.toBase1(game.getEnterprise().getLocation().quadrantX),
                GameLib.toBase1(game.getEnterprise().getLocation().quadrantY)).toString());

        IO.println("");
    }

    public static void main(String[] args) {
        IO.println("Game test");

        Game game = new Game();

        IO.println("Initial Quadrant");
        IO.println(game.at(1, 1).toString());

        // Test every cardinal/intercardinal direction
        testMove(game, 0.75, 1, "North");
        testMove(game, 0.75, 2, "North-East");
        testMove(game, 0.75, 3, "East");
        testMove(game, 0.75, 4, "South-East");
        testMove(game, 0.75, 5, "South");
        testMove(game, 0.75, 6, "South-West");
        testMove(game, 0.75, 7, "West");
        testMove(game, 0.75, 8, "North-West");

        // Longer movement
        testMove(game, 2.5, 3, "Long East");

        // Cross a quadrant boundary
        testMove(game, 7.5, 3, "Cross Quadrant");

        // Maximum warp
        testMove(game, 10.0, 2, "Maximum Warp");

        // Warp larger than maximum (should clamp)
        testMove(game, 15.0, 2, "Warp Clamp");

        IO.println("Game test success");

        game = new Game();

        game.run();
    }
}

/**
 * Sample Output 
 * 
 * Game test
 * Initial Quadrant
 * --------------------------------
 * <*>|   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   | * |   |
 * --------------------------------
 *    |   |   |   |   |   |   | * |
 * --------------------------------
 *  * |   |   |   |   |   | * |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * 
 * ...
 * 
 * Warp Clamp
 * Warp 15.00  Direction 2.0
 * (5, 2) in (3, 0)
 * (6, 1) in (3, 0)
 * (7, 0) in (3, 0)
 * Move Successful: true
 * Current Quadrant:
 * --------------------------------
 *    |   |   |   |   |   |   |<*>|
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   | * |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * 
 * 
 * Game test success
 * 
 */