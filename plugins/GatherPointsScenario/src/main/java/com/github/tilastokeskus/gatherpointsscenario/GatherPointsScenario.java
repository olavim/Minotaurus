package com.github.tilastokeskus.gatherpointsscenario;

import com.github.tilastokeskus.minotaurus.maze.Maze;
import com.github.tilastokeskus.minotaurus.maze.MazeBlock;
import com.github.tilastokeskus.minotaurus.maze.MazeEntity;
import com.github.tilastokeskus.minotaurus.scenario.AbstractScenario;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import com.github.tilastokeskus.minotaurus.runner.Runner;
import com.github.tilastokeskus.minotaurus.util.ArrayList;

public class GatherPointsScenario extends AbstractScenario {

    private static final int MIN_RUNNERS = 2;
    private static final int MAX_RUNNERS = 4;

    private List<Runner> runners;
    private MazeEntity goal;
    
    /**
     * Creates a new TestScenario. In this scenario, a goal is randomly
     * spawned in the maze and the runners' job is to gather it. When the goal
     * is gathered, a new one spawns in an unoccupied space.
     */
    public GatherPointsScenario() {
        goal = new MazeEntity(0, 0);
        runners = new ArrayList<>();
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
    public boolean placeRunners(Collection<Runner> r) {
        this.runners.addAll(r);
        
        if (runners.size() > 0)
            runners.get(0).setPosition(1, 1);
        if (runners.size() > 1)
            runners.get(1).setPosition(maze.getWidth() - 2, maze.getHeight() - 2);
        if (runners.size() > 2)
            runners.get(2).setPosition(1, maze.getHeight() - 2);        
        if (runners.size() > 3)
            runners.get(3).setPosition(maze.getHeight() - 2, 1);
        return true;
    }

    @Override
    public boolean isCollisionAllowed(MazeEntity ent1, MazeEntity ent2) {
        return true;
    }

    @Override
    public boolean handleCollision(MazeEntity ent1, MazeEntity ent2) {
        if (ent1 != goal && ent2 != goal)
            return true;
        
        Runner r = (Runner) (ent1 instanceof Runner ? ent1 : ent2);

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
        } while (maze.get(x, y) != MazeBlock.FLOOR);
        
        goal.setPosition(x, y);
    }
    
}
