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

import com.github.tilastokeskus.minotaurus.plugin.Plugin;
import net.miginfocom.swing.MigLayout;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import javax.swing.*;

public class PluginChooser<T extends Plugin> extends JPanel {
    
    private final Window parent;
    private final Collection<T> plugins;
    private JLabel selectedPluginLabel;
    private T plugin;
    
    public PluginChooser(Window parent, Collection<T> plugins) {
        super(new MigLayout("insets 0", "[grow, fill]"));
        this.parent = parent;
        this.plugins = plugins;
        this.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        addComponents();
    }
    
    public void refresh() {
        this.selectedPluginLabel.setText(plugin.toString());
        this.parent.pack();
    }
    
    public T getPlugin() {
        return this.plugin;
    }
        
    private void addComponents() {
        JPanel westPanel = createWestPanel();            
        this.add(westPanel, "grow");
    }

    private JPanel createWestPanel() {
        Color pluginLabelColor = this.getForeground().brighter().brighter();
        this.selectedPluginLabel = new JLabel("No plugin");
        this.selectedPluginLabel.setFont(this.getFont());
        this.selectedPluginLabel.setForeground(pluginLabelColor);

        MenuShape menuShape = new MenuShape(10);
        menuShape.addMouseListener(new SelectPluginListener(
                    this.parent, this, plugin));
        
        menuShape.setCursor(Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR));

        JPanel westPanel = new JPanel(new MigLayout("", "[grow, fill]0"));

        westPanel.setBorder(BorderFactory.createMatteBorder(
                0, 1, 0, 0, new Color(200, 200, 200)));

        westPanel.setBackground(new Color(230, 230, 230));
        westPanel.add(this.selectedPluginLabel, "west, grow, gap 10 10 6 8");
        westPanel.add(menuShape, "east, grow, gap 20 20 10 8");

        return westPanel;
    }
    
    private class SelectPluginListener extends MouseAdapter {    
        private final Window parent;
        private final PluginChooser<T> chooser;

        public SelectPluginListener(Window parent,
                PluginChooser<T> chooser, T plugin) {
            this.parent = parent;
            this.chooser = chooser;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            ComponentList<T> componentList = new LabelList<>();
            componentList.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
            componentList.addObjects(plugins);

            ComponentListChooser<T> listChooser = new ComponentListChooser<>(parent, componentList);
            T chosenPlugin = listChooser.showDialog();

            if (chosenPlugin != null) {
                plugin = chosenPlugin;
                chooser.refresh();
            }
        }
    }

}