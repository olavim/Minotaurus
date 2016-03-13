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

package com.github.tilastokeskus.minotaurus;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A central access point for the program's fixed resource paths.
 */
public class ResourceManager {
    
    private static final Logger LOGGER = Logger.getLogger(ResourceManager.class.getName());
    
    private static final String PATH = ResourceManager.class
                                        .getProtectionDomain().getCodeSource()
                                        .getLocation().getPath();
    
    /**
     * URL to the program's plugin folder. The plugin folder should contain all
     * the plugins that are used.
     */
    public static final URL PLUGIN_DIR;
    
    /**
     * URL to a delete icon.
     */
    public static final URL IMAGE_DELETE;
    
    /**
     * URL to a settings icon.
     */
    public static final URL IMAGE_SETTINGS;
    
    static {
        PLUGIN_DIR = getURLFromPath("plugins/", true);
        IMAGE_DELETE = getURLFromPath("images/delete.png");
        IMAGE_SETTINGS = getURLFromPath("images/settings.png");
    }
    
    private static URL getURLFromPath(String resource) {
        return getURLFromPath(resource, false);
    }
    
    private static URL getURLFromPath(String resource, boolean createDir) {
        File f = new File(new File(PATH).getParent() + '/' + resource);
        System.out.println(f.getPath());
        
        // Create directory if it doesn't exist (nothing happens if it does).
        if (createDir)
            f.mkdir();
        
        try {
            return f.toURI().toURL();
        } catch (MalformedURLException ex) {
            LOGGER.log(Level.WARNING, null, ex);
            return null;
        }
    }
    
}
