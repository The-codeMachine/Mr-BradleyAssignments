#include "../src/Game.hpp"

#include <common/IO.hpp>

void testMove(Game& game, double warp, double direction, const std::string& name) {
    common::IO::printf("%s\n", name.c_str());
    common::IO::printf("Warp %.2f  Direction %.1f\n", warp, direction);

    bool success = game.move(warp, direction);

    common::IO::printf("Move Successful: %i\n", success);

    common::IO::println("Current Quadrant:");
    common::IO::println(game.at(
            common::toBase1(game.getEnterprise().getLocation().quadrantX),
            common::toBase1(game.getEnterprise().getLocation().quadrantY)).toString());

    common::IO::println("");
}

int main() {
    common::IO::println("Game test");

    Game game;

    common::IO::println("Initial Quadrant");
    common::IO::println(game.at(1, 1).toString());

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

    common::IO::println("Game test success");


    return 0;
}