## What has been tested

Tests are currently focused on the core elements of the program. Most of the classes are currently interfaces and user interface
classes, which will not be tested. Some of the less important classes, such as `ColorFactory`, as well as trivial classes such as
`ResourceManager`, `MazeBlock` and `Direction` will not be tested.

By core elements I'm talking about the plugin loader, which consists of `JarClassLoader` and `PluginLoader`, data structures, which at the moment consists of `LinkedListStack`, and the `Maze` class.

Later on more data structures will be added and then tested, and all the implementations of `Scenario`, `MazeGenerator` and `Runner` will be thorougly tested.

## How has it been tested

All tests are written with JUnit. The tests themselves are tested with the help of PIT mutation testing.

Below are more details about each classes' tests, more specifically, what has been tested. I have listed only tests that test public methods (and constructors).

#### Maze

- Maze should have correct dimensions after initialization.
- Maze should have correct layout after initialization. `null` blocks should be changed to `MazeBlock.WALL`s.
- When the maze is initialized with a layout matrix, changes in the original matrix should not be visible in the maze.
- `set()`
  - Should correctly set blocks. `null` blocks should be changed to `MazeBlock.WALL`s.
- `set()`, `get()`
  - Maze should throw an exception when trying to access out of bounds locations.
- `addEntity()`
  - Should add an entity to the maze.
  - The maze should be set as the entity's observer.
- `setEntities()`
  - Should clear previous entities and add new ones.
- `removeEntity()`
  - Should remove entity from the maze.
  - Maze should no longer be the entity's observer.
- When an entity's position is modified, it should be visible to the maze. More of an integration test between `Maze` and `MazeEntity`.
 
#### HashMap

- HashMap should initialize as an empty map. That is, `isEmpty()` should return *true*, and `size()` should return *0*.
- `isEmpty()`
  - Should return *true* when map is empty, and *false* when not.
- `size()`
  - Should return correct amount of entries in the map.
- `clear()`
  - Should remove all entries from the map.
- `put()`
  - Should add a new entry, or update the value of an existing key.
  - Should return previously stored value, or *null* if the key is new.
- `get()`
  - Should return correct value associated with a key, or *null* if key didn't exist.
- `containsKey()`, `containsValue()`
  - Should *return* true if key or value is present in the map, and *false* otherwise.
- `remove()`
  - Should remove entry if key existed.
  - Should return the previously stored value if key existed, or *null* otherwise.
- `putAll()`
  - Should put all entries in some other map to *this* map.
- `keySet()`
  - Should return a set with all keys in the map.
- `values()`
  - Should return a collection with all values in the map.

#### HashSet

- HashSet should initialize as an empty set.
- `isEmpty()`
  - Should return *true* when set is empty, and *false* when not.
- `size()`
  - Should return correct amount of elements in the set.
- `clear()`
  - Should remove all elements from the set.
- `contains()`
  - Should return *true* if element exists, *false* if not.
- `containsAll()`
  - Should return *true* if all elements exists in set, *false* if not.
- `add()`
  - Should add an the element to the set.
  - Should return *true* if the element didn't exist, *false* if it did.
- `addAll()`
  - Should add all elements in a collection to the set.
  - Should return *true* if at least one element was new, *false* otherwise.
- `retainAll()`
  - Should remove all elements from the set, except for those present in the specified collection.
  - Should return *true* if the set was modified, *false* otherwise.
- `removeAll()`
  - Should remove all elements from the set that are also present in the specified collection.
  - Should return *true* if the set was modified, *false* otherwise.
- `iterator()`
  - Should return an iterator over the elements in the set.
- `toArray()`
  - Should return an array from the set's elements.
  
## Benchmarks

#### HashMap

The class has been benchmarked with integer key/values and string key/values. All benchmarks are an average of hundred times a million consecutive calls. For example to test `put`, we call it a million times and record the time it took to finish those calls. We do this a hundred times and take the average. We also do a warmup period that is identical to the benchmark before timing.

| Operation | Integers - Time | Strings - Time |
|-----------|----------------|-----------------|
Put | 30ms | 222ms 
Contains | 8ms | 52ms
Get | 7ms | 47ms
Remove | 8ms | 81ms

The reason why operating on String key/values is more expensive is because of the additional time it takes to calculate its hash. If the map was tested with objects that have simple hashcode calculations, the times would most likely be more or less identical to the integer map.

The reason why `put` is, in both cases, more expensive than the other operations is because occasionally the map's internal data array is grown, which takes linear time proportional to its new size. With a million entries, the array is grown 16 times.

#### PriorityQueue

Similar to benchmarking HashMap, all benchmarks are an average of numerous consecutive calls to an operation. Because `contains` and `remove` are slow operations, both having a worst case of `O(n)` time complexity, all operations were tested with only 100000 elements.

| Operation | Integers - Time | Strings - Time |
|-----------|----------------|-----------------|
Add | 2ms | 4ms 
Contains | 5432ms | 33297ms
ExtractMin | 19ms | 45ms
Remove | 23ms | 18072ms

`Remove` is faster than `contains` because, after each operation, the queue is left with one less element.

## How you can test it

As tests are written with JUnit, testing is as simple as downloading the project and running the tests.
