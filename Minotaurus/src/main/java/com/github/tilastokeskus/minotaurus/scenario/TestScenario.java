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
import com.github.tilastokeskus.minotaurus.maze.MazeEntity;
import com.github.tilastokeskus.minotaurus.runner.Runner;
import com.github.tilastokeskus.minotaurus.util.ColorFactory;
import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * A Scenario intended for testing runners with.
 */
public class TestScenario extends AbstractScenario {

    private static final int MIN_RUNNERS = 1;
    private static final int MAX_RUNNERS = 1;

    private Runner runner;
    private MazeEntity goal;
    
    /**
     * Creates a new TestScenario. In this scenario, a goal is randomly
     * spawned in the maze and the runners' job is to gather it. When the goal
     * is gathered, a new one spawns in an unoccupied space.
     */
    public TestScenario() {
        goal = new Goal(0, 0);
    }
    
    @Override
    public void setMaze(Maze maze) {
        super.setMaze(maze);
        maze.addEntity(goal);
        resetGoal();
    }

    @Override
    public int getMinRunners() {
        return MIN_RUNNERS;
    }

    @Override
    public int getMaxRunners() {
        return MAX_RUNNERS;
    }

    @Override
    public boolean placeRunners(Collection<Runner> runners) {
        runner = runners.iterator().next();
        runner.setX(1);
        runner.setY(1);
        return true;
    }

    @Override
    public boolean isCollisionAllowed(MazeEntity ent1, MazeEntity ent2) {
        return true;
    }

    @Override
    public boolean handleCollision(MazeEntity ent1, MazeEntity ent2) {      
        Runner r = (Runner) (runner == ent1 ? ent1 : ent2);

        if (!score.containsKey(r))
            score.put(r, 1);
        score.put(r, score.get(r) + 1);

        resetGoal();        
        return true;
    }

    @Override
    public List<MazeEntity> getRunnerGoals(Runner runner) {
        return Arrays.asList(goal);
    }
    
    private void resetGoal() {
        Random r = new Random();
        int x, y;
        do {
            x = r.nextInt(maze.getWidth());
            y = r.nextInt(maze.getHeight());
        } while (maze.isOccupied(x, y));
        
        goal.setX(x);
        goal.setY(y);
    }
    
    private class Goal implements MazeEntity {
        private final Color color;
        private final float size;
        
        private int x;
        private int y;
        
        public Goal(int x, int y) {
            this.x = x;
            this.y = y;
            this.color = ColorFactory.getNextColor();
            this.size = 0.5f;
        }
        
        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }        

        @Override public void setX(int x) {
            this.x = x;
        }
        
        @Override public void setY(int y) {
            this.y = y;
        }

        @Override
        public Color getColor() {
            return this.color;
        }

        @Override
        public float getSize() {
            return this.size;
        }
    }
    
}
