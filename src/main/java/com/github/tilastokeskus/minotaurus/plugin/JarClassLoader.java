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

import com.github.tilastokeskus.minotaurus.util.ClassUtils;
import com.github.tilastokeskus.minotaurus.util.Pair;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Attributes;

/**
 * Used to retrieve classes from external files.
 * 
 * @param <T> Class to search. Subclasses are matched.
 */
public class JarClassLoader<T> {
    
    private URL[] urls;
    private boolean recursive;

    /**
     * Creates a new JarClassLoader.
     * 
     * @param directoryPath Path to search Jars from.
     * @param recursive     If true, subdirectories will be searched.
     * @throws IOException 
     */
    public JarClassLoader(String directoryPath, boolean recursive)
            throws IOException {
        this.recursive = recursive;
        
        try {
            this.urls = getFolderURLs(new File(directoryPath));
        } catch (NullPointerException ex) {
            throw new IOException("Directory not found: " + directoryPath);
        }

        if (urls.length == 0) {
            throw new IOException("No plugins found");
        }
    }
    
    private Attributes getMainAttributes(URL url) throws IOException {
	URL u = new URL("jar", "", url + "!/");
	JarURLConnection uc = (JarURLConnection) u.openConnection();
	Attributes attr = uc.getMainAttributes();
        return attr;
    }

    /**
     * Returns a list of suitable class instances that were found inside the
     * specified directory, paired with Attributes that were pulled from each
     * respective Jar file.
     * 
     * @return A list of Pairs, where the first element is a class instance and
     *         the second is an Attributes object.
     * @see Attributes
     */
    public List<Pair<T, Attributes>> getClassInstanceList() {
        List<Pair<T, Attributes>> classList = new ArrayList<>();
        URLClassLoader classLoader = URLClassLoader.newInstance(urls);
        
        for (URL url : urls) {
            try {
                Attributes attr = getMainAttributes(url);
                String mainClass = attr.getValue(Attributes.Name.MAIN_CLASS);
                Class clazz = classLoader.loadClass(mainClass);
                
                if (isAssignableFrom(clazz)) {
                    
                    /* Found class is assignable from the one we are searching,
                     * or in other words, fits our search criteria.
                     */
                    T instance = (T) clazz.newInstance();
                    classList.add(new Pair(instance, attr));
                }
            } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                System.err.println("Cannot load plugins: " + ex.getMessage());
            }
        }
        
        return classList;
    }
    
    private boolean isAssignableFrom(Class<?> clazz) {
        T cast = ClassUtils.<T>getClassObject();
        return cast.getClass().isAssignableFrom(clazz);
    }
    
    private URL[] getFolderURLs(final File folder) throws MalformedURLException {
        ArrayList<URL> urlList = new ArrayList<>();
        
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory() && recursive) {
                URL[] urlsInFolder = getFolderURLs(fileEntry);
                urlList.addAll(Arrays.asList(urlsInFolder));
            } else if (fileEntry.getName().toLowerCase().endsWith(".jar")) {
                
                // File is a Jar archive.
                urlList.add(fileEntry.toURI().toURL());
            }
        }
        
        return urlList.toArray(new URL[]{});
    }
    
}