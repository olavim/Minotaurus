## How to use the main program

After running the program from the command line, or double-clicking the executable Jar, the main menu pops up. Here you can find the following sections, from top to bottom:

- Maze Generator
- Scenario
- Runners
- Start
 
#### Maze Generator

Maze Generator is the component that generates the playfield. The playfield might be an empty arena, having walls only in the edges of the field. It might be a maze with complex paths from one point to another. Whatever it is, the chosen maze generator determines how the field is constructed.

To choose a maze generator, click on the menu icon on the right edge below the **Maze Generator** label.

#### Scenario

Scenario is the ruleset of the simulation. The scenario defines the following things:

- Minimum and maximum allowed runners.
- Is collision allowed between two entities.
- Is a runner allowed to visit a specific position in a maze.
- What is done to a runner when it wishes to move to some direction.
- How runners should be placed in the maze initially.
- What are the goals of each runner.
- How are runners scored.
 
A scenario might also have additional responsibilities, such as inserting entities in the maze.

To choose a scenario, click on the menu icon on the right edge below the **Maze Generator** label. Depending on the scenario chosen, a settings icon might appear next to the menu icon. Click on it to change scenario-specific settings.

#### Runners

A runner is an artificial intelligence moving in the maze. In most cases it's nothing more than some path finding algorithm moving towards the closest goal it can find, but depending on the scenario it's intended for, it might be a little more complicated than that.

To add a runner, click on the **Add Runner** button. To specify a runner's algorithm, click on the menu icon on the appropriate row. To remove a runner, click on the delete icon on the appropriate row.

#### Start

Clicking on this button starts the simulation, given that all required components are in place. A new window will open showcasing the simulation. Below the simulation panel are each runners' current score, labeled by the associated runner's color and title.

To end the simulation abruptly, simply close the simulation window.

## Implementing plugins

Implementing plugins requires **Java 8** and the main program as a dependency. After these are met, implementing plugins is quite straight forward.

#### MazeGenerator

To implement a `MazeGenerator`, refer to the below example:

```java
public class ArenaMazeGenerator extends AbstractMazeGenerator {

    @Override
    public Maze generateMaze(int width, int height) {
        MazeBlock[][] layout = new MazeBlock[height][width];
        
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                layout[i][j] = isBoundary(j, i, width, height) ?
                        MazeBlock.WALL :
                        MazeBlock.FLOOR;
        
        return new Maze(layout);
    }
    
    private boolean isBoundary(int x, int y, int w, int h) {
        return x == 0 || x == w - 1 || y == 0 || y == h - 1;
    }

}
```

The above example generates an arena-like maze, everything being floor except for the boundaries, which are wall.

The `generateMaze` returns a `Maze` object, which can be constructed with a 2D `MazeBlock` matrix. `MazeBlock` is an enum class containing the `Mazeblock.FLOOR` and `MazeBlock.WALL` enums. What these signify should be self-explanatory.
