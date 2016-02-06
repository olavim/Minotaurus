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

package com.github.tilastokeskus.dfsmazegeneratorpretty;

import com.github.tilastokeskus.minotaurus.maze.Maze;
import com.github.tilastokeskus.minotaurus.maze.MazeEntity;
import com.github.tilastokeskus.minotaurus.maze.MazeGenerator;
import java.awt.Point;
import java.util.Collections;
import java.util.Stack;

public class DFSMazeGeneratorPretty implements MazeGenerator {
    
    private static final int UP    = 1;
    private static final int DOWN  = 1<<1;
    private static final int LEFT  = 1<<2;
    private static final int RIGHT = 1<<3;
    
    private static final double ERASE_WALL = 0.05;
    
    public static void main(String[] args) {
        MazeGenerator.testGenerator(DFSMazeGeneratorPretty.class, 20, 20);
    }
    
    private String title;

    @Override
    public Maze generateMaze(int width, int height) {
        int[][] layout = new int[height/2][width/2];
        dfs(layout, 0, 0, 0, 0);
        
        int[] dir = {UP, RIGHT, DOWN, LEFT};
        int[] dirDelta = {-1, 0, 1, 0, -1};
        boolean[] dirOk = new boolean[4];
        
        MazeEntity[][] maze = new MazeEntity[height+1][width+1];
        for (int y = 0; y < height/2; y++) {
            for (int x = 0; x < width/2; x++) {
                maze[y*2+1][x*2+1] = MazeEntity.FLOOR;
                for (int i = 0; i < 4; i++)
                    dirOk[i] = (layout[y][x] & dir[i]) != 0;
                
                for (int i = 0; i < 4; i++) {
                    int dy = y*2 + dirDelta[i] + 1;
                    int dx = x*2 + dirDelta[i+1] + 1;
                    if (dy >= 0 && dy < height && dx >= 0 && dx < width)
                        maze[dy][dx] = dirOk[i] ? MazeEntity.FLOOR : MazeEntity.WALL;
                                
                }
            }
        }
        
        for (int y = 1; y < height; y++) {
            for (int x = 1; x < width; x++) {
                if (maze[y][x] == MazeEntity.WALL) {
                    double r = Math.random();
                    if (r <= ERASE_WALL)
                        maze[y][x] = MazeEntity.FLOOR;
                }
            }
        }
        
        return new Maze(maze);
    }
    
    /**
     * Depth-first search maze generation algorithm.
     * 
     * @param layout    Current layout.
     * @param x         Current x pos.
     * @param y         Current y pos.
     * @param lx        Previous x pos.
     * @param ly        Previous x pos.
     */
    private void dfs(int[][] layout, int x, int y, int lx, int ly) {
        if (!isInBounds(layout, x, y) || layout[y][x] != 0)
            return;
        
        if (x - 1 == lx) { // went right
            layout[y][x] |= LEFT;
            layout[ly][lx] |= RIGHT;
        } else if (x + 1 == lx) { // went left
            layout[y][x] |= RIGHT;
            layout[ly][lx] |= LEFT;
        } else if (y - 1 == ly) { // went down
            layout[y][x] |= UP;
            layout[ly][lx] |= DOWN;
        } else if (y + 1 == ly) { // went up
            layout[y][x] |= DOWN;
            layout[ly][lx] |= UP;
        }
        
        Stack<Point> points = new Stack<>();
        points.add(new Point(x - 1, y));
        points.add(new Point(x + 1, y));
        points.add(new Point(x, y - 1));
        points.add(new Point(x, y + 1));
        Collections.shuffle(points);
        
        while (!points.empty()) {
            Point p = points.pop();            
            dfs(layout, p.x, p.y, x, y);
        }
    }
    
    private boolean isInBounds(int layout[][], int x, int y) {
        return x >= 0 && x < layout[0].length && y >= 0 && y < layout.length;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return this.title;
    }

}
