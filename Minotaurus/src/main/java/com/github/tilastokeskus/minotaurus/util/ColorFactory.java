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

package com.github.tilastokeskus.minotaurus.util;

import java.awt.Color;

public class ColorFactory {
    
    private final static float GOLDEN_ANGLE = 137.5f;
    private final static int PALETTE_SIZE = 360;
    private final static Color[] COLORWHEEL;
    private final static ColorFactory STATIC_FACTORY = new ColorFactory();
    
    static {
        
        // Create an array of 360 colors that is treated like a color wheel.
        COLORWHEEL = new Color[PALETTE_SIZE];
        for (int i = 0; i < PALETTE_SIZE; i++) {
            Color c = Color.getHSBColor(i / (float) PALETTE_SIZE, 0.3f, 0.85f );
            COLORWHEEL[i] = c;
        }
    }
    
    /**
     * Returns a new color from a preset collection of pastel colors, that is
     * distinct from the previously returned colors.
     * 
     * @return A color.
     */
    public static Color nextColor() {
        return STATIC_FACTORY.getNextColor();
    }
    
    private double index = 0;
    
    /**
     * Returns a new color from a preset collection of pastel colors, that is
     * distinct from the previously returned colors.
     * 
     * @return A color.
     */
    public Color getNextColor() {
        
        /* By moving the index by the golden angle, we get an even distribution
         * among different colors.
         */
        index = (index + GOLDEN_ANGLE) % PALETTE_SIZE;
        Color color = COLORWHEEL[(int) index];
        
        return color;
    }
    
    /**
     * Sets the index according to seed. Any value is valid. Calling
     * {@link getNextColor()} after this function always returns the same color
     * when the same seed is used.
     */
    public void reset() {
        index = 0;
    }
    
}
