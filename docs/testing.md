## What has been tested

Tests are currently focused on the core elements of the program. Most of the classes are currently interfaces and user interface
classes, which will not be tested. Some of the less important classes, such as `ColorFactory`, as well as trivial classes such as
`ResourceManager`, `MazeBlock` and `Direction` will not be tested.

By core elements I'm talking about the plugin loader, which consists of `JarClassLoader` and `PluginLoader`, data structures, which at the moment consists of `LinkedListStack`, and the `Maze` class.

Later on more data structures will be added and then tested, and all the implementations of `Scenario`, `MazeGenerator` and `Runner` will be thorougly tested.

## How has it been tested

All tests are written with JUnit. The tests themselves are tested with the help of PIT mutation testing.

Below are more details about each classes' tests, more specifically, what has been tested.

#### Maze

- Maze should have correct dimensions after initialization.
- Maze should have correct layout after initialization. `null` blocks should be changed to `MazeBlock.WALL`s.
- When the maze is initialized with a layout matrix, changes in the original matrix should not be visible in the maze.
- `set`
  - Should correctly set blocks. `null` blocks should be changed to `MazeBlock.WALL`s.
- `set`, `get`
  - Maze should throw an exception when trying to access out of bounds locations.
- `addEntity`
  - Should add an entity to the maze.
  - The maze should be set as the entity's observer.
- `setEntities`
  - Should clear previous entities and add new ones.
- `removeEntity`
  - Should remove entity from the maze.
  - Maze should no longer be the entity's observer.
- When an entity's position is modified, it should be visible to the maze. More of an integration test between `Maze` and `MazeEntity`.

## How you can test it

As tests are written with JUnit, testing is as simple as downloading the project and running the tests.
