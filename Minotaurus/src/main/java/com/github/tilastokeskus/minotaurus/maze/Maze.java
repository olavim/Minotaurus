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

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class Maze implements Observer {
    
    private final MazeBlock[][] initialLayout;
    
    private MazeBlock[][] currentLayout;
    private Map<Point, List<MazeEntity>> entities;
    
    /**
     * Creates a new maze with the given layout.
     * 
     * @param layout Layout of the new maze.
     */
    public Maze(MazeBlock[][] layout) {
        initialLayout = copyLayout(layout);        
        currentLayout = copyLayout(layout);
        checkValidity(initialLayout);
        checkValidity(currentLayout);
        entities = new HashMap<>();
    }
    
    /**
     * Creates a new maze with the given width and height.
     * 
     * @param width     Width of the maze.
     * @param height    Height of the maze.
     */
    public Maze(int width, int height) {
        initialLayout = new MazeBlock[height][width];
        currentLayout = new MazeBlock[height][width];
        entities = new HashMap<>();
        checkValidity(initialLayout);
        checkValidity(currentLayout);
    }
    
    /**
     * Sets the maze's current layout.
     * 
     * @param layout  New layout.
     */
    protected void setLayout(MazeBlock[][] layout) {
        currentLayout = copyLayout(layout);
        checkValidity(currentLayout);
    }
    
    /**
     * Returns the width of the maze, as specified by the current layout.
     * 
     * @return Width of the maze.
     */
    public int getWidth() {
        return this.currentLayout[0].length;
    }
    
    /**
     * Returns the height of the maze, as specified by the current layout.
     * 
     * @return Height of the maze.
     */
    public int getHeight() {
        return this.currentLayout.length;
    }
    
    /**
     * Returns the current layout of the maze, which is a 2D-matrix of
     * MazeEntities.
     * 
     * @return 2D MazeEntity matrix.
     */
    protected MazeBlock[][] getLayout() {
        return copyLayout(currentLayout);
    }
    
    /**
     * Alters the layout by changing the block in the specified location.
     * 
     * @param x         Location in x-axis.
     * @param y         Location in y-axis.
     * @param block    Entity to write to the location.
     */
    protected void set(int x, int y, MazeBlock block) {
        testBounds(x, y);
        currentLayout[y][x] = block;
    }
    
    /**
     * Retrieves a block from the maze in the specified location.
     * 
     * @param x         Location in x-axis.
     * @param y         Location in y-axis.
     * @return          Block in the specified location.
     */
    public MazeBlock get(int x, int y) {
        testBounds(x, y);
        return currentLayout[y][x];
    }
    
    /**
     * Gets entities at the specified position.
     * 
     * @param x X position to search.
     * @param y Y position to search.
     * @return  A list of MazeEntities, or null if the position contains no
     *          entities.
     */
    public List<MazeEntity> getEntitiesAt(int x, int y) {
        return entities.get(new Point(x, y));
    }
    
    public void addEntity(MazeEntity ent) {
        Point p = ent.getPosition();
        if (!entities.containsKey(p))
            entities.put(p, new ArrayList<>());
        entities.get(p).add(ent);
        ent.addObserver(this);
    }
    
    public void setEntities(List<MazeEntity> entities) {
        this.entities = new HashMap<>();
        for (MazeEntity ent : entities)
            addEntity(ent);
    }
    
    public void removeEntity(MazeEntity ent) {
        entities.get(ent.getPosition()).remove(ent);
        ent.deleteObserver(this);
    }
    
    private void testBounds(int x, int y) {
        if (x < 0 || x >= getWidth())
            throw new IllegalArgumentException("Index out of bounds: x " + x);
        if (y < 0 || y >= getHeight())
            throw new IllegalArgumentException("Index out of bounds: y " + y);
    }
    
    private void checkValidity(MazeBlock[][] layout) {
        for (int i = 0; i < layout.length; i++)
            for (int j = 0; j < layout[i].length; j++)
                if (layout[i][j] == null)
                    layout[i][j] = MazeBlock.WALL;
    }
    
    private MazeBlock[][] copyLayout(MazeBlock[][] layout) {
        MazeBlock[][] copy = new MazeBlock[layout.length][];
        for (int i = 0; i < layout.length; i++)
            copy[i] = Arrays.copyOf(layout[i], layout[i].length);
        
        return copy;
    }

    @Override
    public void update(Observable o, Object arg) {
        MazeEntity ent = (MazeEntity) o;
        Point oldPos = (Point) arg;
        entities.get(oldPos).remove(ent);
        addEntity(ent);
    }

}
