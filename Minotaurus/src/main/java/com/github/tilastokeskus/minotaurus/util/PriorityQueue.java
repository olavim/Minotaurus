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
 * An implementation of PriorityQueue using binary min-heap.
 * 
 * @param <T> Type of elements to store.
 */
public final class PriorityQueue<T> {
    
    static final int INITIAL_CAPACITY = 16;
    static final int MAX_CAPACITY = Integer.MAX_VALUE - 8;
    
    int numElements;
    Comparator<T> comparator;
    Object[] data;
    
    public PriorityQueue() {
        this.comparator = null;
        this.numElements = 0;
        this.data = new Object[INITIAL_CAPACITY];
    }
    
    public PriorityQueue(Comparator<T> comparator) {
        this.comparator = comparator;
        this.numElements = 0;
        this.data = new Object[INITIAL_CAPACITY];
    }
    
    public PriorityQueue(PriorityQueue<T> q) {
        this.comparator = q.comparator;
        this.numElements = q.numElements;
        this.data = Arrays.copyOf(q.data, q.data.length);
    }
    
    public int size() {
        return numElements;
    }
    
    public boolean isEmpty() {
        return numElements == 0;
    }
    
    public void clear() {
        data = new Object[INITIAL_CAPACITY];
        numElements = 0;
    }
    
    public void add(T elem) {
        if (shouldResize(numElements + 1))
            resize(data.length * 2);
        
        data[numElements] = elem;
        shiftUp(numElements++);
    }
    
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }
    
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
    
    public T extractMin() {
        T elem = (T) data[0];
        data[0] = data[numElements - 1];
        data[numElements - 1] = null;
        numElements--;
        shiftDown(0);
        return elem;
    }
    
    public T min() {
        return (T) data[0];
    }
    
    int indexOf(Object o) {
        for (int i = 0; i < numElements; i++)
            if (data[i].equals(o))
                return i;
        
        return -1;
    }
    
    void shiftDown(int index) {
        if (index >= numElements)
            return;
        
        T left = (T) data[left(index)];        
        if (left == null)
            return;
        
        T curr = (T) data[index];
        T right = (T) data[right(index)];
        
        if (compareElems(left, curr) < 0) {
            if (right != null && compareElems(right, left) < 0) {
                swap(index, right(index));
                shiftDown(right(index));
            } else {
                swap(index, left(index));
                shiftDown(left(index));
            }
        } else if (right != null && compareElems(right, curr) < 0) {
            swap(index, right(index));
            shiftDown(right(index));
        }
    }
    
    void shiftUp(int index) {
        if (index == 0)
            return;
        
        T child = (T) data[index];
        T parent = (T) data[parent(index)];

        if (compareElems(child, parent) >= 0)
            return;

        swap(index, parent(index));
        shiftUp(parent(index));
    }
    
    int compareElems(T e1, T e2) {
        if (comparator != null)
            return comparator.compare(e1, e2);
        return ((Comparable<T>) e1).compareTo(e2);
    }
    
    void swap(int index1, int index2) {
        Object temp = data[index1];
        data[index1] = data[index2];
        data[index2] = temp;
    }
    
    int parent(int index) {
        return (index - 1) / 2;
    }
    
    int left(int index) {
        return 2*index + 1;
    }
    
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
