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
import java.util.List;
import java.util.ArrayList;

/**
 * A skeletal implementation of a Runner to help creating runners as easily as
 * possible.
 */
public abstract class AbstractRunner implements Runner {
    
    private final int id;
    private final Maze maze;
    private final List<MazeEntity> goals;
    
    /**
     * Creates a new runner with the given id and maze.
     * 
     * @param id    Id of the runner.
     * @param maze  Maze according to which the runner should make its moves.
     */
    public AbstractRunner(int id, Maze maze) {
        this.id = id;
        this.maze = maze;
        this.goals = new ArrayList<>();
    }
    
    @Override
    public int getId() {
        return this.id;
    }
    
    @Override
    public Maze getMaze() {
        return this.maze;
    }
    
    @Override
    public void addGoal(MazeEntity goal) {
        this.goals.add(goal);
    }
    
    @Override
    public void removeGoal(MazeEntity goal) {
        this.goals.remove(goal);
    }
    
    @Override
    public List<MazeEntity> getGoals() {
        return this.goals;
    }

    @Override
    public abstract RunnerDirection getNextMove();

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
    
}
