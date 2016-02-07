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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class Stack<T> implements List<T> {

    private static final int INITIAL_SIZE = 32;
    
    private Object[] data;
    private int head;
    
    public Stack() {
        data = new Object[INITIAL_SIZE];
        head = 0;
    }
    
    /**
     * Returns and removes the element on top of the stack.
     * @return The element on top of the stack.
     */
    public T pop() {
        return remove(head - 1);
    }
    
    /**
     * Returns the element on top of the stack.
     * @return The element on top of the stack.
     */
    public T peek() {
        return get(head - 1);
    }
    
    @Override
    public int size() {
        return head;
    }

    @Override
    public boolean isEmpty() {
        return head == 0;
    }

    @Override
    public boolean contains(Object o) {
        return this.indexOf(o) >= 0;
    }

    @Override
    public Iterator<T> iterator() {
        return listIterator(0);
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(data, head);
    }

    @Override
    public <E> E[] toArray(E[] a) {
        return (E[]) Arrays.copyOf(data, head, a.getClass());
    }

    @Override
    public boolean add(T e) {
        ensureArrSize(head + 1);
        data[head++] = e;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        return removeAt(index);
    }
    
    public boolean removeAt(int index) {
        if (index >= 0 && index < head) {
            for (int i = index; i < head; i++)
                data[i] = data[i+1];
            
            head--;
            return true;
        }
        
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (indexOf(o) < 0)
                return false;
        
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return addAll(head, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object o : c)
            remove(o);
        
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        for (int i = 0; i < head; i++) {
            if (!c.contains(data[i]))
                removeAt(i);
        }
        
        return true;
    }

    @Override
    public void clear() {
        data = new Object[INITIAL_SIZE];
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureArrSize(head + numNew);
        
        int numMoved = head - index;
        if (numMoved > 0)
            System.arraycopy(data, index, data, index + numNew, numMoved);
        System.arraycopy(a, 0, data, index, numNew);        
        head += numNew;
        
        return numNew > 0;
    }

    @Override
    public T get(int index) {
        if (index >= head)
            throw new IndexOutOfBoundsException();
        return (T) data[index];
    }

    @Override
    public T set(int index, T element) {
        if (index >= head)
            throw new IndexOutOfBoundsException();
        T prev = get(index);
        data[index] = element;
        return prev;
    }

    @Override
    public void add(int index, T element) {
        if (index > head)
            throw new IndexOutOfBoundsException();
        ensureArrSize(head + 1);
        int numMoved = head - index;
        System.arraycopy(data, index, data, index+1, numMoved);
        head++;
        set(index, element);
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= head) 
            throw new IndexOutOfBoundsException();
            
        T prev = get(index);
        int numMoved = head - index;
        System.arraycopy(data, index+1, data, index, numMoved);
        head--;
        return prev;        
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < head; i++)
            if (data[i].equals(o))
                return i;
        
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = head-1; i >= 0; i--)
            if (data[i].equals(o))
                return i;
        
        return -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new Itr(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void ensureArrSize(int size) {
        if (size > data.length) {
            int newSize = size*2;
            Object[] a = new Object[newSize];
            System.arraycopy(data, 0, a, 0, data.length);
            data = a;
        }
    }
    
    private class Itr implements ListIterator<T> {
        
        private int index;
        
        public Itr(int index) {
            this.index = index;
        }

        @Override
        public boolean hasNext() {
            return index < Stack.this.head - 1;
        }

        @Override
        public T next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return Stack.this.get(++index);
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public T previous() {
            if (!hasPrevious())
                throw new NoSuchElementException();
            return Stack.this.get(--index);
        }

        @Override
        public int nextIndex() {
            return index + 1;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public void remove() {
            Stack.this.remove(index);
        }

        @Override
        public void set(T e) {
            Stack.this.set(index, e);
        }

        @Override
        public void add(T e) {
            Stack.this.add(index, e);
        }
        
    }

}
