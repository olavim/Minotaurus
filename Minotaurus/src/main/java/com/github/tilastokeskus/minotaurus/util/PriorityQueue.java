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
import java.util.Comparator;

/**
 * An implementation of a priority queue using binary min-heap.
 * 
 * @param <T> Type of elements to store.
 */
public final class PriorityQueue<T> {
    
    static final int INITIAL_CAPACITY = 16;
    static final int MAX_CAPACITY = Integer.MAX_VALUE - 8;
    
    int numElements;
    Comparator<T> comparator;
    Object[] data;
    
    /**
     * Creates a new PriorityQueue with no comparator. Added elements are
     * assumed to be {@link Comparable}.
     */
    public PriorityQueue() {
        this.comparator = null;
        this.numElements = 0;
        this.data = new Object[INITIAL_CAPACITY];
    }
    
    /**
     * Creates a new PriorityQueue with the specified comparator. Elements are
     * compared to each other using this this comparator.
     * 
     * @param comparator Comparator to be used when comparing elements to each
     *                   other.
     */
    public PriorityQueue(Comparator<T> comparator) {
        this.comparator = comparator;
        this.numElements = 0;
        this.data = new Object[INITIAL_CAPACITY];
    }
    
    /**
     * Creates a new PriorityQueue from the specified existing PriorityQueue.
     * 
     * @param q PriorityQueue to create a new one from.
     */
    public PriorityQueue(PriorityQueue<T> q) {
        this.comparator = q.comparator;
        this.numElements = q.numElements;
        this.data = Arrays.copyOf(q.data, q.data.length);
    }
    
    /**
     * Returns the number of elements in this queue.
     * 
     * @return An integer.
     */
    public int size() {
        return numElements;
    }
    
    /**
     * Returns whether or not this queue is empty.
     * 
     * @return True if queue is empty, otherwise false.
     */
    public boolean isEmpty() {
        return numElements == 0;
    }
    
    /**
     * Removes all elements from this queue.
     */
    public void clear() {
        data = new Object[INITIAL_CAPACITY];
        numElements = 0;
    }
    
    /**
     * Adds the specified element to this queue.
     * 
     * @param elem Element to add.
     */
    public void add(T elem) {
        if (shouldResize(numElements + 1))
            resize(data.length * 2);
        
        data[numElements] = elem;
        shiftUp(numElements++);
    }
    
    /**
     * Returns whether or not the specified object is contained in this queue.
     * 
     * @param o Object to search for.
     * @return True if object is contained, otherwise false.
     */
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }
    
    /**
     * Removes the specified object from this queue if it exists.
     * 
     * @param o Object to remove.
     * @return True if the object existed, otherwise false.
     */
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index == -1)
            return false;
        data[index] = data[numElements - 1];
        data[numElements - 1] = null;
        numElements--;
        shiftDown(index);
        return true;
    }
    
    /**
     * Returns and removes the minimum element in this queue.
     * 
     * @return The minimum element.
     */
    public T extractMin() {
        T elem = (T) data[0];
        data[0] = data[numElements - 1];
        data[numElements - 1] = null;
        numElements--;
        shiftDown(0);
        return elem;
    }
    
    /**
     * Returns the minimum element in this queue.
     * 
     * @return The minimum element.
     */
    public T min() {
        return (T) data[0];
    }
    
    /**
     * Returns the index of an object in the queue's backing data array.
     * 
     * @param o Object to search.
     * @return Index of the object, or -1 if it wasn't found.
     */
    int indexOf(Object o) {
        for (int i = 0; i < numElements; i++)
            if (data[i].equals(o))
                return i;
        
        return -1;
    }
    
    /**
     * Moves the element at the specified index down in the heap until it is
     * smaller than both of its children.
     * 
     * @param index Index of the object to shift.
     */
    void shiftDown(int index) {
        if (index >= numElements)
            return;
        
        T left = (T) data[left(index)];  
        
        // If current node has no left child, return.
        if (left == null)
            return;
        
        T curr = (T) data[index];
        T right = (T) data[right(index)];
        
        // Left child is smaller than element to move.
        if (compareElems(left, curr) < 0) {
            
            // Element has a right child and it is smaller than the left child.
            if (right != null && compareElems(right, left) < 0) {
                swap(index, right(index));
                shiftDown(right(index));
            }
            // There is no right child or it isn't smaller than the left one.
            else {
                swap(index, left(index));
                shiftDown(left(index));
            }
        }
        // Left child is bigger than current node, but the right one is smaller.
        else if (right != null && compareElems(right, curr) < 0) {
            swap(index, right(index));
            shiftDown(right(index));
        }
    }
    
    /**
     * Moves the element at the specified index up in the heap until it is
     * smaller than both of its children.
     * 
     * @param index Index of the object to shift.
     */
    void shiftUp(int index) {
        if (index == 0)
            return;
        
        T child = (T) data[index];
        T parent = (T) data[parent(index)];

        // If element to move is equal to or bigger than its parent, return.
        if (compareElems(child, parent) >= 0)
            return;

        swap(index, parent(index));
        shiftUp(parent(index));
    }
    
    /**
     * Compare two elements to each other. Returns a negative integer, zero or
     * a positive integer if {@code e1} is smaller than, equal to or bigger than
     * {@code e2}.
     * 
     * @param e1
     * @param e2
     * @return An integer.
     */
    int compareElems(T e1, T e2) {
        if (comparator != null)
            return comparator.compare(e1, e2);
        return ((Comparable<T>) e1).compareTo(e2);
    }
    
    /**
     * Swaps the places of the elements in the specified indices.
     * @param index1 Index of object 1 to swap.
     * @param index2 Index of object 2 to swap.
     */
    void swap(int index1, int index2) {
        Object temp = data[index1];
        data[index1] = data[index2];
        data[index2] = temp;
    }
    
    /**
     * Returns the index of the object that is the parent of the object at the
     * specified index.
     * 
     * @param index Index of the object whose parent should be returned.
     * @return An integer.
     */
    int parent(int index) {
        return (index - 1) / 2;
    }
    
    /**
     * Returns the index of the object that is the left child of the object at
     * the specified index.
     * 
     * @param index Index of the object whose left child should be returned.
     * @return An integer.
     */
    int left(int index) {
        return 2*index + 1;
    }
    
    /**
     * Returns the index of the object that is the right child of the object at
     * the specified index.
     * 
     * @param index Index of the object whose right child should be returned.
     * @return An integer.
     */
    int right(int index) {
        return 2*index + 2;
    }
    
    boolean shouldResize(int newNumElements) {
        return right(newNumElements - 1) >= data.length;
    }
    
    void resize(int newCapacity) {
        if (numElements == MAX_CAPACITY)
            return;
        
        if (newCapacity > MAX_CAPACITY)
            newCapacity = MAX_CAPACITY;
        
        Object[] newData = Arrays.copyOf(data, newCapacity);
        data = newData;
    }
    
}
