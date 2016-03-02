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

package com.github.tilastokeskus.minotaurus.ui.button;

import com.github.tilastokeskus.minotaurus.util.ArrayList;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.JComponent;

public class Button extends JComponent {
    
    private Collection<ActionListener> actionListeners;
    private AbstractAction action;    
    private ButtonState state;
    
    protected String label;
    
    public Button(AbstractAction action) {
        this.addMouseListener(new ButtonListener(this));
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.actionListeners = new ArrayList<>();
        this.state = ButtonState.DEFAULT;
        this.action = action;
        this.label = (String) action.getValue(AbstractAction.NAME);
    }
    
    public Button(String label) {
        this.state = ButtonState.DEFAULT;
        this.label = label;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public void addActionListener(ActionListener listener) {
        this.actionListeners.add(listener);
    }
    
    public void notifyListeners(MouseEvent e) {
        ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, new String(), e.getWhen(), e.getModifiers());
        action.actionPerformed(evt);
        
        for (ActionListener listener : this.actionListeners) {
            ActionEvent event = new ActionEvent(e.getSource(), e.getID(), e.paramString());
            listener.actionPerformed(event);
        }
    }
    
    public void setState(ButtonState state) {
        this.state = state;
        repaint();
    }
    
    public ButtonState getState() {
        return this.state;
    }

}