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

public class Maze {
    
    private MazeEntity[][] layout;
    
    /**
     * Creates a new maze with the given layout.
     * 
     * @param layout Layout of the new maze.
     */
    public Maze(MazeEntity[][] layout) {
        this.layout = layout;
        checkValidity();
    }
    
    /**
     * Creates a new maze with the given width and height.
     * 
     * @param width     Width of the maze.
     * @param height    Height of the maze.
     */
    public Maze(int width, int height) {
        this.layout = new MazeEntity[height][width];
        checkValidity();
    }
    
    /**
     * Sets the maze's layout.
     * 
     * @param layout  New layout.
     */
    protected void setLayout(MazeEntity[][] layout) {
        this.layout = layout;
        checkValidity();
    }
    
    /**
     * Returns the width of the maze, as specified by the current layout.
     * 
     * @return Width of the maze.
     */
    public int getWidth() {
        return this.layout[0].length;
    }
    
    /**
     * Returns the height of the maze, as specified by the current layout.
     * 
     * @return Height of the maze.
     */
    public int getHeight() {
        return this.layout.length;
    }
    
    /**
     * Returns the current layout of the maze, which is a 2D-matrix of
     * MazeEntities.
     * 
     * @return 2D MazeEntity matrix.
     */
    protected MazeEntity[][] getLayout() {
        return this.layout;
    }
    
    /**
     * Alters the layout by changing the entity in the specified location.
     * 
     * @param x         Location in x-axis.
     * @param y         Location in y-axis.
     * @param entity    Entity to write to the location.
     */
    protected void set(int x, int y, MazeEntity entity) {
        testBounds(x, y);
        this.layout[y][x] = entity;
    }
    
    /**
     * Retrieves an entity from the maze in the specified location.
     * 
     * @param x         Location in x-axis.
     * @param y         Location in y-axis.
     * @return          Entity in the specified location.
     */
    public MazeEntity get(int x, int y) {
        testBounds(x, y);
        return this.layout[y][x];
    }
    
    private void testBounds(int x, int y) {
        if (x < 0 || x >= getWidth())
            throw new IllegalArgumentException("Index out of bounds: " + x);
        if (y < 0 || y >= getHeight())
            throw new IllegalArgumentException("Index out of bounds: " + y);
    }
    
    private void checkValidity() {
        for (int i = 0; i < getWidth(); i++)
            for (int j = 0; j < getHeight(); j++)
                if (layout[i][j] == null)
                    layout[i][j] = MazeEntity.WALL;
    }

}
