add_executable(klingon tests/test_klingon.cpp src/klingon.cpp ../ship/src/Ship.cpp)

target_link_libraries(klingon PRIVATE common)

target_include_directories(klingon PRIVATE src)
target_include_directories(klingon PRIVATE ../ship/src)
target_include_directories(klingon PRIVATE ../common/include)