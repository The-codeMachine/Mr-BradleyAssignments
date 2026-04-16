# Mr-BradleyAssignments/cpp

This directory holds all of the C++ solutions to the assignments given. 

Everything here is built using CMake. Please ensure you have that installed on your machine before attempting to build it. However, I believe g++ would work as well, as I only include the base C++ files. 

## Building 

Again, to ensure that everything will build correctly, have CMake installed, as well as any C++ compiler. All projects were compiled with MSVC. 

- CMake
  - Version: 4.1.0-rc3
  - Verify installation through
    ```powershell
    cmake --version
    ```

- Any C++ compiler
  - Really any C++ compiler would work here, but I recommend MSVC, as that is what this was natively built in

### Running an Assignment

Each assignment has its own folder. To run these commands, do this from the C++ root directory. Each folder has its own ```CMakeLists.txt``` file. To run one:
```powershell
cd build
cmake .. # constructs cache, only need to do this once

cmake --build . # or from the root directory cmake --build build

./assignmentX/Debug/assignmentX # run the program

```

This command was taken from the cpp/ sub directory.

The .exe file will be found inside (from the root directory) ```build/assignmentX/Debug/assignmentX.exe```.

## Adding a new Assignment

- Create a folder with the assignment's name
- Add CMakeLists.txt, example usage:
```CMake
add_executable(assignmentX tests/test_driver.cpp src/src_file.cpp)

target_link_libraries(assignmentX PRIVATE common)

target_include_directories(assignmentX PRIVATE src) # for headers
target_include_directories(assignmentX PRIVATE ../common/include)
```

- Put all of your source files inside a ```src/``` sub directory (optional).