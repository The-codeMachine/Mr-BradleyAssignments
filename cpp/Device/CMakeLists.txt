add_executable(device tests/test_device.cpp src/device.cpp)

target_link_libraries(device PRIVATE common)

target_include_directories(device PRIVATE src)
target_include_directories(device PRIVATE ../common/include)