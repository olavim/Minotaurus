/*
 * The MIT License
 *
 * Copyright 2016 Olavi Mustanoja.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.tilastokeskus.minotaurus.maze;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Olavi Mustanoja
 */
public class MazeTest {
    
    Maze maze;
    
    public MazeTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        MazeEntity[][] layout = new MazeEntity[][] {{MazeEntity.FLOOR},
                                                    {MazeEntity.GOAL}, 
                                                    {MazeEntity.RUNNER}};
        
        maze = new Maze(layout);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void mazeShouldHaveCorrectDimensions() {
        Maze maze;
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                maze = new Maze(i, j);
                assertTrue(maze.getWidth() == i);
                assertTrue(maze.getHeight() == j);
                
                maze = new Maze(new MazeEntity[j][i]);
                assertTrue(maze.getWidth() == i);
                assertTrue(maze.getHeight() == j);
            }
        }
    }
    
    @Test
    public void mazeShouldHaveCorrectLayout() {
        assertTrue(maze.get(0, 0) == MazeEntity.FLOOR);
        assertTrue(maze.get(0, 1) == MazeEntity.GOAL);
        assertTrue(maze.get(0, 2) == MazeEntity.RUNNER);
    }
    
    @Test
    public void mazeShouldHaveCorrectLayoutAfterChangingSomeEntities() {
        maze.set(0, 0, MazeEntity.WALL);
        maze.set(0, 2, MazeEntity.WALL);
        assertTrue(maze.get(0, 0) == MazeEntity.WALL);
        assertTrue(maze.get(0, 1) == MazeEntity.GOAL);
        assertTrue(maze.get(0, 2) == MazeEntity.WALL);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void mazeSetShouldHaveThrowInvalidArgumentExceptionWhenOutOfBounds() {
        maze.set(1, 0, MazeEntity.WALL);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void mazeGetShouldHaveThrowInvalidArgumentExceptionWhenOutOfBounds() {
        maze.get(1, 0);
    }
    
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
