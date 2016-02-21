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

import com.github.tilastokeskus.minotaurus.ResourceManager;
import java.util.List;

/**
 * Provides an easy way to access plugins in the program's plugin directory.
 */
public class PluginLoader {
    
    private static JarClassLoader classLoader = new JarClassLoader(
            ResourceManager.getPluginDirectoryPath(), true);
    
    /**
     * Retrieves a list of plugins with the type parameter's class from the
     * plugin directory, and sets an appropriate title for them.
     * 
     * @param <T>
     * @param type Type (class) of plugins to load.
     * @return List of plugins found in the program's plugin directory that
     *         are assignable to the class defined by the type parameter.
     */
    public static <T extends Plugin> List<T> loadPlugins(Class<T> type) {
        List<T> plugins = classLoader.getClassInstanceList(type);
        
        if (plugins != null)
            for (T plugin : plugins)
                setPluginTitle(plugin);
                
        return plugins;
    }
    
    public static void setPluginDir(String path) {
        System.out.println(path);
        classLoader = new JarClassLoader(path, true);
    }

    private static void setPluginTitle(Plugin plugin) {
        String canonicalName = plugin.getClass().getCanonicalName();
        int lastDot = canonicalName.lastIndexOf(".");
        plugin.setTitle(canonicalName.substring(lastDot + 1));
    }
    
}
