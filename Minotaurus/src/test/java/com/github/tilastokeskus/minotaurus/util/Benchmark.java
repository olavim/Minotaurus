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

public class Benchmark {
    
    Runnable before;
    Runnable r;
    Runnable after;
    
    public Benchmark(Runnable r) {
        this(() -> {}, r, () -> {});
    }
    
    public Benchmark(Runnable before, Runnable r) {
        this(before, r, () -> {});
    }
    
    public Benchmark(Runnable before, Runnable r, Runnable after) {
        this.before = before;
        this.r = r;
        this.after = after;
    }
    
    public int runBenchmark(int times) {        
        return getBenchmarkAverage(() -> {
            before.run();
            
            long start = System.nanoTime();
            r.run();
            long end = System.nanoTime();
            
            after.run();
            return millis(start, end);
        }, times);
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

}
