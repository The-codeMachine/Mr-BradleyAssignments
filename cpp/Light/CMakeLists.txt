cmake_minimum_required(VERSION 3.18)

set(CMAKE_CXX_STANDARD 20)

add_executable(light tests/test_light.cpp src/light.cpp)

target_include_directories(light PRIVATE src)
