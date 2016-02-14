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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Maze {
    
    private MazeBlock[][] initialLayout;
    private MazeBlock[][] currentLayout;
    private List<MazeEntity> entities;
    
    /**
     * Creates a new maze with the given layout.
     * 
     * @param layout Layout of the new maze.
     */
    public Maze(MazeBlock[][] layout) {
        initialLayout = copyLayout(layout);        
        currentLayout = layout;
        entities = new ArrayList<>();
        checkValidity();
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
        entities = new ArrayList<>();
        checkValidity();
    }
    
    /**
     * Sets the maze's layout.
     * 
     * @param layout  New layout.
     */
    protected void setLayout(MazeBlock[][] layout) {
        this.currentLayout = layout;
        checkValidity();
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
    
    public void updateEntityPositions() {
        currentLayout = copyLayout(initialLayout);
        for (MazeEntity ent : entities)
            currentLayout[ent.getX()][ent.getY()] = MazeBlock.ENTITY;
    }
    
    public List<MazeEntity> getEntities() {
        return this.entities;
    }
    
    /**
     * Gets an entity at the specified position. If the position has more than
     * one entities, any of them will be returned.
     * 
     * @param x X position to search.
     * @param y Y position to search.
     * @return  A MazeEntity.
     */
    public MazeEntity getEntity(int x, int y) {
        return entities.stream()
                .filter(e -> e.getX() == x && e.getY() == y)
                .findFirst().orElse(null);
    }
    
    public void addEntity(MazeEntity ent) {
        entities.add(ent);
        updateEntityPositions();
    }
    
    public void setEntities(List<MazeEntity> entities) {
        this.entities = entities;
        updateEntityPositions();
    }
    
    public void removeEntity(MazeEntity ent) {
        entities.remove(ent);
        updateEntityPositions();
    }
    
    public boolean isOccupied(int x, int y) {
        return currentLayout[y][x] != MazeBlock.FLOOR;
    }
    
    private void testBounds(int x, int y) {
        if (x < 0 || x >= getWidth())
            throw new IllegalArgumentException("Index out of bounds: x " + x);
        if (y < 0 || y >= getHeight())
            throw new IllegalArgumentException("Index out of bounds: y " + y);
    }
    
    private void checkValidity() {
        for (int i = 0; i < getWidth(); i++)
            for (int j = 0; j < getHeight(); j++)
                if (currentLayout[j][i] == null)
                    currentLayout[j][i] = MazeBlock.WALL;
    }
    
    private MazeBlock[][] copyLayout(MazeBlock[][] layout) {
        MazeBlock[][] copy = new MazeBlock[layout.length][];
        for (int i = 0; i < layout.length; i++)
            copy[i] = Arrays.copyOf(layout[i], layout[i].length);
        
        return copy;
    }

}
