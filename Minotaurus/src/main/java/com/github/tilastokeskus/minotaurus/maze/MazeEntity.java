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

import com.github.tilastokeskus.minotaurus.ui.Drawable;
import java.awt.Point;
import java.util.Observable;

public abstract class MazeEntity extends Observable implements Drawable {
    private Point position;
    
    /**
     * Creates a new MazeEntity, initializing its position to (0, 0).
     */
    public MazeEntity() {
        this(0, 0);
    }
    
    /**
     * Creates a new MazeEntity, initializing its position to (x, y).
     * 
     * @param x X position.
     * @param y Y position.
     */
    public MazeEntity(int x, int y) {
        this.position = new Point(x, y);
    }
    
    /**
     * Returns this MazeEntity's position.
     * 
     * @return A Point object.
     */
    public Point getPosition() {
        return this.position;
    }
    
    /**
     * Sets this MazeEntity's position, and notifies its observers, passing the
     * previous position to them.
     * 
     * @param x New X position.
     * @param y New Y position.
     */
    public void setPosition(int x, int y) {
        Point oldPos = new Point(position);
        position = new Point(x, y);
        
        setChanged();
        notifyObservers(oldPos);
    }
}
