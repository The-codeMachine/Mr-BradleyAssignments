add_executable(random_test src/random.cpp)

target_link_libraries(random_test PRIVATE common)

target_include_directories(random_test PRIVATE src)
target_include_directories(random_test PRIVATE ../common/include)
