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

import com.github.tilastokeskus.minotaurus.exception.ThrowableAssertion;
import com.github.tilastokeskus.minotaurus.util.HashMap.Entry;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashMapTest {
    
    private final boolean DO_BENCHMARK = false;
    
    private HashMap<Integer, Integer> map;
    
    public HashMapTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        map = new HashMap<>();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void hashMapIsEmptyShouldReturnTrueWhenMapHoldsNoKeys() {
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void hashMapIsEmptyShouldReturnFalseWhenMapHoldsKeys() {
        map.put(1, 2);
        assertFalse(map.isEmpty());
    }
    
    @Test
    public void hashMapClearShouldRemoveAllKeys() {
        for (int i = 0; i < 1000; i++)
            map.put(i, i);
        map.clear();
        assertTrue(map.isEmpty());
        
        for (int i = 0; i < 1000; i++)
            assertFalse(map.containsKey(i));
    }
    
    @Test
    public void hashMapAddNewEntryShouldIncreaseSize() {
        for (int i = 0; i < 100; i++) {
            assertTrue(map.size() == i);
            map.addNewEntry(map.table, i, 0, i, i);
        }
    }
    
    @Test
    public void hashMapAddNewEntryShouldAddNewEntry() {
        for (int i = 0; i < 100; i++) {
            int bucket = map.getBucketIndex(map.hash(i), map.table.length);
            map.addNewEntry(map.table, i, bucket, i, i*2);
            assertTrue(map.containsKey(i));
            assertTrue(map.get(i) == i*2);
        }
    }
    
    @Test
    public void hashMapPutShouldIncreaseSizeWhenKeyIsNew() {
        for (int i = 0; i < 1000; i++) {
            assertTrue(map.size() == i);
            map.put(i, i);
        }
    }
    
    @Test
    public void hashMapPutShouldBeAbleToPutObjectsWithNegativeHashCode() {
        for (int i = -1000; i < 1000; i++)
            map.put(i, i*2);
        for (int i = -1000; i < 1000; i++) 
            assertTrue(map.get(i) == i*2);
    }
    
    @Test
    public void hashMapPutShouldReturnPreviouslyStoredValue() {
        for (int i = 0; i < 1000; i++)
            map.put(i, i*2);
        for (int i = 0; i < 1000; i++)
            assertTrue(map.put(i, i) == i*2);
    }
    
    @Test
    public void hashMapGetShouldGetValueForKey() {
        for (int i = 0; i < 1000; i++) {
            map.put(i, i*2);
        }
        
        for (int i = 0; i < 1000; i++) {
            assertTrue(map.get(i) == i*2);
        }
        
        assertNull(map.get(1000));
    }
    
    @Test
    public void hashMapContainsKeyShouldReturnTrueIfKeyExists() {
        for (int i = 0; i < 1000; i++)
            map.put(i, i);
        
        for (int i = 0; i < 1000; i++)
            assertTrue(map.containsKey(i));
    }
    
    @Test
    public void hashMapContainsKeyShouldReturnFalseIfKeyDoesNotExist() {
        for (int i = 0; i < 1000; i+=2)
            map.put(i, i);
        
        for (int i = 1; i < 1000; i+=2)
            assertFalse(map.containsKey(i));
    }
    
    @Test
    public void hashMapContainsValueShouldReturnTrueIfValueExists() {
        for (int i = 0; i < 1000; i+=2)
            map.put(i, i);
        
        for (int i = 0; i < 1000; i+=2)
            assertTrue(map.containsValue(i));
    }
    
    @Test
    public void hashMapContainsValueShouldReturnFalseIfValueDoesNotExist() {
        for (int i = 0; i < 1000; i+=2)
            map.put(i, i);
        
        for (int i = 1; i < 1000; i+=2)
            assertFalse(map.containsValue(i));
    }
    
    @Test
    public void hashMapRemoveShouldRemoveEntry() {
        for (int i = 0; i < 1000; i++)
            map.put(i, i*2);
        
        for (int i = 0; i < 1000; i++) {
            assertTrue(map.remove(i) == i*2);
            assertTrue(map.size() == 1000 - i - 1);    
            
            for (int j = i+1; j < 1000; j++) {
                assertTrue(map.containsKey(j));
                assertTrue(map.get(j) == j*2);
            }
        }
    }
    
    @Test
    public void hashMapRemoveShouldReturnNullForNonExistingKey() {
        for (int i = 0; i < 1000; i+=2)
            map.put(i, i);
        
        for (int i = 1; i < 1000; i+=2)
            assertTrue(map.remove(i) == null);
    }
    
    @Test
    public void hashMapPutAllShouldPutAllEntriesToMap() {
        HashMap<Integer, Integer> map2 = new HashMap<>();
        for (int i = 0; i < 500; i++)
            map.put(i, -i);
        for (int i = 500; i < 1000; i++)
            map2.put(i, -i);
        
        map.putAll(map2);
        for (int i = 0; i < 1000; i++) {
            assertTrue(map.containsKey(i));
            assertTrue(map.get(i) == -i);
        }
    }
    
    @Test
    public void hashMapKeySetReturnsSetWithAllKeys() {
        for (int i = 0; i < 1000; i++)
            map.put(i, i);
        
        boolean[] found = new boolean[1000];
        for (int key : map.keySet())
            found[key] = true;
        
        for (int i = 0; i < 1000; i++)
            assertTrue(found[i]);
    }
    
    @Test
    public void hashMapValuesReturnsCollectionWithAllValues() {
        for (int i = 0; i < 1000; i++)
            map.put(i, i*2);
        
        boolean[] found = new boolean[2000];
        for (int key : map.values())
            found[key] = true;
        
        for (int i = 0; i < 2000; i++) {
            if (i % 2 == 0)
                assertTrue(found[i]);
            else
                assertFalse(found[i]);
        }
    }
    
    @Test
    public void hashMapResizeTableShouldResizeTableAndRehashEntries() {
        for (int i = 0; i < 10; i++)
            map.put(i, i*2);
        
        int newCapacity = map.table.length * 2;
        assertTrue(map.resizeTable(newCapacity));
        assertTrue(map.table.length == newCapacity);
        
        for (int i = 0; i < 10; i++)
            assertTrue(map.containsKey(i));
    }
    
    @Test
    public void hashMapResizeTableShouldReturnFalseIfMaxCapacityHasBeenReached() {
        try {
            assertTrue(map.resizeTable(HashMap.MAX_CAPACITY));
            assertFalse(map.resizeTable(HashMap.MAX_CAPACITY));
        } catch (OutOfMemoryError er) {
            // can't test in this environment.
        }
    }
    
    @Test
    public void hashMapShouldResizeShouldReturnFalseIfSizeIsBelowTreshold() {
        float treshold = HashMap.LOAD_FACTOR * map.table.length;
        for (int i = 0; i <= treshold; i++)
            assertFalse(map.shouldResize(map.size++));
        assertTrue(map.shouldResize(map.size));
    }
    
    @Test
    public void hashMapRehashShouldRehashEntries() {
        Entry[] table = new Entry[5];
        for (int i = 0; i < 5; i++)
            map.addNewEntry(table, 35+i, i, 35+i, 1);
        
        int[] oldBuckets = new int[10];
        for (int i = 0; i < 5; i++)
            if (table[i] != null)
                oldBuckets[i] = (int) table[i].key;
        
        Entry[] newTable = new Entry[10];
        map.rehash(table, newTable);
        
        int[] newBuckets = new int[10];
        for (int i = 0; i < 10; i++)
            if (newTable[i] != null)
                newBuckets[i] = (int) newTable[i].key;
        
        int numSame = 0;
        for (int i = 0; i < 10; i++)
            if (oldBuckets[i] == newBuckets[i])
                numSame++;
        
        assertTrue(numSame < 5);
    }
    
    @Test
    public void entryEqualsAndHashCodeShouldWorkProperly() {
        Entry<Integer, Integer> e1 = new Entry<>(1, 1, 1, null);
        Entry<Integer, Integer> e2 = new Entry<>(1, 1, 1, null);
        Entry<Integer, Integer> e3 = new Entry<>(2, 2, 1, null);
        assertTrue(e1.equals(e2));
        assertTrue(e1.hashCode() == e2.hashCode());
        assertFalse(e1.equals(e3));
        assertFalse(e1.hashCode() == e3.hashCode());
    }
    
    @Test
    public void hashMapEntryIteratorShouldWorkProperly() {
        Iterator<Map.Entry<Integer, Integer>> itr1 = map.entryIterator();
        ThrowableAssertion.assertThrown(() -> itr1.remove())
                .expect(IllegalStateException.class);
        ThrowableAssertion.assertThrown(() -> itr1.next())
                .expect(NoSuchElementException.class);
        
        map.put(1, 1);
        Iterator<Map.Entry<Integer, Integer>> itr2 = map.entryIterator();
        assertTrue(itr2.next().getKey() == 1);
        itr2.remove();
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void hashMapPerformanceTest() {
        if (DO_BENCHMARK) {
            hashMapPerformanceTestIntegers();
            hashMapPerformanceTestStrings();
        }
    }
    
    public void hashMapPerformanceTestIntegers() {
        int numIters = 1000000;
        
        int putAvg = makeBenchmark(() -> {
            map.clear();
            map.table = new Entry[HashMap.INITIAL_CAPACITY];
        }, () -> {
            for (int i = 0; i < numIters; i++)
                map.put(i, i);
        });
        
        int containsAvg = makeBenchmark(() -> {}, () -> {
            for (int i = 0; i < numIters; i++)
                map.containsKey(i);
        });
        
        int getAvg = makeBenchmark(() -> {
            for (int i = 0; i < numIters; i++)
                map.get(i);
        });
        
        int removeAvg = makeBenchmark(() -> {
            map.clear();
            map.table = new Entry[HashMap.INITIAL_CAPACITY];
            for (int i = 0; i < numIters; i++)
                map.put(i, i);
        }, () -> {
            for (int i = 0; i < numIters; i++)
                map.remove(i);
        });
        
        System.out.println("Integers - Put: " + putAvg);        
        System.out.println("Integers - Contains: " + containsAvg);        
        System.out.println("Integers - Get: " + getAvg);        
        System.out.println("Integers - Remove: " + removeAvg);        
    }
    
    public void hashMapPerformanceTestStrings() {
        int numIters = 1000000;
        
        long start = System.nanoTime();
        String[] strings = new String[numIters];
        char str[] = new char[50];
        Random r = new Random();
        for (int n = 0; n < numIters; n++) {
            for (int i = 0; i < 50; i++)
                str[i] = (char) r.nextInt('z');
            strings[n] = new String(str);
        }
        long end = System.nanoTime();
        System.out.println("String gen took " + millis(start, end) + "ms");
        
        HashMap<String, String> m = new HashMap<>();
        
        int putAvg = makeBenchmark(() -> {
            m.clear();
            m.table = new Entry[HashMap.INITIAL_CAPACITY];
        }, () -> {
            for (int i = 0; i < numIters; i++)
                m.put(strings[i], strings[i]);
        });
        
        int containsAvg = makeBenchmark(() -> {}, () -> {
            for (int i = 0; i < numIters; i++)
                m.containsKey(strings[i]);
        });
        
        int getAvg = makeBenchmark(() -> {
            for (int i = 0; i < numIters; i++)
                m.get(strings[i]);
        });
        
        int removeAvg = makeBenchmark(() -> {
            m.clear();
            m.table = new Entry[HashMap.INITIAL_CAPACITY];
            for (int i = 0; i < numIters; i++)
                m.put(strings[i], strings[i]);
        }, () -> {
            for (int i = 0; i < numIters; i++)
                m.remove(strings[i]);
        });
        
        System.out.println("Strings - Put: " + putAvg);        
        System.out.println("Strings - Contains: " + containsAvg);        
        System.out.println("Strings - Get: " + getAvg);        
        System.out.println("Strings - Remove: " + removeAvg);        
    }
    
    private int makeBenchmark(Runnable r) {
        return makeBenchmark(() -> {}, r, () -> {});
    }
    
    private int makeBenchmark(Runnable before, Runnable r) {
        return makeBenchmark(before, r, () -> {});
    }
    
    private int makeBenchmark(Runnable before, Runnable r, Runnable after) {
        return getBenchmarkAverage(() -> {
            before.run();
            
            long start = System.nanoTime();
            r.run();
            long end = System.nanoTime();
            
            after.run();
            return millis(start, end);
        }, 100);
    }
    
    private int getBenchmarkAverage(Benchmarkable b, int times) {
        
        // warmup
        for (int i = 0; i < 100; i++)
            b.run();
        
        int totalTime = 0;
        for (int i = 0; i < times; i++)
            totalTime += b.run();
        
        return totalTime / times;
    }
    
    private int millis(long start, long end) {
        return (int) ((end - start) / 1000000);
    }
    
    private interface Benchmarkable {
        
        /**
         * Run a piece of code.
         * 
         * @return Time it took to run the code.
         */
        int run();
    }
}
