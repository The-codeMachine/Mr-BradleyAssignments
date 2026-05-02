add_executable(galaxy tests/test_galaxy.cpp src/galaxy.cpp ../quadrant/src/quadrant.cpp)

target_link_libraries(galaxy PRIVATE common)

target_include_directories(galaxy PRIVATE src)
target_include_directories(galaxy PRIVATE ../common/include)
target_include_directories(galaxy PRIVATE ../Quadrant/src)