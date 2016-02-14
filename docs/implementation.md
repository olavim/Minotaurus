## Structure of the program

The project is divided in clearly named packages and *plugins*. The important thing to know about the project is the way simulations are built. The main project consists of only the core classes and user interface classes.

All maze generation, rule and path finding related classes are in separate projects. These projects are packaged in separate Jar files and then loaded by the main project when put in the appropriate directory. More specifically a simulation consists of a 
