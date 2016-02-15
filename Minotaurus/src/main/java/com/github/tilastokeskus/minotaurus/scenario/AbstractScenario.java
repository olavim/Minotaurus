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
import java.util.HashMap;
import java.util.Map;
import com.github.tilastokeskus.minotaurus.runner.Runner;

/**
 * A skeletal implementation of a Scenario to help creating scenarios as easily
 * as possible.
 */
public abstract class AbstractScenario implements Scenario {
    
    protected final Map<Runner, Integer> score;
    
    protected Maze maze;
    
    private String title;
    
    public AbstractScenario() {
        this.score = new HashMap<>();
    }
    
    @Override
    public void setMaze(Maze maze) {
        this.maze = maze;
    }
    
    @Override
    public int getScore(Runner runner) {
        return score.get(runner);
    }
    
    @Override
    public void setTitle(String title) {
        this.title = title;
    }
    
    @Override
    public String toString() {
        return this.title;
    }
}
