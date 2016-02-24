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
import com.github.tilastokeskus.minotaurus.ui.component.RectangleComponent;
import com.github.tilastokeskus.minotaurus.ui.component.Rotatable;
import com.github.tilastokeskus.minotaurus.ui.component.RotatableComponent;
import com.github.tilastokeskus.minotaurus.util.ColorFactory;
import com.github.tilastokeskus.minotaurus.util.Position;
import java.awt.Color;
import java.util.Objects;
import java.util.Observable;

public class MazeEntity extends Observable implements Drawable, Rotatable, Cloneable {
    
    private Position position;
    private RotatableComponent component;
    
    /**
     * Creates a new MazeEntity.
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
        this(x, y, new RectangleComponent(10, ColorFactory.nextColor(), 0, Color.BLACK));
    }
    
    /**
     * Creates a new MazeEntity, initializing its position to (x, y) and setting
     * its component to the specified RotatableComponent.
     * 
     * @param x X position.
     * @param y Y position.
     * @param component A RotatableComponent for a visual representation.
     */
    public MazeEntity(int x, int y, RotatableComponent component) {
        this.position = new Position(x, y);
        this.component = component;
    }

    @Override
    public RotatableComponent getComponent() {
        return component;
    }
    
    @Override
    public Color getComponentColor() {
        return Color.black;//component.getForeground();
    }
    
    @Override
    public void setComponentColor(Color color) {
        component.setForeground(color);
    }
    
    /**
     * Returns this MazeEntity's position.
     * 
     * @return A Point object.
     */
    public Position getPosition() {
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
        Position oldPos = new Position(position);
        position = new Position(x, y);
        
        setChanged();
        notifyObservers(oldPos);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
            
        if (obj == null)
            return false;
            
        if (getClass() != obj.getClass())
            return false;
            
        final MazeEntity other = (MazeEntity) obj;
        return Objects.equals(this.component, other.component);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.position);
        hash = 73 * hash + Objects.hashCode(this.component);
        return hash;
    }

    @Override
    public void setRotation(double angle) {
        this.component.setRotation(angle);
    }
    
    @Override
    public MazeEntity clone() {
        try {
            MazeEntity ent = (MazeEntity) super.clone();
            ent.component = (RotatableComponent) this.component.clone();
            return ent;
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }
}
