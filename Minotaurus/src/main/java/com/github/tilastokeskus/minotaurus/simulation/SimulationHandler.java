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
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.tilastokeskus.minotaurus.simulation;

import com.github.tilastokeskus.minotaurus.maze.Maze;
import com.github.tilastokeskus.minotaurus.maze.MazeBlock;
import com.github.tilastokeskus.minotaurus.maze.MazeEntity;
import com.github.tilastokeskus.minotaurus.maze.MazeGenerator;
import com.github.tilastokeskus.minotaurus.scenario.Scenario;
import com.github.tilastokeskus.minotaurus.util.Direction;
import java.util.List;
import java.util.Observable;
import com.github.tilastokeskus.minotaurus.runner.Runner;

public class SimulationHandler extends Observable {
    
    private final Maze maze;
    private final Scenario scenario;
    private final List<Runner> runners;

    /**
     * Creates a new simulation handler with the given maze generator, scenario
     * and runners.
     * 
     * @param gen Maze generator to use.
     * @param scenario Scenario to use.
     * @param runners Runners to use.
     */
    public SimulationHandler(MazeGenerator gen, Scenario scenario, List<Runner> runners) {
        this.maze = gen.generateMaze(50, 50);
        this.scenario = scenario;
        this.runners = runners;
    }
    
    /**
     * Starts the simulation.
     * 
     * @param rate Delay, in milliseconds, between each move.
     */
    public void startSimulation(int rate) {       
        this.scenario.setMaze(maze);
        this.scenario.placeRunners(runners);
        
        for (Runner runner : runners)
            maze.addEntity(runner);
        
        while (true) {
            try {
                Thread.sleep(rate);
            } catch (InterruptedException ex) {
            }
            
            simulateRound();
        }
    }
    
    private void simulateRound() {
        for (Runner runner : runners) {
            Direction dir = runner.getNextMove(maze, scenario.getRunnerGoals(runner));
            int nx = runner.getPosition().x + dir.deltaX;
            int ny = runner.getPosition().y + dir.deltaY;
            
            if (maze.get(nx, ny) == MazeBlock.WALL)
                continue;
            
            System.out.println(dir);
            
            List<MazeEntity> entities = maze.getEntitiesAt(nx, ny);
            if (entities != null) {
                boolean collisionAllowedAll = entities.stream()
                        .allMatch(e -> scenario.isCollisionAllowed(runner, e));

                if (!collisionAllowedAll)
                    continue;

                for (MazeEntity ent : entities)
                    scenario.handleCollision(runner, ent);
            }

            runner.setPosition(nx, ny);
        }
        
        this.setChanged();
        this.notifyObservers();
    }
    
    public Maze getMaze() {
        return maze;
    }
    
}
