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

import com.github.tilastokeskus.minotaurus.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used to retrieve classes from external files.
 */
public class JarClassLoader {
    
    private static final Logger LOGGER = Logger.getLogger(JarClassLoader.class.getName());
    
    protected URL[] urls;
    protected ClassLoader classLoader;

    /**
     * Creates a new JarClassLoader.
     * 
     * @param url Path to search Jars from.
     * @param recursive If true, subdirectories will be searched.
     */
    public JarClassLoader(URL url, boolean recursive) {
        this.urls = getJarURLs(new File(url.getPath()), recursive);        
        this.classLoader = URLClassLoader.newInstance(urls);
    }

    /**
     * Returns a list of suitable class instances that were found in jar files
     * inside the specified directory.
     * 
     * @param type Classes to search for.
     * @return A list of class instances.
     * @see Attributes
     */
    public <T> List<T> getClassInstanceList(Class<T> type) {
        List<T> classList = new ArrayList<>();
        
        for (URL url : this.urls) {
            try {
                for (Class c : getJarClasses(url, true)) {
                    if (type.isAssignableFrom(c))
                        classList.add((T) c.newInstance());
                }
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
        
        return classList;
    }
    
    /**
     * Returns a list of classes inside a jar archive.
     * 
     * @param url           URL of the jar file.
     * @param ignoreErrors  If true, erroneous classes are ignored.
     * @return              List of classes inside the jar archive.
     * @throws java.io.IOException Could not open file at URL.
     * @throws java.lang.ClassNotFoundException A class inside the jar archive
     *                                          is erroneous.
     */
    public List<Class> getJarClasses(URL url, boolean ignoreErrors)
            throws IOException, ClassNotFoundException {
        List<Class> classes = new ArrayList<>();
        JarFile jarFile = new JarFile(url.getPath());        
        
        Enumeration<JarEntry> e = jarFile.entries();
        while (e.hasMoreElements()) {
            JarEntry entry = e.nextElement();
            Class c = getClassFromJarEntry(entry, ignoreErrors);
            if (c != null)
                classes.add(c);
        }
        
        return classes;
    }
    
    /**
     * Returns a class object from a JarEntry, if the entry is a class file.
     * @param entry         JarEntry to extract class from.
     * @param ignoreErrors  If true, erroneous class files are ignored.
     * @return              A class object, or null if ignoreErrors is true and
     *                      the extracted class is erroneous.
     * @throws ClassNotFoundException If ignoreErrors if false and the extracted
     *                                class is erroneous.
     */
    protected Class getClassFromJarEntry(JarEntry entry, boolean ignoreErrors)
            throws ClassNotFoundException {
        Class c = null;
        
        try {
            if (isClassFile(entry)) {
                String className = getClassName(entry);
                c = classLoader.loadClass(className);
            }
        } catch (ClassNotFoundException ex) {

            // Class file has invalid naming.
            if (!ignoreErrors) {
                throw ex;
            } else {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
        
        return c;
    }
    
    private boolean isClassFile(JarEntry je) {
        return !je.isDirectory() && je.getName().endsWith(".class");
    }
    
    private String getClassName(JarEntry je) {
        String className = je.getName().substring(0, je.getName().length() - 6);
        className = className.replace('/', '.');
        return className;
    }
    
    /**
     * Composes an array of URLs pointing to Jar archives found in a folder.
     * 
     * @param dir       Directory to search.
     * @param recursive If true, subdirectories are searched.
     * @return          Array of Jar archive URLs.
     */
    static URL[] getJarURLs(final File dir, boolean recursive) {
        List<URL> urlList = new ArrayList<>();
        
        if (dir.exists()) {
            for (final File fileEntry : dir.listFiles()) {
                if (fileEntry.isDirectory() && recursive) {
                    URL[] urlsInFolder = getJarURLs(fileEntry, recursive);
                    urlList.addAll(Arrays.asList(urlsInFolder));
                } else if (fileEntry.getName().toLowerCase().endsWith(".jar")) {

                    // File is a Jar archive.
                    try {                    
                        urlList.add(fileEntry.toURI().toURL());
                    } catch (MalformedURLException ex) {
                        LOGGER.log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        
        return urlList.toArray(new URL[]{});
    }
    
}
