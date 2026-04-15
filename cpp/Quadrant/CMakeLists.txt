add_executable(quadrant_tests tests/test_quadrant.cpp src/quadrant.cpp)

target_link_libraries(quadrant_tests PRIVATE common)

target_include_directories(quadrant_tests PRIVATE src)
target_include_directories(quadrant_tests PRIVATE ../common/include)
