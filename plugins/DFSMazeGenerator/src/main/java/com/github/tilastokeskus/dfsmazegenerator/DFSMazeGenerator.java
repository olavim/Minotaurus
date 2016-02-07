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

package com.github.tilastokeskus.dfsmazegenerator;

import com.github.tilastokeskus.minotaurus.maze.Maze;
import com.github.tilastokeskus.minotaurus.maze.MazeBlock;
import com.github.tilastokeskus.minotaurus.maze.MazeGenerator;
import java.awt.Point;
import java.util.Collections;
import java.util.Stack;

public class DFSMazeGenerator implements MazeGenerator {
    
    public static void main(String[] args) {
        MazeGenerator.testGenerator(DFSMazeGenerator.class, 20, 20);
    }
    
    private String title;

    @Override
    public Maze generateMaze(int width, int height) {
        MazeBlock[][] layout = new MazeBlock[height][width];
        dfs(layout, 0, 0, 0, 0);
        
        return new Maze(layout);
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
    private void dfs(MazeBlock[][] layout, int x, int y, int lx, int ly) {
        if (!isInBounds(layout, x, y) 
                || hasAdjacentVisited(layout, x, y, lx, ly))
            return;
        
        layout[y][x] = MazeBlock.FLOOR;
        
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
    
    /**
     * Is the current position adjacent to a previously visited location, not
     * including the previous location?
     * 
     * @param layout    Current layout.
     * @param x         Current x pos.
     * @param y         Current y pos.
     * @param lx        Previous x pos.
     * @param ly        Previous x pos.
     * @return          True if current position is adjacent to a previously
     *                  visited location, false otherwise.
     */
    private boolean hasAdjacentVisited(MazeBlock[][] layout, int x, int y, int lx, int ly) {
        return (x > 0 && layout[y][x-1] != null && x-1 != lx)
                || (y > 0 && layout[y-1][x] != null && y-1 != ly)
                || (x < layout[0].length - 1 && layout[y][x+1] != null && x+1 != lx)
                || (y < layout.length - 1 && layout[y+1][x] != null && y+1 != ly);
    }
    
    private boolean isInBounds(MazeBlock layout[][], int x, int y) {
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
