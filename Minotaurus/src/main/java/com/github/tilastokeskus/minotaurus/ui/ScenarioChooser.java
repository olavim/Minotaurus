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

import com.github.tilastokeskus.minotaurus.ResourceManager;
import com.github.tilastokeskus.minotaurus.scenario.Scenario;
import com.github.tilastokeskus.minotaurus.scenario.Setting;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ScenarioChooser extends Chooser<Scenario> {
    
    JLabel settingsIcon;

    public ScenarioChooser(GUI parent, List<Scenario> objects) {
        super(parent, objects);
            
        settingsIcon = new JLabel(new ImageIcon(ResourceManager.IMAGE_SETTINGS));
        settingsIcon.setVisible(false);
        settingsIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        settingsIcon.addMouseListener(new SettingsListener());
        
        this.add(settingsIcon, "cell 2 1, east");
    }
    
    @Override
    public void refresh() {
        super.refresh();
        if (object != null && object.getModifiableSettings() != null) {                
            settingsIcon.setVisible(true);
        }
    }
    
    private class SettingsListener extends MouseAdapter {

        @Override
        public void mouseReleased(MouseEvent e) {
            Map<String, Setting> m = object.getModifiableSettings();
            ScenarioSettingsDialog diag = new ScenarioSettingsDialog(parent.getFrame(), m);
            boolean validSettings = diag.showDialog();
        }
        
    }

}
