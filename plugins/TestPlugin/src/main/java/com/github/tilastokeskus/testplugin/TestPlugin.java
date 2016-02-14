
package com.github.tilastokeskus.testplugin;

import com.github.tilastokeskus.minotaurus.plugin.Plugin;

/**
 * This class should be loaded by JarClassLoader
 */
public class TestPlugin implements Plugin {

    private String title;
    
    @Override
    public void setTitle(String title) {
        this.title = title;
    }
    
    @Override
    public String toString() {
        return this.title;
    }
    
    public int doubleVal(int n) {
        return n*2;
    }

}
