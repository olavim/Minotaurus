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

package com.github.tilastokeskus.minotaurus.ui;

import com.github.tilastokeskus.minotaurus.maze.Maze;
import com.github.tilastokeskus.minotaurus.maze.MazeBlock;
import com.github.tilastokeskus.minotaurus.maze.MazeEntity;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class MazePanel extends JPanel {

    private static final int BLOCK_WIDTH = 10;
    private static final int BLOCK_HEIGHT = 10;
    
    private Maze maze;
    
    public MazePanel(Maze maze) {
        this.maze = maze;
        this.setBackground(MazeBlock.FLOOR.drawColor);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);        
        Graphics2D g2 = (Graphics2D) g;
        
        int mw = maze.getWidth();
        int mh = maze.getHeight();
        int pw = this.getWidth();
        int ph = this.getHeight();
        int blockW = pw / mw;
        int blockH = ph / mh;
        
        for (int x = 0; x < mw; x++) {
            for (int y = 0; y < mh; y++) {
                MazeBlock block = maze.get(x, y);
                if (!block.draw)
                    continue;
                
                Color drawColor = block.drawColor;
                int offsetX = 0;
                int offsetY = 0;
                int w = blockW;
                int h = blockH;
                
                if (block == MazeBlock.WALL) {
                    g2.setColor(Color.BLACK);
                    g2.fillRect(x * blockW, y * blockH, blockW + 3, blockH + 3);
                } else if (block == MazeBlock.ENTITY) {
                    System.out.println("1:" + x + ":" + y);
                    MazeEntity ent = maze.getEntity(x, y);
                    if (ent != null) {
                        drawColor = ent.getColor();
                        w *= ent.getSize();
                        h *= ent.getSize();
                        offsetX = (blockW - w) / 2;
                        offsetY = (blockH - h) / 2;
                    }
                }
                
                g2.setColor(drawColor);
                g2.fillRect(x*blockW + offsetX, y*blockH + offsetY, blockW, blockH);
            }
        }
        
        this.revalidate();
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(maze.getWidth() * BLOCK_WIDTH, maze.getHeight() * BLOCK_HEIGHT);
    }
    
}
