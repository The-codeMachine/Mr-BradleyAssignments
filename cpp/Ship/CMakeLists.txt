add_executable(ship tests/test_ship.cpp src/ship.cpp ../Device/src/Device.cpp)

target_link_libraries(ship PRIVATE common)

target_include_directories(ship PRIVATE src)
target_include_directories(ship PRIVATE ../common/include)
target_include_directories(ship PRIVATE ../Device/src)