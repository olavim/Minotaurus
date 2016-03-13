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
