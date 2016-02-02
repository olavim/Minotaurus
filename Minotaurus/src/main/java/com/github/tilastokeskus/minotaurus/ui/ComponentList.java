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
import java.util.Collection;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;

/**
 * Provides basic functionality to lists of visual elements of different kinds.
 * 
 * @param <T>  The type of the elements the list should contain.
 */
public abstract class ComponentList <T> extends JComponent {
    
    private List<ListElement> listElements;
    private List<T> objects;
    private T selected;
    private boolean selectable;
    
    public ComponentList() {
        this(new ArrayList<>());
    }
    
    public ComponentList(Collection<? extends T> objects) {
        this.setLayout(new MigLayout("insets 0, wrap 1", "[grow]0", "[grow]0"));
        this.listElements = new ArrayList<>();
        this.objects = new ArrayList<>(objects);
        this.selected = null;
        this.selectable = true;
    }
    
    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }
    
    public List<T> getObjects() {
        return this.objects;
    }
    
    public T getSelected() {
        return this.selected;
    }
    
    public void setSelected(T selected) {
        this.selected = selected;
    }
    
    public void removeObject(T obj) {
        this.objects.remove(obj);
        this.refresh();
    }
    
    public void addObject(T obj) {
        this.objects.add(obj);
        this.refresh();
    }
    
    public void addObjects(Collection<? extends T> elements) {
        this.objects.addAll(elements);
        this.refresh();
    }
    
    /**
     * Removes all elements from the list and adds them back. This way all
     * new components and changes in them will be redrawn and visible.
     *
     * refresh() should be called after any and all content changes. refresh()
     * is called, by default, automatically by the methods removeObject(),
     * addObject() and addObjects().
     */
    public final void refresh() {
        this.removeAll();
        this.setSelected(null);
        
        this.addComponents(this.generateComponents());
        this.revalidate();
        this.repaint();
    }
    
    public final void addComponents(Iterable<Pair<Component, T>> components) {
        for (Pair<Component, T> component : components) {            
            ListElement element = new ListElement(component);
            this.listElements.add(element);
            this.add(element, "grow");
        }
    }
    
    /**
     * Generates the pairs of components and elements this list should consist of.
     * 
     * @return  {@link java.lang.Iterable Iterable} object containing pairs of
     * Component and T, where Component is something that should be visible in
     * the list, and T being the object the Component represents. For example,
     * the code
     * 
     * <pre>
     * {@code
     *      Iterable<Pair<Component, T>> generateComponents() {
     *          List<Pair<Component, T>> components = new ArrayList<>();
     *          for (T element : this.getObjects()) {
     *              JLabel label = new JLabel(element.toString());
     *              components.add(new Pair(label, element));
     *          }
     * 
     *          return components;
     *      }
     *  }
     * </pre>
     * 
     * generates a list of elements that are represented as JLabels containing
     * what ever the element's {@link #toString() toString} method returns.
     * 
     */
    abstract protected Iterable<Pair<Component, T>> generateComponents();
    
    private class ListElement extends JPanel {

        private final T element;

        private ListElement(Pair<Component, T> component) {
            super(new MigLayout("insets 0", "[grow]"));
            this.element = component.second;

            this.add(component.first, "grow");
            this.addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    if (selectable) {
                        setBackground(new Color(220, 215, 210));
                        setSelected(element);

                        for (ListElement listElement : listElements) {
                            listElement.getMouseListeners()[0].mouseExited(e);
                        }
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (selectable) {
                        if (selected != element) {
                            setBackground(new Color(225, 220, 220));
                        }
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (selectable) {
                        if (selected != element) {
                            setBackground(UIManager.getColor("Panel.background"));
                        }
                    }
                }

            });
        }

    }
    
}