cmake_minimum_required(VERSION 3.18)
project(Light CXX)

set(CMAKE_CXX_STANDARD 20)

add_executable(quadrant_tests tests/test_quadrant.cpp src/quadrant.cpp)

target_include_directories(quadrant_tests PRIVATE src)
