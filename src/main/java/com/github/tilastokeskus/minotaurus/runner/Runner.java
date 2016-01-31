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
import com.github.tilastokeskus.minotaurus.plugin.Plugin;
import java.util.List;
import java.util.ArrayList;

/**
 * A Runner is an artificial intelligence moving in a Maze according to its own
 * rules. A runner's job is to find out where it wants to go next, and nothing
 * else.
 */
public abstract class Runner implements Plugin {
    
    private final int id;
    private final Maze maze;
    private List<RunnerGoal> goals;
    
    /**
     * Creates a new runner with the given id and maze.
     * 
     * @param id    Id of the runner.
     * @param maze  Maze according to which the runner should make its moves.
     */
    public Runner(int id, Maze maze) {
        this.id = id;
        this.maze = maze;
        this.goals = new ArrayList<>();
    }
    
    /**
     * Returns the identifier of this runner.
     * 
     * @return The runner's id.
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * Returns the maze which is associated with this runner.
     * 
     * @return A Maze.
     */
    public Maze getMaze() {
        return this.maze;
    }
    
    /**
     * Adds a goal, or an objective, for the runner.
     * 
     * @param goal Goal or objective to add.
     */
    public void addGoal(RunnerGoal goal) {
        this.goals.add(goal);
    }
    
    /**
     * Removes a goal, or an objective, from the runner's list of goals.
     * 
     * @param goal Goal or objective to remove.
     */
    public void removeGoal(RunnerGoal goal) {
        this.goals.remove(goal);
    }
    
    /**
     * Returns a list of all the goals, or objectives, assigned to this runner.
     * 
     * @return A list of goals or objectives.
     */
    public List<RunnerGoal> getGoals() {
        return this.goals;
    }
    
    /**
     * Returns the direction in which the runner wants to go next.
     * 
     * @return A direction.
     */
    public abstract RunnerDirection getNextMove();
    
}
