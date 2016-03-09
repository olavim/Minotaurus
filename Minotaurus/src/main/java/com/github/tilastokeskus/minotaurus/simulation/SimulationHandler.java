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
import com.github.tilastokeskus.minotaurus.util.ArrayList;
import com.github.tilastokeskus.minotaurus.util.Rotation;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simulation handler is the mediator between a MazeGenerator, Scenario and
 * a set of Runners. Its job is to figure out each runner's preferred move each
 * turn, and move them if it's allowed.
 */
public class SimulationHandler extends Observable {
    
    private static final Logger LOGGER = Logger.getLogger(SimulationHandler.class.getName());
    
    private final Maze maze;
    private final Scenario scenario;
    private final List<Runner> runners;
    private ScheduledExecutorService executor;

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
        
        executor = Executors.newSingleThreadScheduledExecutor();
    }
    
    /**
     * Starts the simulation with the specified speed.
     * 
     * @param rate Delay, in milliseconds, between each move.
     */
    public void startSimulation(int rate) {
        
        // Initialize the scenario and ask it to place the runners in the maze.
        this.scenario.setMaze(maze);
        this.scenario.placeRunners(runners);
        
        // Place runners in the maze.
        for (Runner runner : runners)
            maze.addEntity(runner);
        
        executor.scheduleWithFixedDelay(() -> {
            
            // Wrap call to try-catch, since Future devours exceptions.
            try {
                simulateRound();
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "", ex);
            }
        }, rate, rate, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Simulates a single round, a round being when all runners have moved once.
     */
    private void simulateRound() {
        for (Runner runner : runners) {
            
            // Get the direction the runner wants to go next.
            Direction dir = runner.getNextMove(maze, scenario.getRunnerGoals(runner));
            
            boolean didMove = scenario.handleRunnerMove(runner, dir);
            
            if (didMove) {
            
                // Update the runner to point in the direction it moved.
                switch (dir) {
                    case UP:
                        runner.setRotation(Rotation.UP.angle);
                        break;
                    case RIGHT:
                        runner.setRotation(Rotation.RIGHT.angle);
                        break;
                    case DOWN:
                        runner.setRotation(Rotation.DOWN.angle);
                        break;
                    default:
                        runner.setRotation(Rotation.LEFT.angle);
                        break;
                }
            }
        }
        
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Stops the simulation.
     */
    public void stop() {
        executor.shutdown();
    }
    
    public Maze getMaze() {
        return maze;
    }
    
    public List<Runner> getRunners() {
        return new ArrayList<>(runners);
    }
    
    public Scenario getScenario() {
        return scenario;
    }
    
}
