package com.github.tilastokeskus.gatherpointsscenario;

import com.github.tilastokeskus.minotaurus.maze.Maze;
import com.github.tilastokeskus.minotaurus.maze.MazeEntity;
import com.github.tilastokeskus.minotaurus.runner.Runner;
import com.github.tilastokeskus.minotaurus.scenario.AbstractScenario;
import com.github.tilastokeskus.minotaurus.util.ColorFactory;
import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class GatherPointsScenario extends AbstractScenario {
    
    private static final int MIN_RUNNERS = 2;
    private static final int MAX_RUNNERS = 4;

    private Collection<Runner> runners;
    private MazeEntity goal;
    
    /**
     * Creates a new GatherPointsScenario. In this scenario, a goal is randomly
     * spawned in the maze and the runners' job is to gather it. When the goal
     * is gathered, a new one spawns in an unoccupied space.
     */
    public GatherPointsScenario() {
        this.goal = new Goal(0, 0);
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
        this.runners = runners;
        for (Runner runner : runners)
            maze.addEntity((MazeEntity) runner);
        
        return true;
    }

    @Override
    public boolean isCollisionAllowed(MazeEntity ent1, MazeEntity ent2) {
        return !(runners.contains(ent1) || runners.contains(ent2));
    }

    @Override
    public boolean handleCollision(MazeEntity ent1, MazeEntity ent2) {
        if (!isCollisionAllowed(ent1, ent2))
            throw new IllegalStateException("Collision between runners is not allowed in this scenario.");
        
        if (ent1 == goal || ent2 == goal) {
            Runner r = (Runner) ent1;
            if (!runners.contains(r))
                r = (Runner) ent2;
            
            if (!this.score.containsKey(r))
                this.score.put(r, 1);
            this.score.put(r, this.score.get(r) + 1);
            
            resetGoal();
        }
        
        return true;
    }

    @Override
    public List<MazeEntity> getRunnerGoals(Runner runner) {
        return Arrays.asList(goal);
    }
    
    private void resetGoal() {
        int id = getMaxRunners() + 1;
        
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
