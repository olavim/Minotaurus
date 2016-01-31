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

package com.github.tilastokeskus.minotaurus.maze;

import java.awt.Color;

public enum MazeEntity {
    WALL(true, Color.LIGHT_GRAY, 0.2),
    FLOOR,
    GOAL(true, Color.RED, 0.2),
    RUNNER(true, Color.MAGENTA, 0.6);
    
    /**
     * Should the entity be drawn.
     */
    public final boolean draw;
    
    /**
     * Color the entity should be drawn with.
     */
    public final Color drawColor;
    
    /**
     * Size proportional to full size. 1 is full size, 0 practically means
     * "do not draw"
     */
    public final double drawSize;
    
    private MazeEntity() {
        this.draw = false;
        this.drawColor = null;
        this.drawSize = 0;
    }
    
    private MazeEntity(boolean draw, Color color, double drawSize) {
        this.draw = draw;
        this.drawColor = color;
        this.drawSize = drawSize;
    }
    
}
