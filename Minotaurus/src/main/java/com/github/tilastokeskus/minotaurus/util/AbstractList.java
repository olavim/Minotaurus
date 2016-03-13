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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public abstract class AbstractList<E> implements List<E> {
    
    int mods;

    @Override
    abstract public int size();

    @Override
    abstract public E get(int index);

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public Object[] toArray() {
        Object[] a = new Object[size()];
        for (int i = 0; i < size(); i++)
            a[i] = get(i);
        
        return a;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] arr = size() <= a.length ? a : 
                (T[]) Array.newInstance(a.getClass(), size());
        
        for (int i = 0; i < size(); i++)
            arr[i] = (T) get(i);
        
        return a;
    }

    @Override
    public boolean add(E e) {
        add(size(), e);
        return true;
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index == -1)
            return false;
        return remove(indexOf(o)) != null;
    }

    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o))
                return false;
        
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return addAll(size(), c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            add(index++, e);
            modified = true;
        }
        
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c)
            modified |= remove(o);
        
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c)
            if (!contains(o))
                modified |= remove(o);
        
        return modified;
    }

    @Override
    public void clear() {
        Iterator itr = iterator();
        while (itr.hasNext()) {
            itr.next();
            itr.remove();
        }
    }

    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        ListIterator<E> itr = listIterator();
        while (itr.hasNext()) {
            if (Objects.equals(o, itr.next()))
                return itr.previousIndex();
        }
        
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        ListIterator<E> itr = listIterator(size());
        while (itr.hasPrevious()) {
            if (Objects.equals(o, itr.previous()))
                return itr.nextIndex();
        }
        
        return -1;
    }

    @Override
    public Iterator<E> iterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListItr(0);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }
    
    private class Itr implements Iterator<E> {
        
        int cursor = 0;
        int expectedMods = mods;

        @Override
        public boolean hasNext() {
            return cursor != size();
        }

        @Override
        public E next() {
            if (expectedMods != mods)
                throw new ConcurrentModificationException();
            
            try {
                return get(cursor++);
            } catch (IndexOutOfBoundsException ex) {
                throw new NoSuchElementException();
            }
        }
        
    }
    
    private class ListItr implements ListIterator<E> {
        
        int cursor;
        int lastPos;
        int expectedMods;
        
        public ListItr() {
            this(0);
        }
        
        public ListItr(int index) {
            cursor = index;
            lastPos = -1;
            expectedMods = mods;
        }

        @Override
        public boolean hasNext() {
            return cursor != size();
        }

        @Override
        public E next() {
            if (expectedMods != mods)
                throw new ConcurrentModificationException();
            
            try {
                lastPos = cursor;
                return get(cursor++);
            } catch (IndexOutOfBoundsException ex) {
                throw new NoSuchElementException();
            }
        }

        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        @Override
        public E previous() {
            if (expectedMods != mods)
                throw new ConcurrentModificationException();
            
            try {
                lastPos = --cursor;
                return get(cursor);
            } catch (IndexOutOfBoundsException ex) {
                throw new NoSuchElementException();
            }
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            if (lastPos == -1)
                throw new IllegalStateException();
            if (expectedMods != mods)
                throw new ConcurrentModificationException();
            
            AbstractList.this.remove(lastPos);
            if (lastPos < cursor)
                cursor--;
            lastPos = -1;
            expectedMods = mods;
        }

        @Override
        public void set(E e) {
            if (lastPos == -1)
                throw new IllegalStateException();
            if (expectedMods != mods)
                throw new ConcurrentModificationException();
            
            AbstractList.this.set(lastPos, e);
            expectedMods = mods;
        }

        @Override
        public void add(E e) {
            if (expectedMods != mods)
                throw new ConcurrentModificationException();
            
            AbstractList.this.add(cursor++, e);
            lastPos = -1;
            expectedMods = mods;
        }
        
    }

}
