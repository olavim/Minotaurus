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

import java.util.Arrays;

public class Stack<T> {
    
    /**
     * The maximum size of array to allocate.
     */
    protected static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    
    protected static final int INITIAL_SIZE = 10;
    
    protected Object[] data;
    protected int head;
    
    /**
     * Creates a new stack with a default initial capacity. The stack will
     * automatically grow the capacity when needed.
     */
    public Stack() {
        this(INITIAL_SIZE);
    }
    
    /**
     * Creates a new stack with the given initial capacity. The stack will
     * automatically grow the capacity when needed.
     * @param size Initial capacity.
     */
    public Stack(int size) {
        data = new Object[size];
        head = 0;
    }
    
    /**
     * Returns and removes the element on top of the stack.
     * @return The element on top of the stack.
     */
    public T pop() {
        T e = (T) data[head - 1];
        data[head - 1] = null;
        head--;
        return e;
    }
    
    /**
     * Returns the element on top of the stack.
     * @return The element on top of the stack.
     */
    public T peek() {
        return (T) data[head - 1];
    }
    
    /**
     * Returns the number of elements in this stack.
     * @return Number of elements.
     */
    public int size() {
        return head;
    }

    /**
     * Returns {@code true} if the number of elements in this stack is 0.
     * @return True if the stack holds no elements, false otherwise.
     */
    public boolean isEmpty() {
        return head == 0;
    }

    /**
     * Returns whether or not the specified object is contained in the stack.
     * @param o Object to search.
     * @return  True if the object exists, false otherwise.
     */
    public boolean contains(Object o) {
        for (int i = 0; i < head; i++)
            if (data[i].equals(o))
                return true;
        
        return false;
    }

    /**
     * Adds an element on top of the stack.
     * @param e Element to add.
     * @return  True if the addition was successful.
     */
    public boolean add(T e) {
        if (!ensureCapacity(head + 1))
            return false;
        data[head++] = e;
        return true;
    }

    /**
     * Removes all elements from the stack.
     */
    public void clear() {
        data = new Object[INITIAL_SIZE];
        head = 0;
    }
    
    /**
     * If the stack's inner data array's capacity is less than 
     * {@code size}, grow the capacity to at least {@code size}.
     * 
     * @param size Amount of elements the stack should be able to hold.
     * @return True if the capacity grew, false otherwise.
     */
    protected boolean ensureCapacity(int size) {
        if (size > data.length) {
            int newSize = size;
            System.out.println("1: " + newSize);
            
            if (data.length == MAX_ARRAY_SIZE) {
                return false;
            } else if (size <= MAX_ARRAY_SIZE/2 - 1) {
                newSize *= 2;
            } else if (size > MAX_ARRAY_SIZE/2) {
                newSize = MAX_ARRAY_SIZE;
            }
            
            System.out.println("2: " + newSize);
            data = Arrays.copyOf(data, newSize);
        } else if (size < 0) { // overflow
            return false;
        }
        
        return true;
    }

}
