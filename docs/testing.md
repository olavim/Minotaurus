## What has been tested

Tests are currently focused on the core elements of the program. Most of the classes are currently interfaces and user interface
classes, which will not be tested. Some of the less important classes, such as `ColorFactory`, as well as trivial classes such as
`ResourceManager`, `MazeBlock` and `Direction` will not be tested.

By core elements I'm talking about the plugin loader, which consists of `JarClassLoader` and `PluginLoader`, data structures, which at the moment consists of `LinkedListStack`, and the `Maze` class.

Later on more data structures will be added and then tested, and all the implementations of `Scenario`, `MazeGenerator` and `Runner` will be thorougly tested.

## How has it been tested

All tests are written with JUnit. Below are more details about each unit test, sectioned by classes.

#### Maze

**Maze should have correct dimensions**

Tested that when a Maze is initialized, its `getWidth` and `getHeight` returns the correct values.
Tested by initializing Mazes with different dimensions ranging from width and height 0 to width and height 10.

**Maze should have correct layout**

Tested that after initializing a maze with a layout, the `get` method returns a correct block.
