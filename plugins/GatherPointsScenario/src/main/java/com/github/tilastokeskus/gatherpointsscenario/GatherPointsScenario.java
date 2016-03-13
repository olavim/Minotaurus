package com.github.tilastokeskus.gatherpointsscenario;

import com.github.tilastokeskus.minotaurus.maze.Maze;
import com.github.tilastokeskus.minotaurus.maze.MazeBlock;
import com.github.tilastokeskus.minotaurus.maze.MazeEntity;
import com.github.tilastokeskus.minotaurus.scenario.AbstractScenario;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import com.github.tilastokeskus.minotaurus.runner.Runner;
import com.github.tilastokeskus.minotaurus.scenario.Setting;
import com.github.tilastokeskus.minotaurus.util.ArrayList;
import com.github.tilastokeskus.minotaurus.util.Direction;
import com.github.tilastokeskus.minotaurus.util.HashMap;
import com.github.tilastokeskus.minotaurus.util.Position;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Predicate;

public class GatherPointsScenario extends AbstractScenario implements Observer {

    private static final int MIN_RUNNERS = 1;
    private static final int MAX_RUNNERS = 4;

    private Map<String, Setting> modifiableSettings;    
    private List<Runner> runners;
    private List<MazeEntity> goals;
    
    /**
     * Creates a new TestScenario. In this scenario, a goal is randomly
     * spawned in the maze and the runners' job is to gather it. When the goal
     * is gathered, a new one spawns in an unoccupied space.
     */
    public GatherPointsScenario() {
        goals = new ArrayList<>();
        runners = new ArrayList<>();
        
        modifiableSettings = new HashMap<>();
        modifiableSettings.put("goals", new Setting<Integer>(
                Integer.class,
                "Number of goals in the maze",
                v -> (v > 0 && v <= 10),
                Integer::parseInt));        
        modifiableSettings.get("goals").addObserver(this);
        modifiableSettings.get("goals").setValue(1);
    }
    
    @Override
    public void _reset() {
        goals = new ArrayList<>();
        runners = new ArrayList<>();
        
        modifiableSettings = new HashMap<>();
        modifiableSettings.put("goals", new Setting<Integer>(
                Integer.class,
                "Number of goals in the maze",
                v -> (v > 0 && v <= 10),
                Integer::parseInt));        
        modifiableSettings.get("goals").addObserver(this);
        modifiableSettings.get("goals").setValue(1);
    }
    
    @Override
    public void setMaze(Maze maze) {
        super.setMaze(maze);
        for (MazeEntity goal : goals) {
            maze.addEntity(goal);
            resetGoal(goal);
        }
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
    public boolean handleRunnerMove(Runner runner, Direction dir) {
            
        /* Calculate the position of the runner after moving to said
         * direction
         */
        int nx = runner.getPosition().x + dir.deltaX;
        int ny = runner.getPosition().y + dir.deltaY;

        // If the runner tries to move illegally, skip its turn.
        if (maze.get(nx, ny) == MazeBlock.WALL)
            return false;

        List<MazeEntity> entities = maze.getEntitiesAt(nx, ny);
        boolean collisionAllowedAll = entities.stream()
                .allMatch(e -> isCollisionAllowed(runner, e));

        /* If the runner tries to move illegally on top of some entity with
         * whom collision is not allowed, skip its turn.
         */
        if (!collisionAllowedAll)
            return false;

        for (MazeEntity ent : entities)
            handleCollision(runner, ent);

        runner.setPosition(nx, ny);    
        return true;
    }
    
    private void handleCollision(Runner runner, MazeEntity entity) {            
        if (!goals.contains(entity))
            return;
        
        setScore(runner, getScore(runner) + 1);
        resetGoal(entity);
    }

    @Override
    public List<MazeEntity> getRunnerGoals(Runner runner) {
        return new ArrayList<>(goals);
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

    @Override
    public Map<String, Setting> getModifiableSettings() {        
        return modifiableSettings;
    }

    @Override
    public void update(Observable o, Object arg) {
        int numGoals = (int) modifiableSettings.get("goals").getValue();
        
        while (goals.size() < numGoals)
            goals.add(new MazeEntity(0, 0));
        while (goals.size() > numGoals)
            goals.remove(goals.size() - 1);
    }

    @Override
    public Predicate<Position> getPositionPredicate(Runner runner) {
        return pos -> maze.get(pos.x, pos.y) == MazeBlock.FLOOR;
    }
    
}
