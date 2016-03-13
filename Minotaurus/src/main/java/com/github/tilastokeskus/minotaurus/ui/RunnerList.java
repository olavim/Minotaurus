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
import com.github.tilastokeskus.minotaurus.plugin.PluginLoader;
import com.github.tilastokeskus.minotaurus.runner.Runner;
import com.github.tilastokeskus.minotaurus.ui.component.RectangleComponent;
import com.github.tilastokeskus.minotaurus.util.ArrayList;
import com.github.tilastokeskus.minotaurus.util.ColorFactory;
import com.github.tilastokeskus.minotaurus.util.Pair;
import net.miginfocom.swing.MigLayout;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import javax.swing.*;

public class RunnerList extends ComponentList<Runner> {
    
    private final GUI parent;
    private final ColorFactory runnerColorFactory;
    
    public RunnerList() {
        this(null);
    }
    
    public RunnerList(GUI parent) {
        this.parent = parent;
        this.runnerColorFactory = new ColorFactory();
        this.setSelectable(false);
        this.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
    }

    @Override
    protected Pair<Component, Runner> generateComponent(Runner runner) {
        RunnerPanel panel = new RunnerPanel(runner);        
        return new Pair(panel, runner);
    }
    
    @Override
    public void refresh() {
        for (ListElement le : this.listElements) {
            RunnerPanel p = (RunnerPanel) le.component;
            Runner r = p.chooser.getSelectedObject();
            le.element = r;
            int runnerNum = indexOf(new Pair(p, r)) + 1;
            p.runnerName.setText("Runner " + runnerNum);
        }
        
        super.refresh();
    }
    
    @Override
    public List<Runner> getObjects() {
        List<Runner> list = new ArrayList<>();
        for (ListElement le : this.listElements) {
            RunnerPanel rp = (RunnerPanel) le.component;
            Runner runner = rp.chooser.getSelectedObject();
            if (runner != null)
                runner = runner.clone();

            list.add(runner);
        }
        
        return list;
    }
    
    private class RunnerPanel extends JPanel {
        
        JLabel selectedRunnerLabel;
        JLabel runnerName;
        Color runnerColor;
        Chooser<Runner> chooser;
        
        public RunnerPanel(Runner runner) {
            super(new MigLayout("insets 0", "[]0[grow,fill]0[]0", "[]0"));
            runnerName = new JLabel("Runner 1");
            runnerName.setFont(RunnerList.this.getFont());
            runnerName.setForeground(RunnerList.this.getForeground());
            runnerColor = runnerColorFactory.getNextColor();
            
            List<Runner> runners = PluginLoader.loadPlugins(Runner.class);
            chooser = new Chooser<>(parent, runners);
            chooser.setSelectedObject(runner);
            chooser.addChangeListener(e -> {
                RunnerList.this.refresh();
            });
            
            this.addComponents();
        }
        
        private void addComponents() {
            JPanel westPanel = createWestPanel();            
            JPanel eastPanel = createEastPanel();
            
            URL url = ResourceManager.IMAGE_DELETE;
            JLabel removePlayerIcon = new JLabel(new ImageIcon(url));
            
            removePlayerIcon.addMouseListener(
                    new RemoveRunnerListener(RunnerList.this, this));
            
            removePlayerIcon.setCursor(
                    Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            
            this.add(westPanel, "growy");
            this.add(eastPanel, "grow");
            this.add(removePlayerIcon, "gap 10 10 2 0");
        }
        
        private JPanel createWestPanel() {
            JComponent colorRectangle = new RectangleComponent(
                    16, runnerColor, 1, runnerColor.darker());
            
            JPanel westPanel = new JPanel(new MigLayout("", "[grow, fill]0"));            
            westPanel.setBorder(BorderFactory.createMatteBorder(
                    0, 1, 0, 0, new Color(200, 200, 200)));            
            westPanel.setBackground(new Color(230, 230, 230));
            westPanel.add(colorRectangle, "west, grow, gap 10 0 6 0");
            westPanel.add(runnerName, "west, grow, gap 10 10 6 0");
            
            return westPanel;
        }
        
        private JPanel createEastPanel() {            
            JPanel eastPanel = new JPanel(
                    new MigLayout("insets 0", "[grow, fill]0"));
            eastPanel.add(chooser, "grow");
            eastPanel.setBorder(BorderFactory.createMatteBorder(
                    0, 0, 0, 1, new Color(200, 200, 200)));
            return eastPanel;
        }
        
        private class RemoveRunnerListener extends MouseAdapter {    
            private final RunnerList list;
            private final RunnerPanel panel;

            public RemoveRunnerListener(RunnerList list, RunnerPanel panel) {
                this.list = list;
                this.panel = panel;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (list.getObjects().size() > 1) {
                    Pair<Component, Runner> pair = new Pair(panel, panel.chooser.getSelectedObject());
                    list.removeComponent(pair);
                }
            }
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 79 * hash + Objects.hashCode(this.selectedRunnerLabel);
            hash = 79 * hash + Objects.hashCode(this.runnerName);
            hash = 79 * hash + Objects.hashCode(this.chooser.getSelectedObject());
            hash = 79 * hash + Objects.hashCode(this.runnerColor);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
                
            if (obj == null || getClass() != obj.getClass())
                return false;
                
            final RunnerPanel other = (RunnerPanel) obj;
            return (Objects.equals(this.selectedRunnerLabel, other.selectedRunnerLabel)
                    && Objects.equals(this.runnerName, other.runnerName)
                    && Objects.equals(this.chooser.getSelectedObject(), other.chooser.getSelectedObject())
                    && Objects.equals(this.runnerColor, other.runnerColor));
        }
        
    }

}