/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.tilastokeskus.snakescenario;

import com.github.tilastokeskus.minotaurus.maze.Maze;
import com.github.tilastokeskus.minotaurus.maze.MazeBlock;
import com.github.tilastokeskus.minotaurus.maze.MazeEntity;
import com.github.tilastokeskus.minotaurus.runner.Runner;
import com.github.tilastokeskus.minotaurus.scenario.AbstractScenario;
import com.github.tilastokeskus.minotaurus.scenario.Setting;
import com.github.tilastokeskus.minotaurus.util.ArrayList;
import com.github.tilastokeskus.minotaurus.util.Direction;
import com.github.tilastokeskus.minotaurus.util.HashMap;
import com.github.tilastokeskus.minotaurus.util.Position;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;

/**
 * In this scenario runners act like in the game snake; collision with self or
 * other snakes result in the runner dying/losing the game, and collision with
 * the goal (or the "apple") increments the gatherer's score and length.
 */
public class SnakeScenario extends AbstractScenario {
    
    private static final int MIN_RUNNERS = 1;
    private static final int MAX_RUNNERS = 4;
    
    private final HashMap<Runner, List<MazeEntity>> snakes;
    private final MazeEntity goal;
    
    public SnakeScenario() {
        snakes = new HashMap<>();
        goal = new MazeEntity(0, 0);
    }
    
    @Override
    public void setMaze(Maze maze) {
        super.setMaze(maze);
        maze.addEntity(goal);
        resetGoal(goal);
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
        int posi = 0;
        int pos[][] = new int[][] {
            {1, 1}, 
            {maze.getWidth() - 2, maze.getHeight() - 2}, 
            {1, maze.getHeight() - 2}, 
            {maze.getHeight() - 2, 1}
        };
        
        for (Runner r : runners) {
            r.setPosition(pos[posi][0], pos[posi][1]);
            posi++;
            List<MazeEntity> a = new ArrayList<>();
            a.add(r);
            snakes.put(r, a);
        }
        
        return true;
    }

    @Override
    public boolean isCollisionAllowed(MazeEntity ent1, MazeEntity ent2) {
        return ent1 == goal || ent2 == goal;
    }

    @Override
    public boolean handleRunnerMove(Runner runner, Direction dir) {
        
        int x = runner.getPosition().x;
        int y = runner.getPosition().y;
            
        /* Calculate the position of the runner after moving to said
         * direction
         */
        int nx = x + dir.deltaX;
        int ny = y + dir.deltaY;

        // If the runner tries to move illegally, skip its turn.
        if (maze.get(nx, ny) == MazeBlock.WALL)
            return false;

        List<MazeEntity> entities = maze.getEntitiesAt(nx, ny);
        boolean collisionAllowed = entities.isEmpty() 
                || (entities.size() == 1 && entities.get(0) == goal);

        /* If the runner tries to move illegally on top of some entity with
         * whom collision is not allowed, return false.
         */
        if (!collisionAllowed)
            return false;
        
        runner.setPosition(nx, ny);
        List<MazeEntity> snake = snakes.get(runner);
        if (snake.size() > 1) {
            MazeEntity tail = snake.remove(0);
            tail.setPosition(x, y);
            snake.add(snake.size() - 1, tail);
        }

        if (entities.size() == 1)
            handleGoalCollision(runner);

        return true;
    }
    
    private void handleGoalCollision(Runner runner) {
        setScore(runner, getScore(runner) + 1);
        resetGoal(goal);
        List<MazeEntity> snake = snakes.get(runner);
        Position tailPos = snake.get(0).getPosition();
        MazeEntity newTail = new MazeEntity(tailPos.x, tailPos.y, runner.getShapeColor());
        snake.add(0, newTail);
        maze.addEntity(newTail);
    }

    @Override
    public List<MazeEntity> getRunnerGoals(Runner runner) {
        return Arrays.asList(goal);
    }

    @Override
    public Map<String, Setting> getModifiableSettings() {
        return null;
    }

    @Override
    public Predicate<Position> getPositionPredicate(Runner runner) {
        return pos -> {
            List<MazeEntity> entities = maze.getEntitiesAt(pos.x, pos.y);
            if ((entities.size() == 1 && entities.get(0) != goal) || entities.size() > 1)
                return false;
            
            return maze.get(pos.x, pos.y) == MazeBlock.FLOOR;
        };
    }
    
    private void resetGoal(MazeEntity goal) {
        Random r = new Random();
        int x, y;
        do {
            x = r.nextInt(maze.getWidth());
            y = r.nextInt(maze.getHeight());
        } while (maze.get(x, y) != MazeBlock.FLOOR || maze.getEntitiesAt(x, y).size() > 0);
        
        goal.setPosition(x, y);
    }

}
