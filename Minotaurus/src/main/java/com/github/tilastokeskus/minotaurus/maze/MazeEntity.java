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
import com.github.tilastokeskus.minotaurus.ui.component.Rotatable;
import com.github.tilastokeskus.minotaurus.util.ColorFactory;
import com.github.tilastokeskus.minotaurus.util.Position;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Objects;
import java.util.Observable;

public class MazeEntity extends Observable implements Drawable, Rotatable, Cloneable {
    
    protected Position position;
    protected Shape initialShape;
    protected Shape shape;
    protected Color shapeColor;
    
    /**
     * Creates a new MazeEntity with a position at (0, 0).
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
        this(x, y, new Rectangle2D.Double(0, 0, 10, 10), ColorFactory.nextColor());
    }
    
    /**
     * Creates a new MazeEntity, initializing its position to (x, y) and setting
     * its shape to a rectangle with the specified color.
     * 
     * @param x X position.
     * @param y Y position.
     * @param shapeColor Foreground color of the shape.
     */
    public MazeEntity(int x, int y, Color shapeColor) {
        this(x, y, new Rectangle2D.Double(0, 0, 10, 10), shapeColor);
    }
    
    /**
     * Creates a new MazeEntity, initializing its position to (x, y) and setting
     * its shape to the specified shape.
     * 
     * @param x X position.
     * @param y Y position.
     * @param shape A shape for a visual representation.
     * @param shapeColor Foreground color of the shape.
     */
    public MazeEntity(int x, int y, Shape shape, Color shapeColor) {
        this.position = new Position(x, y);
        this.initialShape = shape;
        this.shape = shape;
        this.shapeColor = shapeColor;
    }

    @Override
    public Shape getShape() {
        return shape;
    }
    
    @Override
    public Color getShapeColor() {
        return shapeColor;
    }
    
    @Override
    public void setShapeColor(Color color) {
        shapeColor = color;
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
        return Objects.equals(this.position, other.position) 
                && Objects.equals(this.initialShape, other.initialShape) 
                && Objects.equals(this.shapeColor, other.shapeColor);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.position);
        hash = 73 * hash + Objects.hashCode(this.initialShape);
        hash = 73 * hash + Objects.hashCode(this.shapeColor);
        return hash;
    }

    @Override
    public void setRotation(double angle) {
        int x = shape.getBounds().x;
        int y = shape.getBounds().y;
        int w = shape.getBounds().width;
        int h = shape.getBounds().height;
        
        AffineTransform af = new AffineTransform();
        double offsetX = x + (w / 2.0);
        double offsetY = y + (h / 2.0);
        af.translate(offsetX, offsetY);
        
        double radians = angle * (Math.PI / 180.0);
        af.rotate(radians);
        af.translate(-offsetX, -offsetY);
        
        shape = af.createTransformedShape(initialShape);
    }
    
    @Override
    public MazeEntity clone() {
        try {
            MazeEntity ent = (MazeEntity) super.clone();
            AffineTransform af = new AffineTransform();
            ent.initialShape = initialShape;
            ent.shape = af.createTransformedShape(shape);
            ent.shapeColor = shapeColor;
            ent.position = position;
            return ent;
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }
}
