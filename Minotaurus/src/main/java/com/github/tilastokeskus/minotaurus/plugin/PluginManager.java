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

package com.github.tilastokeskus.minotaurus.plugin;

import com.github.tilastokeskus.minotaurus.maze.MazeGenerator;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides an easy way to access plugins in the program's plugin directory.
 */
public class PluginManager {

    private static List<MazeGenerator> mazeGenerators;
    
    /**
     * Loads and retrieves a list of maze generators from the program's plugin
     * directory.
     * 
     * @return A list of maze generators, or null if none were found.
     */
    public static List<MazeGenerator> getMazeGenerators() {
        mazeGenerators = new ArrayList<>();
        PluginManager.<MazeGenerator>loadPlugins(mazeGenerators);
        return mazeGenerators;
    }
    
    private static <T extends Plugin> void loadPlugins(List<T> pluginList) {
        List<T> plugins = PluginLoader.<T>loadPlugins();
        
        if (plugins != null)
            for (T plugin : plugins)
                PluginManager.<T>handlePlugin(pluginList, plugin);
    }

    private static <T extends Plugin> void handlePlugin(List<T> list, T plugin) {
        String canonicalName = plugin.getClass().getCanonicalName();
        int lastDot = canonicalName.lastIndexOf(".");
        plugin.setTitle(canonicalName.substring(lastDot + 1));
        list.add(plugin);
    }
    
}