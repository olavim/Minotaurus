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

package com.github.tilastokeskus.minotaurus.scenario;

import com.github.tilastokeskus.minotaurus.maze.Maze;
import java.util.Map;
import com.github.tilastokeskus.minotaurus.runner.Runner;
import com.github.tilastokeskus.minotaurus.util.HashMap;

/**
 * A skeletal implementation of a Scenario to help creating scenarios as easily
 * as possible.
 */
public abstract class AbstractScenario implements Scenario {
    
    protected Map<Runner, Integer> scoreMap;    
    protected Maze maze;    
    protected String title;
    
    public AbstractScenario() {
        this.scoreMap = new HashMap<>();
    }
    
    @Override
    public void reset() {
        this.scoreMap = new HashMap<>();
        this.maze = null;
        _reset();
    }
    
    /**
     * Initializes the scenario to the state it was in when it was first created.
     */
    public abstract void _reset();
    
    @Override
    public void setMaze(Maze maze) {
        this.maze = maze;
    }
    
    @Override
    public int getScore(Runner runner) {
        return scoreMap.containsKey(runner) ? scoreMap.get(runner) : 0;
    }
    
    @Override
    public int setScore(Runner runner, int score) {
        int oldScore = scoreMap.containsKey(runner) ? scoreMap.get(runner) : 0;
        scoreMap.put(runner, score);
        return oldScore;
    }
    
    @Override
    public void setTitle(String title) {
        this.title = title;
    }
    
    @Override
    public String toString() {
        return this.title;
    }
    
    @Override
    public AbstractScenario clone() {
        try {
            AbstractScenario clone = (AbstractScenario) super.clone();
            clone.scoreMap = new HashMap<>();
            clone.title = title;
            if (maze != null)
                clone.maze = maze.clone();
            return clone;
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }
}
