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
        MazeBlock[][] layout = new MazeBlock[][] {{MazeBlock.WALL},
                                                    {MazeBlock.FLOOR}, 
                                                    {MazeBlock.ENTITY}};
        
        maze = new Maze(layout);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void mazeShouldHaveCorrectDimensions() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                maze = new Maze(i, j);
                assertTrue(maze.getWidth() == i);
                assertTrue(maze.getHeight() == j);
                
                maze = new Maze(new MazeBlock[j][i]);
                assertTrue(maze.getWidth() == i);
                assertTrue(maze.getHeight() == j);
            }
        }
    }
    
    @Test
    public void mazeShouldHaveCorrectLayout() {
        assertTrue(maze.get(0, 0) == MazeBlock.WALL);
        assertTrue(maze.get(0, 1) == MazeBlock.FLOOR);
        assertTrue(maze.get(0, 2) == MazeBlock.ENTITY);
    }
    
    @Test
    public void mazeShouldHaveCorrectLayoutAfterChangingSomeEntities() {
        maze.set(0, 0, MazeBlock.WALL);
        maze.set(0, 2, MazeBlock.WALL);
        assertTrue(maze.get(0, 0) == MazeBlock.WALL);
        assertTrue(maze.get(0, 1) == MazeBlock.FLOOR);
        assertTrue(maze.get(0, 2) == MazeBlock.WALL);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void mazeSetShouldHaveThrowInvalidArgumentExceptionWhenOutOfBounds() {
        maze.set(1, 0, MazeBlock.WALL);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void mazeGetShouldHaveThrowInvalidArgumentExceptionWhenOutOfBounds() {
        maze.get(1, 0);
    }
    
    public void mazeShouldNotStoreOriginalArrayReference() {
        MazeBlock[][] layout = new MazeBlock[][] {{MazeBlock.WALL},
                                                    {MazeBlock.FLOOR}, 
                                                    {MazeBlock.ENTITY}};
        
        Maze m = new Maze(layout);
        assertTrue(m.get(0, 0) == MazeBlock.WALL);
        layout[0][0] = MazeBlock.ENTITY;
        assertTrue(m.get(0, 0) == MazeBlock.WALL);
        m.set(0, 0, MazeBlock.FLOOR);
        assertTrue(layout[0][0] == MazeBlock.ENTITY);
    }
    
    public void mazeSetLayoutShouldOnlyStoreCopyOfLayout() {
        MazeBlock[][] layout = new MazeBlock[][] {{MazeBlock.WALL},
                                                    {MazeBlock.FLOOR}, 
                                                    {MazeBlock.ENTITY}};
        
        maze.setLayout(layout);
        assertTrue(maze.get(0, 0) == MazeBlock.WALL);
        layout[0][0] = MazeBlock.ENTITY;
        assertTrue(maze.get(0, 0) == MazeBlock.WALL);
        maze.set(0, 0, MazeBlock.FLOOR);
        assertTrue(layout[0][0] == MazeBlock.ENTITY);
    }
}
