add_executable(quadrantMap tests/test_quadrantMap.cpp src/QuadrantMap.cpp src/QuadrantString.cpp ../quadrant/src/quadrant.cpp ../klingon/src/klingon.cpp ../ship/src/ship.cpp)

target_link_libraries(quadrantMap PRIVATE common)

target_include_directories(quadrantMap PRIVATE src)
target_include_directories(quadrantMap PRIVATE ../common/include)
target_include_directories(quadrantMap PRIVATE ../quadrant/src)
target_include_directories(quadrantMap PRIVATE ../ship/src)
target_include_directories(quadrantMap PRIVATE ../klingon/src)