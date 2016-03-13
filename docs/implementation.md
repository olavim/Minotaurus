## Structure of the program

The project is divided in clearly named packages and *plugins*. The important thing to know about the project is the way simulations are built. The main project consists of only the core classes and user interface classes.

All maze generation, rule and path finding related classes are in separate projects. These projects are packaged in separate Jar files and then loaded by the main project when put in the appropriate directory. More specifically a simulation consists of an implementation of the `MazeGenerator`, `Scenario` and `Runner` classes.

- A `MazeGenerator` generates the maze, or the arena of the simulation.
- A `Scenario` is the ruleset of the simulation, and specifies things like the allowed number of runners, what should happen when a runner collides with an entity (such as some other runner), moving runners according to their wishes, scoring and so on. A scenario is allowed to modify the maze, and in some cases, such as adding/removing goals, removing runners on collison etc., it is required to do so.
- A `Runner` is an entity in the maze that can (try to) move according to its own will. A runner is the actual algorithmic focus point of this project.

## Accomplished time and space requirements

Space requirement stays at `O(n)` for all of the implemented data structures.
Accomplished time requirements are as follows:

|       | Access | Search | Insertion | Deletion |
|-------|--------|--------|-----------|----------|
| **ArrayList**  | O(1)   | O(n)   | O(n)      | O(n)     |
| **HashMap**   | O(1)      | O(1)   | O(1)      | O(1)     |
| **HashSet**   | -      | O(1)   | O(1)      | O(1)     |
| **Stack** | O(1)   | O(n)   | O(1)      | O(1)     |
| **PriorityQueue** | O(1)   | O(n)   | O(log n)      | O(n)     |

Some exceptions apply:

- `ArrayList`'s insertion and deletion operations have `O(1)` time complexity when applied at the end of the list.
- `HashMap`'s and `HashSet`'s operations have amortized `O(1)` time complexity; in the worst case scenario all operations have `O(n)` complexity if all elements end up in the same bucket.
- `Stack`'s access, insertion and deletion only apply to the top element of the stack.
- `PriorityQueue`'s access can only access the minimum element in the queue. `PriorityQueue` also has a special operation, `extractMin`, which accesses and removes the minimum element in the queue. `extractMin`'s time requirement is `O(log n)`.

## Performance

Benchmarks for `HashMap` and `PriorityQueue` are located in the [**testing docs**](testing.md) 

Compared to Java's implementations of `ArrayList`, `HashMap`, `Stack` and `PriorityQueue`, my implementations are close to identical in performance. My implementations have a very slight edge performance wise in some cases due to less bloated code, but it is negligible.

The only data structure that has undergone significant changes is `Stack`, which originally had an array in its core. The array would have had to been grown from time to time, making the `add` operation have only an amortized `O(1)` time requirement. The class was changed to a linked list based one, which simplified the implementation significantly and quarantees a true `O(1)` time requirement for the `add` operation.

I haven't considered any real optimizations for the other data structures, except maybe for `PriorityQueue`.

`HashMap`'s own rehash algorithm could perhaps be improved, but it would have to be tested against so much real world data that I simply won't bother. Within the constraints of this program, the data is distributed amongst different buckets equally well with my and Java's implementation, so I can only draw the conclusion that improving the rehash function wouldn't lead to any real performance gains. Reading Java's `HashMap` documentation on its rehash function, the sole purpose of the function is to better distribute elements amongst buckets when the elements *have a poor hashCode function*. I have used NetBean's automatic hashcode function generation, and it seems to be doing a decent job.

`PriorityQueue` is currently backed by a *binary heap*. `PriorityQueue` isn't designed for searching elements, and as such the `contains` and `remove` operations have `O(n)` time requirements for non-minimum elements. This could in fact be slightly improved to have just a `O(n/2)` time requirement, with the tradeoff of a bit more required space and a slightly greater constant factor for time. The other possible improvement is to change the class to be backed by a *fibonacci heap*. I never tried it because it would have taken too much time to do, and I was afraid the performance improvements wouldn't have been real due to much bigger constant factors.
