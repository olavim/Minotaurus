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
import com.github.tilastokeskus.minotaurus.util.Pair;
import java.io.IOException;
import java.util.List;
import java.util.jar.Attributes;

public class PluginLoader {
    
    /**
     * Returns a list of suitable class instances that were found inside the
     * plugin directory, paired with Attributes that were pulled from each
     * respective Jar file.
     * 
     * @param <T>   Type (class) of plugins to load.
     * @return      A list of Pairs, where the first element is a class instance
     *              and the second is an Attributes object.
     * @see Attributes
     */
    public static <T> List<Pair<T, Attributes>> loadPlugins() {
        try {
            String pluginFolderPath = ResourceManager.getPluginDirectoryPath();
            JarClassLoader<T> jcl = new JarClassLoader<>(pluginFolderPath, true);
            return jcl.getClassInstanceList();
        } catch (IOException ex) {
            System.out.println("Cannot load plugins: " + ex.getMessage());
        }
        
        return null;
    }

}
