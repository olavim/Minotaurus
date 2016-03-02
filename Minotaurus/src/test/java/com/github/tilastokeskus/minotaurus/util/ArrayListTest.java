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

import static com.github.tilastokeskus.minotaurus.exception.ThrowableAssertion.assertThrown;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayListTest {
    
    private final static int ITERATIONS = 1000;
    
    private ArrayList<Integer> list;
    
    public ArrayListTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        list = new ArrayList<>();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void arrayListSizeShouldGrowWhenElementsAreAddedToTheList() {
        for (int i = 0; i < ITERATIONS; i++) {
            list.add(i);
            assertTrue(list.size() == i+1);
        }
    }

    @Test
    public void arrayListAddShouldAddElementAtEndOfList() {
        for (int i = 0; i < ITERATIONS; i++) {
            list.add(i);
            assertTrue(list.get(list.size() - 1) == i);
        }
    }

    @Test
    public void arrayListAddShouldAddElementAtIndex() {
        for (int i = 0; i < ITERATIONS; i++)
            list.add(0);
        
        list.add(100, 1);
        list.add(88, 2);
        list.add(50, 3);
        list.add(25, 4);
        
        assertTrue(list.get(103) == 1);
        assertTrue(list.get(90) == 2);
        assertTrue(list.get(51) == 3);
        assertTrue(list.get(25) == 4);
    }

    @Test
    public void arrayListContainsShouldReturnTrueIfElementExists() {
        for (int i = 0; i < ITERATIONS; i++) {
            list.add(i);
            assertTrue(list.contains(i));
        }
    }

    @Test
    public void arrayListSetShouldSetElementAtIndex() {
        for (int i = 0; i < ITERATIONS; i++)
            list.add(i);
        
        for (int i = ITERATIONS - 1; i > 0; i--) {
            assertTrue(list.get(i) == i);
            list.set(i, ITERATIONS - i);
            assertTrue(list.get(i) == ITERATIONS - i);
        }
    }

    @Test
    public void arrayListRemoveShouldRemoveElementByObject() {
        for (int i = 0; i < ITERATIONS; i++)
            list.add(i);
        
        for (int i = 0; i < ITERATIONS; i+=2) {
            assertTrue(list.contains(i));
            list.remove(new Integer(i));
            assertFalse(list.contains(i));
        }
        
        assertTrue(list.size() == ITERATIONS/2);
    }

    @Test
    public void arrayListAddShouldThrowExceptionWhenIndexIsOutOfBounds() {
        assertThrown(() -> list.add(1, 0)).expect(IndexOutOfBoundsException.class);
        for (int i = 0; i < ITERATIONS; i++)
            list.add(i);
        assertThrown(() -> list.add(-1, 0)).expect(IndexOutOfBoundsException.class);
        assertThrown(() -> list.add(ITERATIONS + 1, 0)).expect(IndexOutOfBoundsException.class);
    }

    @Test
    public void arrayListGetShouldThrowExceptionWhenIndexIsOutOfBounds() {
        assertThrown(() -> list.get(0)).expect(IndexOutOfBoundsException.class);
        assertThrown(() -> list.get(-1)).expect(IndexOutOfBoundsException.class);
        for (int i = 0; i < ITERATIONS; i++)
            list.add(i);
        assertThrown(() -> list.get(ITERATIONS)).expect(IndexOutOfBoundsException.class);
    }

    @Test
    public void arrayListSetShouldThrowExceptionWhenIndexIsOutOfBounds() {
        assertThrown(() -> list.set(0, 0)).expect(IndexOutOfBoundsException.class);
        for (int i = 0; i < ITERATIONS; i++)
            list.add(i);
        assertThrown(() -> list.set(-1, 0)).expect(IndexOutOfBoundsException.class);
        assertThrown(() -> list.set(ITERATIONS, 0)).expect(IndexOutOfBoundsException.class);
    }

    @Test
    public void arrayListContainsShouldReturnFalseIfElementDoesNotExist() {
        for (int i = 0; i < ITERATIONS; i+=2)
            list.add(i);
        
        for (int i = 1; i < ITERATIONS; i+=2)
            assertFalse(list.contains(i));
    }

    @Test
    public void arrayListToArrayReturnsAnArrayFromList1() {
        for (int i = 0; i < ITERATIONS; i++)
            list.add(i);
        
        Object[] arr = list.toArray();
        assertTrue(arr.length == ITERATIONS);
        for (int i = 0; i < ITERATIONS; i++)
            assertTrue((int) arr[i] == i);
    }

    @Test
    public void arrayListToArrayReturnsAnArrayFromList2() {
        for (int i = 0; i < ITERATIONS; i++)
            list.add(i);
        
        Integer[] arr = list.toArray(new Integer[] {});
        assertTrue(arr.length == ITERATIONS);
        for (int i = 0; i < ITERATIONS; i++)
            assertTrue(arr[i] == i);
    }

    @Test
    public void arrayListShouldResizeShouldReturnTrueIfProbedSizeWouldGoBeyondCapacity() {
        assertTrue(list.shouldResize(17));
    }

    @Test
    public void arrayListShouldResizeShouldReturnFalseIfProbedSizeWouldNotGoBeyondCapacity() {
        assertFalse(list.shouldResize(0));
        assertFalse(list.shouldResize(1));
        assertFalse(list.shouldResize(2));
        assertFalse(list.shouldResize(12));
        assertFalse(list.shouldResize(16));
    }
}
