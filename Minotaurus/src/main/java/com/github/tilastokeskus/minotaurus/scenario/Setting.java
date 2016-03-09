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

package com.github.tilastokeskus.minotaurus.scenario;

import java.util.Observable;
import java.util.function.Function;
import java.util.function.Predicate;

public class Setting<T> extends Observable {
    
    Class<T> clazz;
    String description;
    Predicate<T> predicate;
    Function<String, T> function;
    T value;
    
    /**
     * Creates a new setting with the provided type, description and predicate.
     * 
     * @param c Class of the setting's type.
     * @param desc A short description for the setting.
     * @param p A predicate that returns true for an object when it is valid for
     *          this setting, and false otherwise.
     * @param f A function generating a valid instance of {@code T} from a string.
     */
    public Setting(Class<T> c, String desc, Predicate<T> p, Function<String, T> f) {
        this.clazz = c;
        this.description = desc;
        this.predicate = p;
        this.function = f;
        this.value = null;
    }
    
    /**
     * Returns the type of this setting's value.
     * 
     * @return A class object.
     */
    public Class<T> getType() {
        return clazz;
    }
    
    /**
     * Returns a short description for the setting.
     * 
     * @return A String.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Returns whether or not a value is an accepted value for this setting.
     * 
     * @param value Value to evaluate.
     * @return True if the value is allowed, false otherwise.
     */
    public boolean isValueAllowed(String value) {
        try {
            T v = function.apply(value);
            return predicate.test(v);
        } catch (Exception ex) {
            return false;
        }
    }
    
    /**
     * Returns whether or not this setting's current value is a valid value for
     * this setting.
     * 
     * @return True if the value is allowed, false otherwise.
     */
    public boolean isValueAllowed() {
        try {
            return predicate.test(value);
        } catch (Exception ex) {
            return false;
        }
    }
    
    /**
     * Sets this setting's value to {@code v}. Notifies observers that a change
     * occurred.
     * 
     * @param v New value of the setting.
     */
    public void setValue(T v) {
        this.value = v;
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Sets this setting's value to {@code v}. If the value was changed,
     * notifies observers that a change occurred.
     * 
     * @param v New value of the setting.
     * @return True if the value was changed, false otherwise.
     */
    public boolean setValue(String v) {
        try {
            setValue(function.apply(v));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    
    /**
     * Returns the current value of this setting.
     * 
     * @return An object.
     */
    public T getValue() {
        return value;
    }
    
}
