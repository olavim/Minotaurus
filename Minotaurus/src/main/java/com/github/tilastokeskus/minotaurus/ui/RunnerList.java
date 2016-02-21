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

import com.github.tilastokeskus.minotaurus.plugin.PluginLoader;
import com.github.tilastokeskus.minotaurus.runner.Runner;
import com.github.tilastokeskus.minotaurus.ui.component.RectangleComponent;
import com.github.tilastokeskus.minotaurus.util.ColorFactory;
import com.github.tilastokeskus.minotaurus.util.Pair;
import net.miginfocom.swing.MigLayout;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class RunnerList extends LabelList<Runner> {
    
    private final Window parent;
    
    public RunnerList(Window parent) {
        this.parent = parent;
        this.setSelectable(false);
        this.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
    }

    @Override
    protected Iterable<Pair<Component, Runner>> generateComponents() {
        List<Pair<Component, Runner>> components = new ArrayList<>();
        
        for (int i = 0; i < this.getObjects().size(); i++) {
            Runner runner = this.getObjects().get(i);
            RunnerPanel panel = new RunnerPanel(runner, i+1);
            components.add(new Pair(panel, runner));
        }
        
        return components;
    }
    
    private class RunnerPanel extends JPanel {
        
        JLabel selectedRunnerLabel;
        Runner runner;
        int runnerNum;
        Color runnerColor;
        
        private RunnerPanel(Runner runner, int runnerNum) {
            super(new MigLayout("insets 0", "[grow, fill]"));
            this.runner = runner;
            this.runnerNum = runnerNum;
            this.runnerColor = ColorFactory.getNextColor();
            this.addComponents();
        }
        
        private void addComponents() {
            JPanel westPanel = createWestPanel();            
            JPanel eastPanel = createEastPanel();
            
            URL url = this.getClass().getResource("/images/delete.png");
            JLabel removePlayerIcon = new JLabel(new ImageIcon(url));
            
            removePlayerIcon.addMouseListener(
                    new RemoveRunnerListener(RunnerList.this, runner));
            
            removePlayerIcon.setCursor(
                    Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            
            this.add(westPanel, "west, grow");
            this.add(eastPanel, "west, grow, w 100%");
            this.add(removePlayerIcon, "east, grow, gap 10 10 2 0");
        }
        
        private JPanel createWestPanel() {
            JComponent colorRectangle = new RectangleComponent(
                    16, runnerColor, 1, runnerColor.darker());
            
            JLabel runnerName = new JLabel("Runner " + runnerNum);
            runnerName.setFont(RunnerList.this.getFont());
            runnerName.setForeground(RunnerList.this.getForeground());
            
            JPanel westPanel = new JPanel(new MigLayout("", "[grow, fill]0"));
            
            westPanel.setBorder(BorderFactory.createMatteBorder(
                    0, 1, 0, 0, new Color(200, 200, 200)));
            
            westPanel.setBackground(new Color(230, 230, 230));
            westPanel.add(colorRectangle, "west, grow, gap 10 0 6 0");
            westPanel.add(runnerName, "west, grow, gap 10 10 6 0");
            
            return westPanel;
        }
        
        private JPanel createEastPanel() {
            Color runnerLabelColor = RunnerList.this.getForeground().brighter().brighter();
            String text = "No plugin";
            if (runner != null) {
                text = runner.toString();
                runnerLabelColor = RunnerList.this.getForeground().brighter();
            }
            
            this.selectedRunnerLabel = new JLabel(text);
            this.selectedRunnerLabel.setFont(RunnerList.this.getFont());
            this.selectedRunnerLabel.setForeground(runnerLabelColor);
            
            MenuShape menuShape = new MenuShape(10);            
            menuShape.addMouseListener(new SelectRunnerListener());            
            menuShape.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            
            JPanel eastPanel = new JPanel(new MigLayout("", "[grow, fill]0"));            
            eastPanel.setBorder(BorderFactory.createMatteBorder(
                    0, 1, 0, 1, new Color(200, 200, 200)));            
            eastPanel.setBackground(new Color(220, 220, 220));
            eastPanel.add(this.selectedRunnerLabel, "dock center, grow, gap 12 0 6 6");
            eastPanel.add(menuShape, "east, grow, gap 20 10 10 0");
            
            return eastPanel;
        }
        
        private class RemoveRunnerListener extends MouseAdapter {    
            private final RunnerList list;
            private final Runner runner;

            public RemoveRunnerListener(RunnerList list, Runner runner) {
                this.list = list;
                this.runner = runner;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (list.getObjects().size() > 1) {
                    list.removeObject(runner);
                }
            }
        }
        
        private class SelectRunnerListener extends MouseAdapter {
            @Override
            public void mouseReleased(MouseEvent e) {
                ComponentList componentList = new LabelList();
                componentList.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
                
                List<Runner> runners = PluginLoader.loadPlugins(Runner.class);
                if (runners != null)
                    componentList.addObjects(runners);

                ComponentListChooser chooser = new ComponentListChooser<>(
                        RunnerList.this.parent, componentList);
                Runner runner = (Runner) chooser.showDialog();

                if (runner != null) {
                    runner.setComponentColor(runnerColor);
                    RunnerPanel.this.runner = (Runner) runner.clone();
                    RunnerList.this.refresh();
                }
            }
        }
        
    }

}