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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class LinkedListStackTest {
    
    private LinkedListStack<Integer> stack;
    
    public LinkedListStackTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        stack = new LinkedListStack<>();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void LinkedListStackSizeShouldInitiallyBeZero() {
        assertTrue(stack.size() == 0);
    }
    
    @Test
    public void LinkedListStackShouldInitiallyBeEmpty() {
        assertTrue(stack.isEmpty());
    }
    
    @Test
    public void LinkedListStackShouldNotBeEmptyWhenItHasElements() {
        assertTrue(stack.add(1));
        assertTrue(!stack.isEmpty());
    }
    
    @Test
    public void LinkedListStackSizeShouldGrowWhenElementsAreAdded() {
        for (int i = 1; i <= 100; i++) {
            stack.add(i);
            assertTrue(stack.size() == i);
        }
    }
    
    @Test
    public void LinkedListStackPopShouldReturnAndRemoveLastElement() {
        for (int i = 1; i <= 100; i++) {
            stack.add(i);
        }
        
        for (int i = 100; i >= 1; i--) {
            assertTrue("Wrong stack size", stack.size() == i);
            int e = stack.pop();
            assertTrue("Wrong element", e == i);            
        }
        
        assertTrue("Stack should be empty", stack.isEmpty());
        assertTrue("Wrong stack size", stack.size() == 0);
    }
    
    @Test
    public void LinkedListStackPeekShouldReturnLastElement() {
        for (int i = 1; i <= 100; i++) {
            stack.add(i);
        }
        
        for (int i = 100; i >= 1; i--) {
            int e = stack.peek();
            assertTrue("Wrong stack size, was" + stack.size() + "should be " + i, stack.size() == i);
            assertTrue("Wrong element", e == i);            
            stack.pop();
        }
        
        assertTrue("Wrong stack size", stack.isEmpty());
    }
    
    @Test
    public void LinkedListStackClearShouldInitialize() {
        for (int i = 1; i <= 100; i++) {
            stack.add(i);
        }
        
        assertTrue(stack.size() == 100);
        stack.clear();
        assertTrue(stack.isEmpty());
        assertTrue(stack.size() == 0);
        assertTrue(stack.head == LinkedListStack.ROOT);
    }
}
