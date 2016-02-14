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

package com.github.tilastokeskus.minotaurus.runner;

import com.github.tilastokeskus.minotaurus.maze.MazeEntity;
import com.github.tilastokeskus.minotaurus.util.ColorFactory;
import java.awt.Color;

/**
 * A skeletal implementation of a Runner to help creating runners as easily as
 * possible.
 */
public abstract class AbstractRunner implements Runner {
    
    private int id;    
    private int x;
    private int y;    
    private String title;
    
    private final Color color;
    private final float size;
    
    public AbstractRunner() {
        color = ColorFactory.getNextColor();
        size = 0.5f;
    }
    
    @Override
    public Color getColor() {
        return this.color;
    }
    
    @Override
    public float getSize() {
        return this.size;
    }
    
    @Override
    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public int getId() {
        return this.id;
    }
    
    @Override
    public void setX(int x) {
        this.x = x;
    }
    
    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractRunner other = (AbstractRunner) obj;
        return this.id == other.getId();
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
