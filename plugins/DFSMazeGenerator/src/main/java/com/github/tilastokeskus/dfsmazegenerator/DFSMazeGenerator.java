/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.tilastokeskus.dfsmazegenerator;

import com.github.tilastokeskus.minotaurus.maze.Maze;
import com.github.tilastokeskus.minotaurus.maze.MazeEntity;
import com.github.tilastokeskus.minotaurus.maze.MazeGenerator;

public class DFSMazeGenerator implements MazeGenerator {
    
    public static void main(String[] args) {
        
    }
    
    private String title;

    @Override
    public Maze generateMaze(int width, int height) {
        MazeEntity[][] layout = new MazeEntity[height][width];
        return new Maze(layout);
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
