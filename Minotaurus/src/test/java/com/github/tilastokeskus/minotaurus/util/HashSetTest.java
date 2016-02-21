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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashSetTest {
    
    private HashSet<Integer> set;
    
    public HashSetTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        set = new HashSet<>();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void hashSetShouldInitiallyBeEmpty() {
        assertTrue(set.isEmpty());
        assertTrue(set.size() == 0);
    }

    @Test
    public void hashSetSizeShouldGrowWhenElementsAreAdded() {
        for (int i = 0; i < 1000; i++) {
            assertTrue(set.size() == i);
            set.add(i);
        }
    }

    @Test
    public void hashSetAddShouldAddElements() {
        for (int i = 0; i < 1000; i++) {
            assertTrue(set.add(i));
            assertTrue(set.contains(i));
        }
    }

    @Test
    public void hashSetAddShouldNotAddDuplicateElements() {
        for (int i = 0; i < 1000; i++) {
            set.add(i);
            assertFalse(set.add(i));
        }
        
        assertTrue(set.size() == 1000);
    }

    @Test
    public void hashSetRemoveShouldRemoveElement() {
        for (int i = 0; i < 1000; i++)
            set.add(i);
        
        for (int i = 0; i < 1000; i++) {
            assertTrue(set.size() == 1000-i);
            assertTrue(set.contains(i));
            assertTrue(set.remove(i));
            assertFalse(set.contains(i));
        }
    }

    @Test
    public void hashSetRemoveShouldReturnFalseIfElementDidNotExists() {
        for (int i = 0; i < 500; i++)
            set.add(i);
        
        for (int i = 500; i < 1000; i++)
            assertFalse(set.remove(i));
    }

    @Test
    public void hashSetClearShouldRemoveAllElements() {
        for (int i = 0; i < 1000; i++)
            set.add(i);
        
        set.clear();
        assertTrue(set.isEmpty());
        
        for (int i = 0; i < 1000; i++)
            assertFalse(set.contains(i));
    }

    @Test
    public void hashSetToArray1ShouldReturnArrayWithCorrectElements() {
        for (int i = 0; i < 1000; i++)
            set.add(i);
        
        boolean found[] = new boolean[1000];
        Integer[] arr = set.toArray(new Integer[1000]);
        for (int i : arr)
            found[i] = true;
        
        for (boolean b : found)
            assertTrue(b);
    }

    @Test
    public void hashSetToArray2ShouldReturnArrayWithCorrectElements() {
        for (int i = 0; i < 1000; i++)
            set.add(i);
        
        boolean found[] = new boolean[1000];
        Object[] arr = set.toArray();
        for (Object o : arr)
            found[(int) o] = true;
        
        for (boolean b : found)
            assertTrue(b);
    }

    @Test
    public void hashSetContainsAllShouldReturnTrueIfSetContainsAllElementsInSomeCollection() {
        List<Integer> l = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 1000; i++) {
            set.add(i);
            l.add(r.nextInt(1000));
        }
        
        assertTrue(set.containsAll(l));
    }

    @Test
    public void hashSetContainsAllShouldReturnFalseIfSetDoesNotContainAllElementsInSomeCollection() {
        List<Integer> l = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 1000; i++) {
            if (i % 2 == 0)
                set.add(i);
            l.add(r.nextInt(1000));
        }
        
        assertFalse(set.containsAll(l));
    }

    @Test
    public void hashSetAddAllShouldAddAllElementsInSomeCollection() {
        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < 500; i++)
            set.add(i);
        for (int i = 500; i < 1000; i++)
            l.add(i);
        
        assertTrue(set.addAll(l));
        for (int i = 0; i < 1000; i++)
            assertTrue(set.contains(i));
    }

    @Test
    public void hashSetAddAllShouldReturnFalseIfElementsExisted() {
        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < 1000; i++)
            l.add(i);
        
        set.addAll(l);
        assertFalse(set.addAll(l));
    }

    @Test
    public void hashSetRetainAllShouldRetainOnlyElementsInSomeCollection() {
        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < 1000; i++)
            set.add(i);
        for (int i = 300; i < 600; i++)
            l.add(i);
        
        assertTrue(set.retainAll(l));
        for (int i = 0; i < 300; i++)
            assertFalse(set.contains(i));
        for (int i = 300; i < 600; i++)
            assertTrue(set.contains(i));
        for (int i = 600; i < 1000; i++)
            assertFalse(set.contains(i));
    }

    @Test
    public void hashSetRetainAllShouldReturnFalseIfSetWasNotModified() {
        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < 1000; i++)
            set.add(i);
        for (int i = 300; i < 600; i++)
            l.add(i);
        
        set.retainAll(l);
        assertFalse(set.retainAll(l));
    }

    @Test
    public void hashSetRemoveAllShouldRemoveElementsInSomeCollection() {
        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < 1000; i++)
            set.add(i);
        for (int i = 300; i < 600; i++)
            l.add(i);
        
        assertTrue(set.removeAll(l));
        for (int i = 0; i < 300; i++)
            assertTrue(set.contains(i));
        for (int i = 300; i < 600; i++)
            assertFalse(set.contains(i));
        for (int i = 600; i < 1000; i++)
            assertTrue(set.contains(i));
    }

    @Test
    public void hashSetRemoveAllShouldReturnFalseIfSetWasNotModified() {
        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < 1000; i++)
            set.add(i);
        for (int i = 300; i < 600; i++)
            l.add(i);
        
        set.removeAll(l);
        assertFalse(set.removeAll(l));
    }
}
