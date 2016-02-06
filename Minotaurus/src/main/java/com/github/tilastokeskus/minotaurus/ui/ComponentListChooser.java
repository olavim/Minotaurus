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

import com.github.tilastokeskus.minotaurus.ui.button.LabelButton;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import net.miginfocom.swing.MigLayout;

public class ComponentListChooser<T> extends JDialog {
    
    private Window parent;
    private ComponentList<T> componentList;
    private T selected;
    
    private ComponentListChooser() {
        throw new UnsupportedOperationException();
    }
    
    public ComponentListChooser(ComponentList<T> list) {
        this(null, list);
    }
    
    public ComponentListChooser(Window parent, ComponentList<T> list) {
        super(parent, "Chooser", Dialog.DEFAULT_MODALITY_TYPE);
        
        this.componentList = list;
        this.parent = parent;
        this.selected = null;
        
        addContents(this.getContentPane());
    }
    
    public T showDialog() {
        this.pack();
        this.setLocationRelativeTo(parent);
        this.setVisible(true);        
        return selected;
    }
    
    private void addContents(Container container) {
        container.setLayout(new MigLayout("", "[grow]", "[grow]"));
        
        SelectAction selectAction = new SelectAction("Select");        
        LabelButton selectButton = new LabelButton(selectAction, true);
        
        container.add(componentList, "north, width 220, span");
        container.add(new PlainSeparator(true), "north, growx, span");
        container.add(selectButton, "gaptop 10, gapbottom 10, center, grow, span");
    }

    private class SelectAction extends AbstractAction {
        public SelectAction(String name) {
            super(name);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            selected = componentList.getSelected();
            dispose();
        }            
    };
    
}