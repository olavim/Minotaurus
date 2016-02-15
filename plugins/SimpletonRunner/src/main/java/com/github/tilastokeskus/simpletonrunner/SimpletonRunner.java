/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.tilastokeskus.simpletonrunner;

import com.github.tilastokeskus.minotaurus.maze.Maze;
import com.github.tilastokeskus.minotaurus.maze.MazeBlock;
import com.github.tilastokeskus.minotaurus.maze.MazeEntity;
import com.github.tilastokeskus.minotaurus.util.Direction;
import java.util.Collection;
import com.github.tilastokeskus.minotaurus.runner.Runner;
import java.awt.Point;

/**
 * Decides a direction by checking which direction has the lowest Manhattan
 * distance. Will get stuck in every trap.
 */
public class SimpletonRunner extends Runner {
    
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
            int nx = getPosition().x + dir.deltaX;
            int ny = getPosition().y + dir.deltaY;
            
            if (maze.get(nx, ny) == MazeBlock.WALL)
                continue;
            
            int dist = getManhattanDistance(new Point(nx, ny), goal.getPosition());
            
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
        return getManhattanDistance(this.getPosition(), ent.getPosition());
    }
    
    private int getManhattanDistance(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }

}
