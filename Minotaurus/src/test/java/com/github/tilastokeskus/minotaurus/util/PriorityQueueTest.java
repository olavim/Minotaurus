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

import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class PriorityQueueTest {
    
    PriorityQueue<Integer> queue;
    
    public PriorityQueueTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        queue = new PriorityQueue<>();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void priorityQueueAddShouldAddNewElementToQueue() {
        for (int i = 1; i <= 1000; i++) {
            queue.add(i);
            assertTrue(queue.size() == i);
        }
    }

    @Test
    public void priorityQueueMinSHouldReturnMinimumElementInQueue() {
        int min = Integer.MAX_VALUE;
        Random r = new Random();
        for (int i = 1; i <= 1000; i++) {
            int n = r.nextInt();
            if (n < min)
                min = n;
            queue.add(n);
            assertTrue(queue.min() == min);
        }
    }

    @Test
    public void priorityQueueExtractMinShouldReturnMinimumElementInQueueAndRemoveIt() {
        int min = Integer.MAX_VALUE;
        Random r = new Random();
        for (int i = 1; i <= 1000; i++) {
            int n = r.nextInt();
            if (n < min)
                min = n;
            queue.add(n);
        }
        
        assertTrue(queue.size() == 1000);
        assertTrue(queue.extractMin() == min);
        assertTrue(queue.size() == 999);
    }

    @Test
    public void priorityQueueClearShouldRemoveAllElements() {
        for (int i = 1; i <= 1000; i++)
            queue.add(i);
        
        assertTrue(queue.size() == 1000);
        queue.clear();
        assertTrue(queue.size() == 0);
        assertTrue(queue.min() == null);
    }
}
