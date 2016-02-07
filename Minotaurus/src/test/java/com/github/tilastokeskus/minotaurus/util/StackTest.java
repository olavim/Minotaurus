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

/**
 *
 * @author Olavi Mustanoja
 */
public class StackTest {
    
    private Stack<Integer> stack;
    
    public StackTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        stack = new Stack<>();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void StackSizeShouldInitiallyBeZero() {
        assertTrue(stack.size() == 0);
    }
    
    @Test
    public void StackShouldInitiallyBeEmpty() {
        assertTrue(stack.isEmpty());
    }
    
    @Test
    public void StackCapacityShouldGrowWhenElementsAreAdded() {
        // initial capacity is 10, should be able to add more than 10 elements.
        for (int i = 0; i < 20; i++)
            assertTrue(stack.add(i));
        
        for (int i = 19; i >= 0; i--)
            assertTrue(stack.pop() == i);
    }
    
    @Test
    public void StackShouldNotBeEmptyWhenItHasElements() {
        assertTrue(stack.add(1));
        assertTrue(!stack.isEmpty());
    }
    
    @Test
    public void StackSizeShouldGrowWhenElementsAreAdded() {
        for (int i = 1; i <= 100; i++) {
            stack.add(i);
            assertTrue(stack.size() == i);
        }
    }
    
    @Test
    public void StackPopShouldReturnAndRemoveLastElement() {
        for (int i = 1; i <= 100; i++) {
            stack.add(i);
        }
        
        for (int i = 100; i >= 1; i--) {
            assertTrue("Wrong stack size", stack.size() == i);
            int e = stack.pop();
            assertTrue("Wrong element", e == i);            
        }
        
        assertTrue("Stack should be empty", stack.isEmpty());
    }
    
    @Test
    public void StackPeekShouldReturnLastElement() {
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
    public void StackContainsShouldWork() {
        for (int i = 0; i <= 100; i+=2) {
            stack.add(i);
        }
        
        for (int i = 0; i <= 100; i++) {
            if (i%2 == 0)
                assertTrue(stack.contains(i));
            else
                assertTrue(!stack.contains(i));
        }
    }
    
    @Test
    public void StackClearShouldInitialize() {
        for (int i = 1; i <= 100; i++) {
            stack.add(i);
        }
        
        assertTrue(stack.size() == 100);
        stack.clear();
        assertTrue(stack.isEmpty());
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
