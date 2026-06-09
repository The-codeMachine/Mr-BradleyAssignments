add_executable(enterprise tests/test_enterprise.cpp src/enterprise.cpp)

target_link_libraries(enterprise PRIVATE common)

target_include_directories(enterprise PRIVATE src)
target_include_directories(enterprise PRIVATE ../common/include)
