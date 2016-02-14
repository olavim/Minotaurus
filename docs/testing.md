## What has been tested

Tests are currently focused on the core elements of the program. Most of the classes are currently interfaces and user interface
classes, which will not be tested. Some of the less important classes, such as `ColorFactory`, as well as trivial classes such as
`ResourceManager`, `MazeBlock` and `Direction` will not be tested.

By core elements I'm talking about the plugin loader, which consists of `JarClassLoader` and `PluginLoader`, data structures, which at the moment consists of `LinkedListStack`, and the `Maze` class.

Later on more data structures will be added and then tested, and all the implementations of `Scenario`, `MazeGenerator` and `Runner` will be thorougly tested.

