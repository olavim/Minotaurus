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

package com.github.tilastokeskus.minotaurus.runner;

import com.github.tilastokeskus.minotaurus.Main;
import com.github.tilastokeskus.minotaurus.maze.Maze;
import com.github.tilastokeskus.minotaurus.maze.MazeEntity;
import com.github.tilastokeskus.minotaurus.maze.TestMazeGenerator;
import com.github.tilastokeskus.minotaurus.plugin.Plugin;
import com.github.tilastokeskus.minotaurus.scenario.TestScenario;
import com.github.tilastokeskus.minotaurus.util.ColorFactory;
import com.github.tilastokeskus.minotaurus.util.Direction;
import java.awt.Color;
import java.awt.Point;
import java.util.Arrays;
import java.util.Collection;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An artificial intelligence doing various tasks in a set maze.
 */
public abstract class Runner extends MazeEntity implements Plugin {
    
    public final static Logger LOGGER = Logger.getLogger(Runner.class.getName());
    
    /**
     * Creates an instance of {@code clazz} and generates a maze with it.
     * A window will be opened showcasing the generated maze.
     * 
     * @param clazz Class object to create an instance of.
     * @param width Width of the maze to generate.
     * @param height Height of the maze to generate.
     */
    public static void testGenerator(Class<? extends Runner> clazz,
            int width, int height) {
        try {
            Runner runner = clazz.newInstance();
            Main.startSimulation(new TestMazeGenerator(),
                                    new TestScenario(),
                                    Arrays.asList(runner));
        } catch (InstantiationException | IllegalAccessException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    private int id;
    private String title;
    
    private final Color color;
    private final float size;
    
    public Runner() {
        color = ColorFactory.getNextColor();
        size = 0.5f;
    }
    
    @Override
    public Color getColor() {
        return this.color;
    }
    
    @Override
    public float getSize() {
        return this.size;
    }    
    
    /**
     * Sets this runner's identifier.
     * 
     * @param id New identifier.
     */
    public void setId(int id) {
        this.id = id;
    }
    
    
    /**
     * Returns this runner's identifier.
     * 
     * @return The identifier.
     */
    public int getId() {
        return this.id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Runner other = (Runner) obj;
        return this.id == other.getId();
    }
    
    @Override
    public void setTitle(String title) {
        this.title = title;
    }
    
    @Override
    public String toString() {
        return this.title;
    }
    
    /**
     * Returns the direction in which the runner wants to go next in the
     * specified maze, with the specified goals.
     * 
     * @param maze Maze to navigate.
     * @param goals Goals to aim for.
     * @return A direction.
     */
    public abstract Direction getNextMove(Maze maze, Collection<MazeEntity> goals);
}