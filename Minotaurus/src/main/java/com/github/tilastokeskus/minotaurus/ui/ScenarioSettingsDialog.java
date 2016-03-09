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

import com.github.tilastokeskus.minotaurus.scenario.Setting;
import com.github.tilastokeskus.minotaurus.util.HashMap;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

public class ScenarioSettingsDialog extends JDialog {

    private final Frame parent;
    private final Map<String, Setting> settings;
    private final Map<String, SettingComponent> components;
    
    private boolean settingsAreValid;
    
    public ScenarioSettingsDialog(Frame parent, Map<String, Setting> settings) {
        super(parent, "Scenario Settings", JDialog.DEFAULT_MODALITY_TYPE);
        this.parent = parent;
        this.settings = settings;
        this.components = new HashMap<>();
        
        settingsAreValid = validateSettings(settings);
        
        addContents((JComponent) this.getContentPane());
    }
    
    public boolean showDialog() {
        this.pack();
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
            
        return settingsAreValid;
    }
    
    private void addContents(JComponent container) {
        container.setLayout(new MigLayout("wrap 1", "[grow, fill]"));
        container.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        Set<Entry<String, Setting>> entries = settings.entrySet();
        for (Entry<String, Setting> entry : entries) {
            JLabel label = new JLabel(entry.getValue().getDescription());
            label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
            Setting setting = entry.getValue();
            SettingComponent component = getComponentForType(setting.getType());
            component.setValue(setting.getValue());
            components.put(entry.getKey(), component);
            
            this.add(label);
            this.add((Component) component, "w 100");
        }
        
        JButton okButton = new JButton(new AcceptAction());
        this.add(okButton, "gap 0 0 10 0, south");
    }
    
    private boolean validateSettings(Map<String, Setting> settings) {
        
        // Do all settings have a valid value?
        return settings.values().stream()
                .allMatch(Setting::isValueAllowed);
    }
    
    private class AcceptAction extends AbstractAction {
        
        public AcceptAction() {
            super("Accept");
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            for (Entry<String, SettingComponent> entry : components.entrySet()) {
                Setting setting = settings.get(entry.getKey());
                String value = (String) entry.getValue().getValue();
                
                if (!setting.isValueAllowed(value)) {
                    JOptionPane.showMessageDialog(parent,
                            "Bad value for field: " + entry.getKey(),
                            "Bad Setting",
                            JOptionPane.ERROR_MESSAGE);
                    
                    settingsAreValid = false;
                    return;
                }
                
                setting.setValue(value);
            }
            
            settingsAreValid = true;
            ScenarioSettingsDialog.this.dispose();
        }    
    }
    
    private SettingComponent getComponentForType(Class<?> type) {
        if (type == Integer.class) 
                return new SettingTextField();
        
        return null;
    }
    
    private interface SettingComponent {
        Object getValue();
        void setValue(Object o);
    }
    
    private class SettingTextField extends JTextField implements SettingComponent {
        @Override
        public String getValue() {
            return this.getText();
        }        

        @Override
        public void setValue(Object o) {
            this.setText(o.toString());
        }
    }
    
}
