add_executable(common_test src/main.cpp)

target_link_libraries(common_test PRIVATE common)

target_include_directories(common_test PRIVATE src)
target_include_directories(common_test PRIVATE ../common/include)
