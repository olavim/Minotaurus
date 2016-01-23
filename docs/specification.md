### Algorithms and data structures

This is a tricky question to answer, since, as I will elaborate in the next topic, there will not be a known set of algorithms that will be used. The same goes for data structures, as they are dependent on the algorithms used. It is however probably safe to assume that there will be implementations of the `set`, `list`, `queue`and `stack` data structures. As optimizations come into play, the `priority queue` will very likely make its debut as well.

### Focus of this project

The focus will be in comparing how different AIs perform in different mazes, when they are given different goals. An iconic setting would be when there are two entities: The minotaur and Theseus. The minotaur's goal is to get to Theseus, while Theseus' goal is to find a way ot of the maze.

This is only one of endless possibilities. What about four entities trying to eat as much apples they can, or the minotaur setting, except there are four humans and no exits; the winner is the last human standing.

The focus is to rate different AIs in these kinds of settings.

The goal is to have the program easily extendable, whether it came to different settings or AIs. The core program should be a platform that can be given different rules and contenders. In other words, the core program shouldn't have to be compiled again each time new rules or a new contender is added; they should be loaded dynamically.

### Input

The program will have a graphical user interface in the end.

### Time and space requirement objectives

For now I'll list the objectives for the data structures I mentioned in the first topic:

**Set**
- O(1) time for insertion, deletion and searching.

**List**
- O(1) for access.
- O(n) for insertion, deletion and searching.
 
**Queue**
- O(1) for insertion and deletion.
 
**Stack**
- O(1) for insertion and deletion.
