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

import static com.github.tilastokeskus.minotaurus.exception.ThrowableAssertion.assertThrown;
import com.github.tilastokeskus.minotaurus.util.Position;
import java.awt.Color;
import java.awt.Point;
import java.util.Arrays;
import java.util.Random;
import javax.swing.JComponent;
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
    public void mazeShouldChangeNullBlocksToWall() {
        Maze m = new Maze(2, 2);
        assertTrue(m.get(0, 0) == MazeBlock.WALL);
        assertTrue(m.get(0, 1) == MazeBlock.WALL);
        assertTrue(m.get(1, 0) == MazeBlock.WALL);
        assertTrue(m.get(1, 1) == MazeBlock.WALL);
        
        Maze m2 = new Maze(new MazeBlock[][]{{null}});
        assertTrue(m2.get(0, 0) == MazeBlock.WALL);
        
        m2.setLayout(new MazeBlock[][]{{null}});
        assertTrue(m2.get(0, 0) == MazeBlock.WALL);
    }
    
    @Test
    public void mazeShouldHaveCorrectLayoutAfterChangingSomeEntities() {
        maze.set(0, 0, MazeBlock.WALL);
        maze.set(0, 2, MazeBlock.WALL);
        assertTrue(maze.get(0, 0) == MazeBlock.WALL);
        assertTrue(maze.get(0, 1) == MazeBlock.FLOOR);
        assertTrue(maze.get(0, 2) == MazeBlock.WALL);
    }
    
    @Test
    public void mazeSetShouldHaveThrowInvalidArgumentExceptionWhenOutOfBounds() {
        assertThrown(() -> maze.set(1, 0, MazeBlock.WALL))
                .expect(IllegalArgumentException.class);
        assertThrown(() -> maze.set(-1, 0, MazeBlock.WALL))
                .expect(IllegalArgumentException.class);
        assertThrown(() -> maze.set(0, 3, MazeBlock.WALL))
                .expect(IllegalArgumentException.class);
        assertThrown(() -> maze.set(0, -1, MazeBlock.WALL))
                .expect(IllegalArgumentException.class);
    }
    
    @Test
    public void mazeGetShouldHaveThrowInvalidArgumentExceptionWhenOutOfBounds() {
        assertThrown(() -> maze.get(1, 0))
                .expect(IllegalArgumentException.class);
        assertThrown(() -> maze.get(-1, 0))
                .expect(IllegalArgumentException.class);
        assertThrown(() -> maze.get(0, 3))
                .expect(IllegalArgumentException.class);
        assertThrown(() -> maze.get(0, -1))
                .expect(IllegalArgumentException.class);
    }
    
    @Test
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
    
    @Test
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
    
    @Test
    public void mazeAddEntityShouldAddEntity() {
        MockEntity ent = new MockEntity(5, 5);
        
        assertTrue(maze.getEntitiesAt(5, 5).isEmpty());
        maze.addEntity(ent);
        assertTrue(maze.getEntitiesAt(5, 5).get(0) == ent);
    }
    
    @Test
    public void mazeSetEntitiesShouldSetEntities() {
        MockEntity ent1 = new MockEntity(5, 5);
        MockEntity ent2 = new MockEntity(3, 4);
        MockEntity ent3 = new MockEntity(4, 3);
        MockEntity ent4 = new MockEntity(3, 4);
        
        maze.addEntity(ent1);
        assertTrue(maze.getEntitiesAt(5, 5).get(0) == ent1);
        maze.setEntities(Arrays.asList(ent2, ent3, ent4));
        assertTrue(maze.getEntitiesAt(5, 5).isEmpty());
        assertTrue(maze.getEntitiesAt(3, 4).contains(ent2));
        assertTrue(maze.getEntitiesAt(3, 4).contains(ent4));
        assertTrue(maze.getEntitiesAt(4, 3).get(0) == ent3);
    }
    
    @Test
    public void mazeRemoveEntityShouldRemoveEntity() {
        MockEntity ent1 = new MockEntity(5, 5);
        MockEntity ent2 = new MockEntity(3, 4);
        MockEntity ent3 = new MockEntity(3, 4);
        
        maze.addEntity(ent1);
        maze.addEntity(ent2);
        maze.addEntity(ent3);
        assertTrue(maze.getEntitiesAt(5, 5).get(0) == ent1);
        assertTrue(maze.getEntitiesAt(3, 4).contains(ent2));
        assertTrue(maze.getEntitiesAt(3, 4).contains(ent3));
        
        maze.removeEntity(ent1);
        assertTrue(maze.getEntitiesAt(5, 5).isEmpty());
        maze.removeEntity(ent2);
        assertTrue(maze.getEntitiesAt(3, 4).size() == 1 && maze.getEntitiesAt(3, 4).get(0) == ent3);
        maze.removeEntity(ent3);
        assertTrue(maze.getEntitiesAt(3, 4).isEmpty());
    }
    
    @Test
    public void mazeShouldBeSetAsAnEntitysObserverWhenEntityIsAdded() {
        MockEntity ent = new MockEntity(5, 5);   
        maze.addEntity(ent);
        assertTrue(ent.countObservers() == 1);
    }
    
    @Test
    public void mazeShouldBeRemovedFromEntitysObserverWhenEntityIsRemoved() {
        MockEntity ent = new MockEntity(5, 5);   
        maze.addEntity(ent);
        assertTrue(ent.countObservers() == 1);
        maze.removeEntity(ent);
        assertTrue(ent.countObservers() == 0);
    }
    
    @Test
    public void mazeChangesInEntityPositionsShouldBeVisibleToMaze() {
        MockEntity ent = new MockEntity(5, 5);   
        maze.addEntity(ent);
        
        Random r = new Random();
        Position p = ent.getPosition();
        Position oldP;
        for (int i = 0; i < 1000; i++) {
            oldP = ent.getPosition();
            assertTrue(maze.getEntitiesAt(oldP.x, oldP.y).contains(ent));
            
            while (p.equals(oldP))
                p = new Position(r.nextInt(10), r.nextInt(10));
            
            ent.setPosition(p.x, p.y);
            assertFalse(maze.getEntitiesAt(oldP.x, oldP.y).contains(ent));
            assertTrue(maze.getEntitiesAt(p.x, p.y).contains(ent));            
        }
    }
    
    private class MockEntity extends MazeEntity {
        public MockEntity(int x, int y) {
            super(x, y);
        }
    }
}
