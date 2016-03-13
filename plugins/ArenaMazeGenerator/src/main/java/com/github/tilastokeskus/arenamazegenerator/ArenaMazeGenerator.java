/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.tilastokeskus.arenamazegenerator;

import com.github.tilastokeskus.minotaurus.maze.AbstractMazeGenerator;
import com.github.tilastokeskus.minotaurus.maze.Maze;
import com.github.tilastokeskus.minotaurus.maze.MazeBlock;

/**
 * Generates an arena with everything being floor except for the arena's
 * boundaries, which are wall.
 */
public class ArenaMazeGenerator extends AbstractMazeGenerator {

    @Override
    public Maze generateMaze(int width, int height) {
        MazeBlock[][] layout = new MazeBlock[height][width];
        
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                layout[i][j] = isBoundary(j, i, width, height) ?
                        MazeBlock.WALL :
                        MazeBlock.FLOOR;
        
        return new Maze(layout);
    }
    
    private boolean isBoundary(int x, int y, int w, int h) {
        return x == 0 || x == w - 1 || y == 0 || y == h - 1;
    }

}
