## Structure of the program

The project is divided in clearly named packages and *plugins*. The important thing to know about the project is the way simulations are built. The main project consists of only the core classes and user interface classes.

All maze generation, rule and path finding related classes are in separate projects. These projects are packaged in separate Jar files and then loaded by the main project when put in the appropriate directory. More specifically a simulation consists of an implementation of the `MazeGenerator`, `Scenario` and `Runner` classes.

- A `MazeGenerator` generates the maze, or the arena of the simulation.
- A `Scenario` is the ruleset of the simulation, and specifies things like the allowed number of runners, what should happen when a runner collides with an entity (such as some other runner), scoring and so on. A scenario is allowed to modify the maze, and in some cases, such as adding/removing goals, removing runners on collison etc., it's required to do so.
- A `Runner` is an entity in the maze that can move according to its own will. A runner is the actual algorithmic focus point of this project.

## Accomplished time and space requirements

Space requirement stays at `O(n)` for all of the implemented data structures.
Accomplished time requirements are as follows:

|       | Access | Search | Insertion | Deletion |
|-------|--------|--------|-----------|----------|
| **ArrayList**  | O(1)   | O(n)   | O(n)      | O(n)     |
| **HashMap**   | O(1)      | O(1)   | O(1)      | O(1)     |
| **HashSet**   | -      | O(1)   | O(1)      | O(1)     |
| **Stack** | O(1)   | O(n)   | O(1)      | O(1)     |
| **Priority Queue** | O(1)   | O(n)   | O(log n)      | O(n)     |

Some exceptions apply:

- `ArrayList`'s insertion and deletion operations have `O(1)` time complexity when applied at the end of the list.
- `HashMap`'s and `HashSet`'s operations have amortized `O(1)` time complexity; in the worst case scenario all operations have `O(n)` complexity if all elements end up in the same bucket.
- `Stack`'s access, insertion and deletion only apply to the top element of the stack.
- `PriorityQueue`'s access can only access the minimum element in the queue. `PriorityQueue` also has a special operation, `extractMin`, which accesses and removes the minimum element in the queue. `extractMin`'s time requirement is `O(log n)`.
