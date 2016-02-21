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
import com.github.tilastokeskus.minotaurus.plugin.Plugin;
import java.util.Collection;
import java.util.List;
import com.github.tilastokeskus.minotaurus.runner.Runner;

/**
 * A scenario is a situation, or a world, that a simulation aims to model. A
 * scenario answers questions such as what is each runner's role, what do they
 * aim for, what is allowed and how they are rewarded for their actions.
 */
public interface Scenario extends Plugin {
    
    void setMaze(Maze maze);
    
    /**
     * Returns the runner's current score.
     * 
     * @param runner A Runner.
     * @return       The Runner's current score.
     */
    int getScore(Runner runner);
    
    /**
     * Returns the minimum amount of runners required for using this scenario.
     * 
     * @return Minimum amount of runners.
     */
    int getMinRunners();
    
    /**
     * Returns the maximum amount of runners allowed for using this scenario, or
     * 0 for no maximum.
     * 
     * @return Maximum amount of runners.
     */
    int getMaxRunners();
    
    /**
     * Places the given runners in a maze.
     * 
     * @param  runners 
     * @return True if the runners were placed successfully, false otherwise.
     */
    boolean placeRunners(Collection<Runner> runners);
    
    /**
     * Returns whether or not the given two entities are allowed to be on the
     * same location, to collide.
     * 
     * @param ent1 A maze entity.
     * @param ent2 A maze entity.
     * @return     True if collision is allowed between the entities, false
     *             otherwise.
     */
    boolean isCollisionAllowed(MazeEntity ent1, MazeEntity ent2);
    
    /**
     * Handles a collision between two entities. Specifies what should happen
     * when an entity is on top of another. If collision between the entities
     * is not allowed, nothing should happen.
     * 
     * @param  ent1 A maze entity.
     * @param  ent2 A maze entity.
     * @return True if the collision was handled, false otherwise.
     */
    boolean handleCollision(MazeEntity ent1, MazeEntity ent2);
    
    /**
     * Returns the goals of the specified runner.
     * 
     * @param runner A Runner.
     * @return A list of goals the runner should aim for.
     */
    List<MazeEntity> getRunnerGoals(Runner runner);

}
