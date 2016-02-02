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

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;

public class PlainSeparator extends JComponent {

    private static final Color light = new Color(80, 80, 80);
    private static final Color dark = new Color(40, 40, 40);
    private static final Color lightInv = new Color(230, 230, 230);
    private static final Color darkInv = new Color(200, 200, 200);
    
    private final boolean inverseColors;
    private final double sizePercent;
    private final boolean vertical;
    
    /**
     * Creates a PlainSeparator with default colors and width.
     */
    public PlainSeparator() {
        this(false, 1.0, false);
    }
    
    /**
     * Creates a PlainSeparator with default or inverse colors.
     * 
     * @param inverseColors   True for inverse colors, false for default.
     * Inverse colors are meant for a light surface.
     */
    public PlainSeparator(boolean inverseColors) {
        this(inverseColors, 100, false);
    }
    
    /**
     * Creates a PlainSeparator with specified width.
     * 
     * @param widthPercent   double from 0.0 to 1.0
     * a width of 0.5 creates a PlainSeparator with
     * half the width of its full width.
     */
    public PlainSeparator(double widthPercent) {
        this(false, widthPercent, false);
    }
    
    /**
     * Creates a PlainSeparator with default or inverse colors and specified width.
     * 
     * @param widthPercent   double from 0.0 to 1.0.
     * A width of 0.5 creates a PlainSeparator with half the width of its full width.
     * 
     * @param inverseColors  True for inverse colors, false for default.
     * Inverse colors are meant for a light surface.
     */
    public PlainSeparator(boolean inverseColors, double widthPercent) {
        this(inverseColors, widthPercent, false);
    }
    
    public PlainSeparator(boolean inverseColors, double widthPercent, boolean vertical) {
        this.inverseColors = inverseColors;
        this.sizePercent = widthPercent;
        this.vertical = vertical;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (vertical) {
            int separatorHeight = (int) (getHeight() * sizePercent);
            int topOffset = (getHeight() - separatorHeight) / 2;

            g.setColor(inverseColors ? lightInv : light);
            g.drawLine(1, topOffset, 1, topOffset + separatorHeight);
            g.setColor(inverseColors ? darkInv : dark);
            g.drawLine(0, topOffset, 0, topOffset + separatorHeight);
        } else {
            int separatorWidth = (int) (getWidth() * sizePercent);
            int leftOffset = (getWidth() - separatorWidth) / 2;

            g.setColor(inverseColors ? lightInv : light);
            g.drawLine(leftOffset, 1, leftOffset + separatorWidth, 1);
            g.setColor(inverseColors ? darkInv : dark);
            g.drawLine(leftOffset, 0, leftOffset + separatorWidth, 0);
        }
    }
    
    @Override
    public int getWidth() {
        return this.vertical ? 2 : super.getWidth();
    }
    
    @Override
    public int getHeight() {
        return this.vertical ? super.getHeight() : 2;
    }
    
}