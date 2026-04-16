add_executable(quadrant tests/test_quadrant.cpp src/quadrant.cpp)

target_link_libraries(quadrant PRIVATE common)

target_include_directories(quadrant PRIVATE src)
target_include_directories(quadrant PRIVATE ../common/include)
