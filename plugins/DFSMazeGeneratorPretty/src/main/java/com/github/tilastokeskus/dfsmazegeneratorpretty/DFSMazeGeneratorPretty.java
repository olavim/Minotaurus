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

import com.github.tilastokeskus.minotaurus.maze.AbstractMazeGenerator;
import com.github.tilastokeskus.minotaurus.maze.Maze;
import com.github.tilastokeskus.minotaurus.maze.MazeBlock;
import com.github.tilastokeskus.minotaurus.maze.MazeGenerator;
import com.github.tilastokeskus.minotaurus.util.Direction;
import java.util.Random;

public class DFSMazeGeneratorPretty extends AbstractMazeGenerator {
    
    private static final double ERASE_WALL = 0.05;
    
    public static void main(String[] args) {
        MazeGenerator.testGenerator(DFSMazeGeneratorPretty.class, 20, 20);
    }
    
    private final Direction[] dirs = new Direction[] {
        Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT
    };

    @Override
    public Maze generateMaze(int width, int height) {
        int[][] layout = new int[height/2][width/2];
        dfs(layout, 0, 0, 0, 0);
        
        boolean[] dirOk = new boolean[4];
        
        MazeBlock[][] maze = new MazeBlock[height+1][width+1];
        for (int y = 0; y < height/2; y++) {
            for (int x = 0; x < width/2; x++) {
                maze[y*2+1][x*2+1] = MazeBlock.FLOOR;
                for (int i = 0; i < 4; i++)
                    dirOk[i] = (layout[y][x] & bitOffset(dirs[i])) != 0;
                
                for (int i = 0; i < 4; i++) {
                    int dy = y*2 + dirs[i].deltaY + 1;
                    int dx = x*2 + dirs[i].deltaX + 1;
                    if (dy >= 0 && dy < height && dx >= 0 && dx < width)
                        maze[dy][dx] = dirOk[i] ? MazeBlock.FLOOR : MazeBlock.WALL;
                                
                }
            }
        }
        
        for (int y = 1; y < height; y++) {
            for (int x = 1; x < width; x++) {
                if (maze[y][x] == MazeBlock.WALL) {
                    double r = Math.random();
                    if (r <= ERASE_WALL)
                        maze[y][x] = MazeBlock.FLOOR;
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
        
        for (Direction dir : dirs) {
            if ((x + dir.deltaX == lx && dir.deltaX != 0) 
                    || (y + dir.deltaY == ly && dir.deltaY != 0)) {
                layout[y][x] |= bitOffset(dir);
                layout[ly][lx] |= bitOffset(dir.opposite());
            }
        }
        
        shuffleDirs();
        
        for (int i = 0; i < 4; i++) {
            dfs(layout, x + dirs[i].deltaX, y + dirs[i].deltaY, x, y);
        }
    }
    
    private void shuffleDirs() {        
        Random r = new Random();
        for (int i = 3; i >= 1; i--) {
            int j = r.nextInt(i);
            Direction temp = dirs[j];
            dirs[j] = dirs[i];
            dirs[i] = temp;
        }
    }
    
    private boolean isInBounds(int layout[][], int x, int y) {
        return x >= 0 && x < layout[0].length && y >= 0 && y < layout.length;
    }
    
    private int bitOffset(Direction dir) {
        switch (dir) {
            case UP: return 1<<1;
            case DOWN: return 1<<2;
            case LEFT: return 1<<3;
            default: return 1<<4;
        }
    }

}
