add_executable(game tests/test_game.cpp src/game.cpp ../Device/src/device.cpp ../Ship/src/ship.cpp ../Klingon/src/klingon.cpp ../Enterprise/src/enterprise.cpp ../QuadrantMap/src/quadrantstring.cpp ../QuadrantMap/src/quadrantmap.cpp ../Galaxy/src/galaxy.cpp ../Quadrant/src/quadrant.cpp)

target_link_libraries(game PRIVATE common)

target_include_directories(game PRIVATE src)
target_include_directories(game PRIVATE ../Device/src)
target_include_directories(game PRIVATE ../Ship/src)
target_include_directories(game PRIVATE ../Enterprise/src)
target_include_directories(game PRIVATE ../Quadrant/src)
target_include_directories(game PRIVATE ../QuadrantMap/src)
target_include_directories(game PRIVATE ../Galaxy/src)
target_include_directories(game PRIVATE ../Klingon/src)
target_include_directories(game PRIVATE ../common/include)