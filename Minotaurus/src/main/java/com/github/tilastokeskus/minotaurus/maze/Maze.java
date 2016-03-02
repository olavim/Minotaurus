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

import com.github.tilastokeskus.minotaurus.util.ArrayList;
import com.github.tilastokeskus.minotaurus.util.HashMap;
import com.github.tilastokeskus.minotaurus.util.Position;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Maze implements Observer {
    
    final Map<Position, List<MazeEntity>> entityMap;
    final Set<MazeEntity> entitySet;    
    final MazeBlock[][] layout;
    
    /**
     * Creates a new maze with the given layout.
     * 
     * @param layout Layout of the new maze.
     */
    public Maze(MazeBlock[][] layout) {      
        this.layout = copyLayout(layout);
        checkValidity(this.layout);
        entityMap = new HashMap<>();
        entitySet = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }
    
    /**
     * Creates a new maze with the given width and height.
     * 
     * @param width     Width of the maze.
     * @param height    Height of the maze.
     */
    public Maze(int width, int height) {
        this.layout = new MazeBlock[height][width];
        checkValidity(this.layout);
        entityMap = new HashMap<>();
        entitySet = new HashSet<>();
    }
    
    /**
     * Sets the maze's current layout.
     * 
     * @param layout  New layout.
     */
    protected void setLayout(MazeBlock[][] layout) {
        layout = copyLayout(layout);
        checkValidity(layout);
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
    public MazeBlock[][] getLayout() {
        return copyLayout(layout);
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
        layout[y][x] = block;
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
        return layout[y][x];
    }
    
    /**
     * Gets entities at the specified position.
     * 
     * @param x X position to search.
     * @param y Y position to search.
     * @return  A list of MazeEntities.
     */
    public List<MazeEntity> getEntitiesAt(int x, int y) {
        Position p = new Position(x, y);
        if (!entityMap.containsKey(p))
            entityMap.put(p, new ArrayList<>());
        return new ArrayList<>(entityMap.get(p));
    }
    
    /**
     * Returns a list of all the entities in the maze.
     * 
     * @return A list of MazeEntities.
     */
    public Set<MazeEntity> getEntities() {
        return new HashSet<>(entitySet);
    }
    
    /**
     * Adds a MazeEntity to this maze, and adds this maze to that entity's
     * list of observers.
     * 
     * @param ent MazeEntity to add.
     */
    public void addEntity(MazeEntity ent) {
        Position p = ent.getPosition();
        
        if (!entityMap.containsKey(p))
            entityMap.put(p, new ArrayList<>());
        
        entityMap.get(p).add(ent);        
        entitySet.add(ent);
        
        ent.addObserver(this);
    }
    
    /**
     * Removes existing entities from this maze, removes this maze from each 
     * of those entities' list of observers and adds the given entities to this
     * maze.
     * 
     * @param entities List of MazeEntities to set.
     */
    public void setEntities(List<MazeEntity> entities) {
        entityMap.clear();
        
        for (MazeEntity ent : entitySet)
            ent.deleteObserver(this);
            
        entitySet.clear();
        
        for (MazeEntity ent : entities)
            addEntity(ent);
    }
    
    /**
     * Removes an entity from this maze, and removes this maze from the entity's
     * list of observers
     * 
     * @param ent Entity to remove.
     */
    public void removeEntity(MazeEntity ent) {
        if (entityMap.containsKey(ent.getPosition()))
            entityMap.get(ent.getPosition()).remove(ent);
        entitySet.remove(ent);
        
        ent.deleteObserver(this);
    }
    
    void testBounds(int x, int y) {
        if (x < 0 || x >= getWidth())
            throw new IllegalArgumentException("Index out of bounds: x " + x);
        if (y < 0 || y >= getHeight())
            throw new IllegalArgumentException("Index out of bounds: y " + y);
    }
    
    final void checkValidity(MazeBlock[][] layout) {
        for (int i = 0; i < layout.length; i++)
            for (int j = 0; j < layout[i].length; j++)
                if (layout[i][j] == null)
                    layout[i][j] = MazeBlock.WALL;
    }
    
    MazeBlock[][] copyLayout(MazeBlock[][] layout) {
        MazeBlock[][] copy = new MazeBlock[layout.length][];
        for (int i = 0; i < layout.length; i++)
            copy[i] = Arrays.copyOf(layout[i], layout[i].length);
        
        return copy;
    }

    @Override
    public void update(Observable o, Object arg) {
        MazeEntity ent = (MazeEntity) o;
        Position oldPos = (Position) arg;
        entityMap.get(oldPos).remove(ent);
        addEntity(ent);
    }

}
