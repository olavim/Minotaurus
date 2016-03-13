## What has been tested

Tests are currently focused on the core elements of the program. Most of the classes are currently interfaces and user interface
classes, which will not be tested. Some of the less important classes, such as `ColorFactory`, as well as trivial classes such as
`ResourceManager`, `MazeBlock` and `Direction` will not be tested.

By core elements I'm talking about the plugin loader, which consists of `JarClassLoader` and `PluginLoader`, data structures, which at the moment consists of `LinkedListStack`, and the `Maze` class.

Later on more data structures will be added and then tested, and all the implementations of `Scenario`, `MazeGenerator` and `Runner` will be thorougly tested.

## How has it been tested

All tests are written with JUnit. The tests themselves are tested with the help of PIT mutation testing. For more specific info about what has been tested and how, refer to the actual tests located [here](../Minotaurus/src/test).
  
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

Similar to benchmarking HashMap, all benchmarks are an average of numerous consecutive calls to an operation. Because `contains` and `remove` are slow operations, both having a worst case of `O(n)` time complexity, all operations were tested with only 100000 elements. So for example `contains` was called 100000 times on a queue that has 100000 elements.

| Operation | Integers - Time | Strings - Time |
|-----------|----------------|-----------------|
Add | 2ms | 4ms 
Contains | 5432ms | 33297ms
ExtractMin | 19ms | 45ms
Remove | 23ms | 18072ms

`Remove` is faster than `contains` because, after each operation, the queue is left with one less element.

## How you can test it

As tests are written with JUnit, testing is as simple as downloading the project and running the tests.
