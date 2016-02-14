/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.tilastokeskus.simpletonrunner;

import com.github.tilastokeskus.minotaurus.maze.Maze;
import com.github.tilastokeskus.minotaurus.maze.MazeEntity;
import com.github.tilastokeskus.minotaurus.runner.AbstractRunner;
import com.github.tilastokeskus.minotaurus.runner.Runner;
import com.github.tilastokeskus.minotaurus.util.Direction;
import java.util.Collection;

/**
 * Decides a direction by checking which direction has the lowest Manhattan
 * distance. Will get stuck in every trap.
 */
public class SimpletonRunner extends AbstractRunner {
    
    public static void main(String[] args) {
        Runner.testGenerator(SimpletonRunner.class, 20, 20);
    }
    
    private static final Direction dirs[] = new Direction[] {
        Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT
    };

    @Override
    public Direction getNextMove(Maze maze, Collection<MazeEntity> goals) {
        MazeEntity goal = getClosestGoal(goals);
        
        int bestDist = getDistanceTo(goal);
        Direction bestDir = Direction.NONE;
        for (Direction dir : dirs) {
            int nx = getX() + dir.deltaX;
            int ny = getY() + dir.deltaY;
            
            if (maze.isOccupied(nx, ny))
                continue;
            
            int dist = getDistance(nx, goal.getX(), ny, goal.getY());
            
            if (dist < bestDist) {
                bestDist = dist;
                bestDir = dir;
            }
        }
        
        return bestDir;
    }
    
    private MazeEntity getClosestGoal(Collection<MazeEntity> goals) {
        int bestDist = Integer.MAX_VALUE;
        MazeEntity bestGoal = null;
        
        for (MazeEntity goal : goals) {
            int dist = getDistanceTo(goal);      
            
            if (dist < bestDist) {
                bestDist = dist;
                bestGoal = goal;
            }
        }
        
        return bestGoal;
    }
    
    private int getDistanceTo(MazeEntity ent) {
        return getDistance(this.getX(), ent.getX(), this.getY(), ent.getY());
    }
    
    private int getDistance(int x, int x2, int y, int y2) {
        return Math.abs(x - x2) + Math.abs(y - y2);
    }

}
