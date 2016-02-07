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

import com.github.tilastokeskus.minotaurus.maze.Maze;
import com.github.tilastokeskus.minotaurus.maze.MazeEntity;
import com.github.tilastokeskus.minotaurus.plugin.Plugin;
import java.util.List;

/**
 * An artificial intelligence doing various tasks in a set maze.
 */
public interface Runner extends Plugin, MazeEntity {
    
    /**
     * Returns the maze which is associated with this runner.
     * 
     * @return A Maze.
     */
    Maze getMaze();
    
    /**
     * Adds a goal, or an objective, for the runner.
     * 
     * @param goal Goal or objective to add.
     */
    void addGoal(MazeEntity goal);
    
    /**
     * Removes a goal, or an objective, from the runner's list of goals.
     * 
     * @param goal Goal or objective to remove.
     */
    void removeGoal(MazeEntity goal);
    
    /**
     * Returns a list of all the goals, or objectives, assigned to this runner.
     * 
     * @return A list of goals or objectives.
     */
    List<MazeEntity> getGoals();
    
    /**
     * Returns the direction in which the runner wants to go next.
     * 
     * @return A direction.
     */
    RunnerDirection getNextMove();
}
