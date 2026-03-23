# Mr. Bradley Assignments

This is a GitHub repository for organizing the solutions to the assignments my teacher gave me. 

Each assignment is organized into a folder, for example the Light assignment's solution can be found inside the Light folder.

This is organized as a multi-module Gradle project. Each assignment is its own module. 

## Building

In advance, please understand that all commands in this .md file take please in this root directory.

To build this please ensure you have all of these tools properly installed:

- **Java Development Kit (JDK)**
  - Version: Java 21
  - Verify installation through 
   ```powershell
    java -version
  ```

### Running an Assignment

Each assignment is a separate module. To run one:

```powershell
./gradlew :light:run
```

You can replace the light with any assignment (the assignment's name will always be the folder's name)

### Compiling all of the modules

To compile all of the modules you must run this command:

```powershell
./gradlew build
```

## Adding a new Assignment

- Create a new folder with the assignment's name

- Add to ```settings.gradle```:
    ```gradle
    include 'assignmentX'
    ```

- Create a ```build.gradle``` inside the folder, add:
    ```gradle
    plugins { id 'application' } 
    application { mainClass = 'your.package.Main' }
    ```

- All source code for each project will go inside a src/main/java/packagename directory