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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class JarClassLoaderTest {
    
    private final URL testPluginPath = getClass().getResource("/plugins/");
    
    private JarClassLoader jcl;
    
    public JarClassLoaderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        jcl = new JarClassLoader(testPluginPath, true);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void JarClassLoaderShouldContainCorrectURLs() {
        List<String> pluginPaths = Arrays.asList(jcl.urls).stream()
                .map(u -> u.getPath().substring(u.getPath().lastIndexOf('/')))
                .collect(Collectors.toList());
        assertTrue(pluginPaths.contains("/TestPlugin.jar"));
        assertTrue(pluginPaths.contains("/InvalidTestPlugin.jar"));
    }

    @Test
    public void JarClassLoaderGetJarURLsShouldLoadCorrectURLsWhenRecursing() {
        URL[] urls = jcl.getJarURLs(new File(testPluginPath.getPath()), true);
        List<String> pluginPaths = Arrays.asList(urls).stream()
                .map(u -> u.getPath().substring(u.getPath().lastIndexOf('/')))
                .collect(Collectors.toList());
        assertTrue(pluginPaths.contains("/TestPlugin.jar"));
        assertTrue(pluginPaths.contains("/InvalidTestPlugin.jar"));
    }

    @Test
    public void JarClassLoaderGetJarURLsShouldLoadCorrectURLsWhenNotRecursing() {
        URL[] urls = jcl.getJarURLs(new File(testPluginPath.getPath()), false);
        List<String> pluginPaths = Arrays.asList(urls).stream()
                .map(u -> u.getPath().substring(u.getPath().lastIndexOf('/')))
                .collect(Collectors.toList());
        assertTrue(pluginPaths.contains("/TestPlugin.jar"));
        assertFalse(pluginPaths.contains("/InvalidTestPlugin.jar"));
    }
    
    @Test
    public void JarClassLoaderGetClassFromJarEntryShouldReturnCorrectClassObjects() throws Exception {
        URL[] urls = jcl.getJarURLs(new File(testPluginPath.getPath()), true);
        List<String> classes = new ArrayList<>();
        for (URL url : urls) {
            JarFile jarFile = new JarFile(url.getPath());        

            Enumeration<JarEntry> e = jarFile.entries();
            while (e.hasMoreElements()) {
                JarEntry entry = e.nextElement();
                Class c = jcl.getClassFromJarEntry(entry, false);
                if (c != null)
                    classes.add(c.getSimpleName());
            }
        }
        
        assertTrue(classes.contains("TestPlugin"));
        assertTrue(classes.contains("InvalidTestPlugin"));
        assertTrue(classes.size() == 2);
    }
    
    @Test
    public void JarClassLoaderGetJarClassesShouldReturnCorrectClassObjects() throws Exception {
        URL[] urls = jcl.getJarURLs(new File(testPluginPath.getPath()), true);
        List<String> classes = new ArrayList<>();
        for (URL url : urls) {
            List<Class> l = jcl.getJarClasses(url, false);
            classes.addAll(l.stream().map(c -> c.getSimpleName()).collect(Collectors.toList()));
        }
        
        assertTrue(classes.contains("TestPlugin"));
        assertTrue(classes.contains("InvalidTestPlugin"));
        assertTrue(classes.size() == 2);
    }
    
    @Test
    public void JarClassLoaderGetClassInstanceListShouldReturnCorrectClassInstances() throws Exception {
        List<Plugin> plugins = jcl.getClassInstanceList(Plugin.class);
        List<String> classes = plugins.stream().map(p -> p.getClass().getSimpleName()).collect(Collectors.toList());
        assertTrue(classes.contains("TestPlugin"));
        assertFalse(classes.contains("InvalidTestPlugin"));
        assertTrue(classes.size() == 1);
        
        Plugin plugin = plugins.get(0);
        plugin.setTitle("TestTitle");
        assertEquals(plugin.toString(), "TestTitle");
        
        List<Object> plugins2 = jcl.getClassInstanceList(Object.class);
        classes = plugins2.stream().map(p -> p.getClass().getSimpleName()).collect(Collectors.toList());
        assertTrue(classes.contains("TestPlugin"));
        assertTrue(classes.contains("InvalidTestPlugin"));
        assertTrue(classes.size() == 2);
    }
}
