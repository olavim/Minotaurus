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

package com.github.tilastokeskus.minotaurus.ui;

import com.github.tilastokeskus.minotaurus.util.Pair;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;

/**
 * A customizable visual list of objects.
 * 
 * @param <T>  Type of elements the list contains.
 */
public abstract class ComponentList<T> extends JComponent {
    
    final List<ListElement> listElements;
    final List<T> objects;
    T selected;
    boolean selectable;
    
    public ComponentList() {
        this.setLayout(new MigLayout("insets 0, wrap 1", "[grow, fill]0", "[grow]0"));
        this.listElements = new ArrayList<>();
        this.objects = new ArrayList<>();
        this.selected = null;
        this.selectable = true;
    }
    
    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }
    
    /**
     * Returns a the list of objects in this list.
     * 
     * @return A list of objects.
     */
    public List<T> getObjects() {
        return new ArrayList<>(objects);
    }
    
    /**
     * Returns the currently selected object.
     * 
     * @return An object.
     */
    public T getSelected() {
        return selected;
    }
    
    /**
     * Sets the currently selected object to the specified one. The specified
     * object doesn't have to be contained in this list.
     * 
     * @param selected Object to mark as the currently selected object.
     */
    public void setSelected(T selected) {
        this.selected = selected;
    }
    
    /**
     * Removes a component matching the specified pair. Also removes the object
     * from this list specified by the pair.
     * 
     * @param pair A Component - {@code T} pair.
     */
    public void removeComponent(Pair<Component, T> pair) {
        ListElement le = new ListElement(pair, false);
        int index = Arrays.asList(this.getComponents()).indexOf(le);
        remove(index);
        objects.remove(pair.second);
        refresh();
    }
    
    /**
     * Adds an object to this list and generates a component for it, which will
     * be added to this panel.
     * 
     * @param obj Object to add.
     * @return A Component - {@code T} pair that was generated from the object.
     */
    public Pair<Component, T> addObject(T obj) {
        objects.add(obj);
        Pair<Component, T> pair = generateComponent(obj);
        ListElement listElement = new ListElement(pair, true);
        listElements.add(listElement);
        this.add(listElement);
        
        refresh();
        return pair;
    }
    
    /**
     * Adds objects to this list and generates components for for them, which
     * will be added to this panel.
     * 
     * @param objects Objects to add.
     * @return A list of Component - {@code T} pairs that were generated from
     *         the objects.
     */
    public List<Pair<Component, T>> addObjects(List<T> objects) {
        objects.addAll(objects);
        List<Pair<Component, T>> pairs = new ArrayList<>();
        for (T obj : objects)
            pairs.add(addObject(obj));
        refresh();
        return pairs;
    }
    
    /**
     * Refreshes the changes in this panel to be visible.
     */
    public void refresh() {
        this.revalidate();
        this.repaint();
    }
    
    /**
     * Returns the index of the Component - {@code T} pair in this list.
     * 
     * @param pair A Component - {@code T} pair.
     * @return The index of the pair.
     */
    public int indexOf(Pair<Component, T> pair) {
        ListElement e = new ListElement(pair, false);
        return listElements.indexOf(e);
    }
    
    /**
     * Generates a component/element pair for an element.
     * 
     * @param element Element to generate component for.
     * @return A pair, first element being a component meant as a visual
     * representation of an element, and the second being the element itself.
     */
    abstract protected Pair<Component, T> generateComponent(T element);
    
    class ListElement extends JPanel {

        final T element;
        final Component component;
        
        private ListElement(Pair<Component, T> pair) {
            this(pair, true);
        }

        private ListElement(Pair<Component, T> pair, boolean visible) {
            super(new MigLayout("insets 0", "[grow]"));
            this.component = pair.first;
            this.element = pair.second;

            if (visible) {
                this.add(component, "grow");
                this.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (selectable) {
                            setBackground(new Color(220, 215, 210));
                            setSelected(element);

                            for (ListElement listElement : listElements)
                                listElement.getMouseListeners()[0].mouseExited(e);
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (selectable && selected != element)
                            setBackground(new Color(225, 220, 220));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (selectable && selected != element)
                            setBackground(UIManager.getColor("Panel.background"));
                    }
                });
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
                
            if (obj == null || getClass() != obj.getClass())
                return false;
                
            final ListElement other = (ListElement) obj;
                
            return Objects.equals(this.component, other.component)
                    && Objects.equals(this.element, other.element);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 79 * hash + Objects.hashCode(this.element);
            hash = 79 * hash + Objects.hashCode(this.component);
            return hash;
        }

    }
    
}