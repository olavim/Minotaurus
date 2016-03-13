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
import java.util.Collection;

public class ArrayList<E> extends AbstractList<E> {
    
    static final int INITIAL_CAPACITY = 16;
    static final int MAX_CAPACITY = Integer.MAX_VALUE - 8;
    
    Object[] data;
    int numElements;
    
    public ArrayList() {
        this(INITIAL_CAPACITY);
    }
    
    public ArrayList(int initialCapacity) {
        data = new Object[initialCapacity];
        numElements = 0;
    }
    
    public ArrayList(Collection<E> coll) {
        numElements = 0;
        data = new Object[coll.size() * 2];
        addAll(coll);
    }

    @Override
    public int size() {
        return numElements;
    }

    @Override
    public E get(int index) {
        checkBounds(index);
        return (E) data[index];
    }

    @Override
    public void clear() {
        mods++;        
        for (int i = 0; i < numElements; i++)
            data[i] = null;        
        numElements = 0;
    }

    @Override
    public void add(int index, E element) {
        checkBoundsAdd(index);
        if (shouldResize(numElements + 1))
            resize(data.length * 2);
        
        shiftElements(index);
        data[index] = element;
        numElements++;
        mods++;
    }

    @Override
    public E set(int index, E element) {
        checkBounds(index);
        E oldElem = get(index);
        data[index] = element;
        mods++;
        return oldElem;
    }

    @Override
    public E remove(int index) {
        checkBounds(index);
        E oldElem = get(index);
        int elementsToMove = numElements - index - 1;
        System.arraycopy(data, index + 1, data, index, elementsToMove);
        data[numElements - 1] = null;
        numElements--;
        mods++;
        return oldElem;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(data, numElements);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (numElements > a.length)
            return (T[]) Arrays.copyOf(data, numElements, a.getClass());
        
        System.arraycopy(data, 0, a, 0, numElements);
        return a;
    }
    
    void shiftElements(int startIndex) {
        int elementsToShift = numElements - startIndex;
        System.arraycopy(data, startIndex, data, startIndex + 1, elementsToShift);
    }
    
    boolean shouldResize(int newNumElements) {
        return newNumElements > data.length;
    }
    
    void resize(int newCapacity) {
        if (numElements == MAX_CAPACITY)
            return;
        
        if (newCapacity > MAX_CAPACITY)
            newCapacity = MAX_CAPACITY;
        
        Object[] newData = Arrays.copyOf(data, newCapacity);
        data = newData;
    }
    
    private void checkBounds(int index) {
        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException("" + index);
    }
    
    private void checkBoundsAdd(int index) {
        if (index < 0 || index > size())
            throw new IndexOutOfBoundsException("" + index);
    }

}
