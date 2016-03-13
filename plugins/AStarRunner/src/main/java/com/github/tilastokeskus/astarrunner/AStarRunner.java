/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.tilastokeskus.astarrunner;

import com.github.tilastokeskus.minotaurus.maze.Maze;
import com.github.tilastokeskus.minotaurus.maze.MazeEntity;
import com.github.tilastokeskus.minotaurus.runner.Runner;
import com.github.tilastokeskus.minotaurus.util.Direction;
import com.github.tilastokeskus.minotaurus.util.HashSet;
import com.github.tilastokeskus.minotaurus.util.Position;
import com.github.tilastokeskus.minotaurus.util.PriorityQueue;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

public class AStarRunner extends Runner {
    
    public static void main(String[] args) {
        Runner.testRunner(AStarRunner.class, 20, 20, 50);
    }
    
    private static final Direction dirs[] = new Direction[] {
        Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT
    };

    @Override
    public Direction getNextMove(Maze maze, Collection<MazeEntity> goals, Predicate<Position> positionPredicate) {
        
        // A queue of nodes we have yet to visit.
        PriorityQueue<Node> open = new PriorityQueue<>((Node o1, Node o2) -> Integer.compare(o1.score, o2.score));
        
        // A set of nodes we know we have the best path to.
        HashSet<Node> closed = new HashSet<>();
        
        // Number of moves it takes to reach a position from the start.
        int[][] g = new int[maze.getHeight()][maze.getWidth()];
        
        for (int i = 0; i < g.length; i++)
            Arrays.fill(g[i], Integer.MAX_VALUE);        
        
        g[getPosition().y][getPosition().x] = 0;
        
        Position closestGoal = getClosestGoalPosition(getPosition(), goals);
        Node n = new Node(getPosition(), null);
        n.score = distance(getPosition(), closestGoal);
        open.add(n);
        
        while (!open.isEmpty()) {
            
            // Get the node with the smallest score.
            n = open.extractMin();
            closestGoal = getClosestGoalPosition(n.pos, goals);
            if (distance(n.pos, closestGoal) == 0) {
                
                /* If we arrived at a goal, get the second position in the
                 * move history (first position being the starting position).
                 */
                while (n.prev.prev != null)
                    n = n.prev;                
                Position nextPos = n.pos;
                
                // Return the direction we went from the starting position.
                Direction dir = dirFromPositions(this.getPosition(), nextPos);
                return dir;
            }
            
            // Add the current node to the closed set.
            closed.add(n);
            
            for (Direction dir : dirs) {
                Position pos = n.pos;
                Position newPos = new Position(pos.x + dir.deltaX, pos.y + dir.deltaY);
                
                // Skip direction if it would lead to an illegal position.
                if (!positionPredicate.test(newPos))
                    continue;
                
                Node newNode = new Node(newPos, n);
                
                if (closed.contains(newNode))
                    continue;
                
                if (open.contains(newNode)) {
                    
                    // If we have a better path, update the info.
                    if (g[pos.y][pos.x] + 1 < g[newPos.y][newPos.x])
                        open.remove(newNode);
                    else
                        continue;
                }
                
                newNode.prev = n;
                newNode.score = g[pos.y][pos.x] + 1 + distance(newPos, closestGoal);
                open.add(newNode);
                g[newPos.y][newPos.x] = g[pos.y][pos.x] + 1;
            }
        }
        
        return Direction.NONE;
    }
    
    private Direction dirFromPositions(Position p1, Position p2) {
        if (p1.x < p2.x) return Direction.RIGHT;
        if (p1.x > p2.x) return Direction.LEFT;
        if (p1.y < p2.y) return Direction.DOWN;
        if (p1.y > p2.y) return Direction.UP;
        return Direction.NONE;
    }
    
    /**
     * Returns the position of the goal whose Manhattan distance from some
     * position is the lowest.
     * 
     * @param p Position to calculate distance from.
     * @param goals Collection of goals from which the closest one is chosen.
     * @return The position of the closest goal.
     */
    private Position getClosestGoalPosition(Position p, Collection<MazeEntity> goals) {
        int minDist = Integer.MAX_VALUE;
        Position bestPos = p;
        for (MazeEntity e : goals) {
            Position p2 = e.getPosition();
            int dist = distance(p, p2);
            if (dist < minDist) {
                minDist = dist;
                bestPos = p2;
            }
        }
        
        return bestPos;
    }
    
    /**
     * Manhattan distance between two positions.
     * 
     * @param p1 Position 1.
     * @param p2 Position 2.
     * @return Manhattan distance between the positions.
     */
    private int distance(Position p1, Position p2) {
        int x1 = p1.x;
        int y1 = p1.y;
        int x2 = p2.x;
        int y2 = p2.y;
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
    
    private static class Node {
        int score;
        Position pos;
        Node prev;
        
        public Node(Position pos, Node prev) {
            this.pos = pos;
            this.prev = prev;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
                
            if (obj == null || getClass() != obj.getClass())
                return false;
                
            final Node other = (Node) obj;
            return this.pos.equals(other.pos);
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 37 * hash + Objects.hashCode(this.pos);
            return hash;
        }
    }

}
