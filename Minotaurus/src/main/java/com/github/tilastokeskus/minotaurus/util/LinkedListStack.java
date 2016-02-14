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

public class LinkedListStack<T> {
    
    private static class Node<T> {
        private final Node<T> previous;
        private final T data;
        
        public Node(T data, Node<T> previous) {
            this.data = data;
            this.previous = previous;
        }
    }
    
    protected final static Node ROOT = new Node(null, null);
    
    protected Node<T> head;    
    protected int size;
    
    /**
     * Creates a new stack.
     */
    public LinkedListStack() {
        this.head = ROOT;
        this.size = 0;
    }
    
    /**
     * Returns and removes the element on top of the stack.
     * @return The element on top of the stack.
     */
    public T pop() {
        if (head == ROOT)
            return null;
        
        T elem = head.data;
        head = head.previous;
        size--;
        return elem;
    }
    
    /**
     * Returns the element on top of the stack.
     * @return The element on top of the stack.
     */
    public T peek() {
        return head.data;
    }
    
    /**
     * Returns the number of elements in this stack.
     * @return Number of elements.
     */
    public int size() {
        return size;
    }

    /**
     * Returns {@code true} if the number of elements in this stack is 0.
     * @return True if the stack holds no elements, false otherwise.
     */
    public boolean isEmpty() {
        return head == ROOT;
    }

    /**
     * Adds an element on top of the stack.
     * @param e Element to add.
     * @return  True if the addition was successful.
     */
    public boolean add(T e) {
        Node<T> n = new Node(e, head);
        head = n;
        size++;
        return true;
    }

    /**
     * Removes all elements from the stack.
     */
    public void clear() {
        head = ROOT;
        size = 0;
    }

}
