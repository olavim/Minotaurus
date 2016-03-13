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
import com.github.tilastokeskus.minotaurus.maze.MazeBlock;
import com.github.tilastokeskus.minotaurus.maze.MazeEntity;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import com.github.tilastokeskus.minotaurus.runner.Runner;
import com.github.tilastokeskus.minotaurus.util.Direction;
import com.github.tilastokeskus.minotaurus.util.Position;
import java.util.Map;
import java.util.function.Predicate;

/**
 * A Scenario intended for testing runners with. In this scenario, a goal is
 * randomly spawned in the maze and the runners' job is to gather it. When the
 * goal is gathered, a new one spawns in an unoccupied space.
 */
public class TestScenario extends AbstractScenario {

    private static final int MIN_RUNNERS = 1;
    private static final int MAX_RUNNERS = 1;

    private Runner runner;
    private MazeEntity goal;
    
    /**
     * Creates a new TestScenario.
     */
    public TestScenario() {
        goal = new MazeEntity(0, 0);
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
        runner.setPosition(1, 1);
        return true;
    }

    @Override
    public boolean isCollisionAllowed(MazeEntity ent1, MazeEntity ent2) {
        return true;
    }

    @Override
    public boolean handleRunnerMove(Runner runner, Direction dir) {
            
        /* Calculate the position of the runner after moving to said
         * direction
         */
        int nx = runner.getPosition().x + dir.deltaX;
        int ny = runner.getPosition().y + dir.deltaY;

        // If the runner tries to move illegally, skip its turn.
        if (maze.get(nx, ny) == MazeBlock.WALL
                || !maze.getEntitiesAt(nx, ny).isEmpty())
            return false;
        
        setScore(runner, getScore(runner) + 1);
        resetGoal();
        runner.setPosition(nx, ny);    
        return true;
    }

    @Override
    public List<MazeEntity> getRunnerGoals(Runner runner) {
        return Arrays.asList(goal);
    }

    @Override
    public Predicate<Position> getPositionPredicate(Runner runner) {
        return pos -> maze.get(pos.x, pos.y) == MazeBlock.FLOOR;
    }
    
    private void resetGoal() {
        
        /* Randomly select a position in the maze until we find
         * an unoccupied one.
         */
        Random r = new Random();
        int x, y;
        do {
            x = r.nextInt(maze.getWidth());
            y = r.nextInt(maze.getHeight());
        } while (maze.get(x, y) != MazeBlock.FLOOR);
        
        goal.setPosition(x, y);
    }

    @Override
    public Map<String, Setting> getModifiableSettings() {
        return null;
    }
    
}
