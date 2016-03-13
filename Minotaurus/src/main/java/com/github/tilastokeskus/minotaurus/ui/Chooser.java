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

import com.github.tilastokeskus.minotaurus.util.HashSet;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import net.miginfocom.swing.MigLayout;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Chooser<T> extends JPanel {
    
    final GUI parent;
    final List<T> objects;
    final Set<ChangeListener> changeListeners;
    JLabel selectedObjectLabel;
    T object;
    
    public Chooser(GUI parent, List<T> objects) {
        super(new MigLayout("insets 0", "[grow, fill]0"));
        this.parent = parent;
        this.objects = objects;
        this.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        this.changeListeners = new HashSet<>();
        addComponents();
    }
    
    public void refresh() {
        if (object != null)
            this.selectedObjectLabel.setText(object.toString());
        this.parent.refresh();
    }
    
    public T getSelectedObject() {
        return this.object;
    }
    
    public void setSelectedObject(T object) {
        this.object = object;
        refresh();
    }
    
    public void addChangeListener(ChangeListener listener) {
        changeListeners.add(listener);
    }
    
    public void removeChangeListener(ChangeListener listener) {
        changeListeners.remove(listener);
    }
    
    protected void notifyChangeListeners() {
        for (ChangeListener listener : changeListeners) {
            listener.stateChanged(new ChangeEvent(this));
        }
    }
        
    private void addComponents() {
        Color objectLabelColor = this.getForeground().brighter().brighter();
        this.selectedObjectLabel = new JLabel("No selection");
        this.selectedObjectLabel.setFont(this.getFont());
        this.selectedObjectLabel.setForeground(objectLabelColor);

        MenuShape menuShape = new MenuShape(10);
        menuShape.addMouseListener(new SelectObjectListener(
                    parent.getFrame(), this, object));
        
        menuShape.setCursor(Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR));

        this.setBorder(BorderFactory.createMatteBorder(
                0, 1, 0, 0, new Color(200, 200, 200)));

        this.setBackground(new Color(230, 230, 230));
        this.add(this.selectedObjectLabel, "west, gap 10 10 6 8");
        this.add(menuShape, "east, gap 20 20 10 8");
    }
    
    private class SelectObjectListener extends MouseAdapter {    
        private final Window parent;
        private final Chooser<T> chooser;

        public SelectObjectListener(Window parent,
                Chooser<T> chooser, T object) {
            this.parent = parent;
            this.chooser = chooser;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            ComponentList<T> componentList = new LabelList<>();
            componentList.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
            componentList.addObjects(objects);

            ComponentListChooser<T> listChooser = new ComponentListChooser<>(parent, componentList);
            T chosenPlugin = listChooser.showDialog();

            if (chosenPlugin != null) {
                object = chosenPlugin;
                chooser.refresh();
                notifyChangeListeners();
            }
        }
    }

}