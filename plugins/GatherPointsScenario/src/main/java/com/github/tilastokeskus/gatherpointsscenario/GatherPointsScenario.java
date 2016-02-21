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

public class GatherPointsScenario extends AbstractScenario {

    private static final int MIN_RUNNERS = 2;
    private static final int MAX_RUNNERS = 4;

    private Runner runner;
    private MazeEntity goal;
    
    /**
     * Creates a new TestScenario. In this scenario, a goal is randomly
     * spawned in the maze and the runners' job is to gather it. When the goal
     * is gathered, a new one spawns in an unoccupied space.
     */
    public GatherPointsScenario() {
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
        } while (maze.get(x, y) != MazeBlock.FLOOR);
        
        goal.setPosition(x, y);
    }
    
}
