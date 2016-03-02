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

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

public class HashMap<K, V> implements Map<K, V> {
    
    static final int INITIAL_CAPACITY = 16;
    static final int MAX_CAPACITY = 1<<30;
    static final float LOAD_FACTOR = 0.75f;
    
    Entry[] table;    
    int size;
    volatile int mods;
    
    public HashMap() {
        this.table = new Entry[INITIAL_CAPACITY];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        int hash = hash(key);
        int bucket = getBucketIndex(hash, table.length);
        for (Entry<K, V> e = table[bucket]; e != null; e = e.next) {
            if (e.hash == hash && key.equals(e.key))
                return true;
        }
        
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Entry<K, V> e : table)
            for (; e != null; e = e.next)
                if (value.equals(e.value))
                    return true;
        
        return false;
    }

    @Override
    public V get(Object key) {
        int hash = hash(key);
        int bucket = getBucketIndex(hash, table.length);
        
        for (Entry<K, V> e = table[bucket]; e != null; e = e.next) {
            if (e.hash == hash && key.equals(e.key))
                return e.value;
        }
        
        return null;
    }

    @Override
    public V put(K key, V value) {
        int hash = hash(key);
        int bucket = getBucketIndex(hash, table.length);
        for (Entry<K, V> e = table[bucket]; e != null; e = e.next) {
            if (e.hash == hash && key.equals(e.key))
                return e.setValue(value);
        }
        
        addNewEntry(table, hash, bucket, key, value);
        mods++;
        return null;
    }

    @Override
    public V remove(Object key) {
        int hash = hash(key);
        int bucket = getBucketIndex(hash, table.length);
        
        Entry<K, V> prev = table[bucket];
        Entry<K, V> e = table[bucket];
        Entry<K, V> next;
        
        while (e != null) {
            next = e.next;
            
            if (e.hash == hash && key.equals(e.key)) {
                if (e != prev) {
                    prev.next = next;
                } else {
                    
                    // key was found in the bucket's first entry
                    table[bucket] = next;
                }
                
                mods++;
                size--;
                return e.value;
            }
            
            prev = e;
            e = next;
        }
        
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet())
            put(entry.getKey(), entry.getValue());
    }

    @Override
    public void clear() {
        mods++;
        for (int i = 0; i < table.length; i++)
            table[i] = null;
        
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        return new KeySet();
    }

    @Override
    public Collection<V> values() {
        return new Values();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new EntrySet();
    }
    
    void addNewEntry(Entry[] t, int hash, int bucket, K key, V value) {
        Entry<K, V> e = t[bucket];
        t[bucket] = new Entry<>(hash, key, value, e);
        if (shouldResize(size++))
            resizeTable(table.length * 2);
    }
    
    int getBucketIndex(int hash, int capacity) {
        return hash % capacity;
    }
    
    boolean shouldResize(int numElements) {
        return numElements > LOAD_FACTOR * table.length;
    }
    
    int hash(Object obj) {
        return Math.abs(obj.hashCode());
    }
    
    Iterator<Map.Entry<K, V>> entryIterator() {
        return new EntryIterator();
    }
    
    Iterator<K> keyIterator() {
        return new KeyIterator();
    }
    
    Iterator<V> valueIterator() {
        return new ValueIterator();
    }
    
    boolean resizeTable(int newCapacity) {
        if (table.length >= MAX_CAPACITY)
            return false;
        
        if (newCapacity > MAX_CAPACITY)
            newCapacity = MAX_CAPACITY;
        
        Entry[] newTable = new Entry[newCapacity];
        rehash(table, newTable);
        table = newTable;
        return true;
    }
    
    void rehash(Entry[] src, Entry[] dest) {
        
        // Iterate through all buckets.
        for (int i = 0; i < src.length; i++) {
            Entry<K, V> e = src[i];
            
            if (e != null)
                src[i] = null;
                
            // Iterate through all the entries in the bucket.
            while (e != null) {
                Entry<K, V> next = e.next;

                // Rehash for new capacity.
                int newBucket = getBucketIndex(e.hash, dest.length);

                // Push the entry to the beginning of the new bucket
                e.next = dest[newBucket];
                dest[newBucket] = e;

                e = next;
            }
        }
    }
    
    static class Entry<K, V> implements Map.Entry<K, V> {
        
        final K key;
        V value;
        final int hash;
        Entry<K, V> next;
        
        public Entry(int hash, K key, V value, Entry<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
        
        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public int hashCode() {
            return (key == null ? 0 : key.hashCode()) ^
                   (value == null ? 0 : value.hashCode());
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
                
            if (obj == null)
                return false;
                
            if (getClass() != obj.getClass())
                return false;
                
            final Entry<?, ?> other = (Entry<?, ?>) obj;                
            return Objects.equals(this.key, other.key) 
                    && Objects.equals(this.value, other.value);
        }
        
    }
    
    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return entryIterator();
        }

        @Override
        public int size() {
            return size;
        }        
    }
    
    private class KeySet extends AbstractSet<K> {
        @Override
        public Iterator<K> iterator() {
            return keyIterator();
        }

        @Override
        public int size() {
            return size;
        }        
    }
    
    private class Values extends AbstractCollection<V> {
        @Override
        public Iterator<V> iterator() {
            return valueIterator();
        }

        @Override
        public int size() {
            return size;
        }
    }
    
    private abstract class MapIterator<E> implements Iterator<E> {
        Entry<K, V> next;
        Entry<K, V> current;
        int bucketIndex;
        int expectedMods;
        
        MapIterator() {
            expectedMods = mods;
            if (size > 0) {
                while (bucketIndex < table.length)
                    if ((next = table[bucketIndex++]) != null)
                        break;
            }
        }
        
        @Override
        public boolean hasNext() {
            return next != null;
        }
        
        @Override
        public void remove() {
            if (current == null)
                throw new IllegalStateException();
            if (expectedMods != mods)
                throw new ConcurrentModificationException();
            
            K k = current.key;
            current = null;
            HashMap.this.remove(k);
            expectedMods = mods;
        }

        public Entry<K, V> nextEntry() {
            if (next == null)
                throw new NoSuchElementException();
            if (expectedMods != mods)
                throw new ConcurrentModificationException();
                
            Entry<K, V> e = next;
            current = e;
            next = e.next;
            
            if (next == null) {
                while (bucketIndex < table.length)
                    if ((next = table[bucketIndex++]) != null)
                        break;
            }
            
            return e;
        }        
    }
    
    private class EntryIterator extends MapIterator<Map.Entry<K, V>> {
        @Override
        public Entry<K, V> next() {
            return nextEntry();
        }        
    }
    
    private class KeyIterator extends MapIterator<K> {
        @Override
        public K next() {
            return nextEntry().getKey();
        }        
    }
    
    private class ValueIterator extends MapIterator<V> {
        @Override
        public V next() {
            return nextEntry().getValue();
        }        
    }

}
