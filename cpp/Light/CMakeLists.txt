cmake_minimum_required(VERSION 3.18)
project(Light CXX)

set(CMAKE_CXX_STANDARD 20)

add_executable(light_tests tests/test_light.cpp src/light.cpp)

target_include_directories(light_tests PRIVATE src)
