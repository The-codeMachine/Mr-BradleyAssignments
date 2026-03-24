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

Each assignment has its own folder. Each folder has its own ```CMakeLists.txt``` file. To run one:
```powershell
cd assignmentX # go inside assignment sub directory

mkdir build # make and go inside a build sub directory
cd build

cmake .. # building using CMake
cmake --build .
```

This command was taken from the cpp/ sub directory.

The .exe file will be found inside (from the assignment sub directory) ```build/Debug/assignmentX.exe```.

## Adding a new Assignment

- Create a folder with the assignment's name
- Add CMakeLists.txt, example usage:
```CMake
cmake_minimum_required(VERSION 3.18)
project(assignmentX CXX)

add_executable(assignmentX src/main.cpp)
```

- Put all of your source files inside a ```src/``` sub directory (optional).