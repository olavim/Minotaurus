

package com.github.tilastokeskus.invalidtestplugin;

/**
 * This class should not be loaded by JarClassLoader
 */
public class InvalidTestPlugin {
    
    public int doubleVal(int n) {
        return n*2;
    }

}
