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

import com.github.tilastokeskus.minotaurus.maze.MazeGenerator;
import com.github.tilastokeskus.minotaurus.plugin.PluginLoader;
import com.github.tilastokeskus.minotaurus.runner.Runner;
import com.github.tilastokeskus.minotaurus.scenario.Scenario;
import com.github.tilastokeskus.minotaurus.ui.button.LabelButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

public class MainWindow extends AbstractGUI {
    
    private static final String WINDOW_NAME = "Minotaurus";
    private static final Color COLOR_BG = new Color(240, 240, 240);
    private static final Border PANEL_PADDING = new EmptyBorder(10, 10, 10, 10);
    private static final Border PANEL_BORDER =  BorderFactory.createMatteBorder(
            1, 1, 1, 1, new Color(200, 200, 200));
    
    private PluginChooser<MazeGenerator> mazeGeneratorChooser;
    private PluginChooser<Scenario> scenarioChooser;
    private ComponentList<Runner> runnerList;

    @Override
    public void run() {
        this.frame = new JFrame(WINDOW_NAME);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        addContents((JPanel) this.frame.getContentPane());
        
        this.frame.pack();
        this.frame.setLocationByPlatform(true);
        this.frame.setVisible(true);
    }
    
    private void addContents(JPanel container) {
        container.setBackground(COLOR_BG);
        container.setLayout(new MigLayout("wrap 1, insets 0", "[grow]", "[grow]0"));
        
        JPanel pluginPanel = new JPanel(new MigLayout("wrap 1, insets 0", "[grow]", "[]10"));
        
        List<MazeGenerator> mazeGenerators = PluginLoader.loadPlugins(MazeGenerator.class);
        List<Scenario> scenarios = PluginLoader.loadPlugins(Scenario.class);
        mazeGeneratorChooser = new PluginChooser<>(this, mazeGenerators);
        scenarioChooser = new PluginChooser<>(this, scenarios);
        
        JPanel mazeGenPanel = createPluginChooserPanel("Maze Generator", mazeGeneratorChooser);
        JPanel scenarioPanel = createPluginChooserPanel("Scenario", scenarioChooser);
        
        pluginPanel.add(mazeGenPanel, "grow");
        pluginPanel.add(scenarioPanel, "grow");
        pluginPanel.setBorder(PANEL_PADDING);
        
        JPanel buttonPanel = new JPanel(new MigLayout("", "[grow]"));
        JButton btnStart = new JButton(new StartAction("Start"));
        buttonPanel.setBorder(PANEL_PADDING);
        buttonPanel.add(btnStart, "south");
        
        JPanel runnerListPanel = new JPanel(
                new MigLayout("wrap 1, insets 0", "[grow]0", "[]0"));
        LabelButton addRunnerBtn = new LabelButton(
                new AddRunnerAction("Add Runner"), true);
        addRunnerBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        
        runnerList = new RunnerList(this);
        runnerList.addObject(null);
        runnerList.setBorder(BorderFactory.createCompoundBorder(PANEL_PADDING,
                                                                PANEL_BORDER));
        
        runnerListPanel.add(addRunnerBtn, "right, gapx 0 10");
        runnerListPanel.add(runnerList, "grow");
        
        container.add(pluginPanel, "north");
        container.add(runnerListPanel, "north");
        container.add(buttonPanel, "south");
    }
    
    private JPanel createPluginChooserPanel(String labelStr, PluginChooser chooser) {
        Font labelFont = new Font(Font.SANS_SERIF, Font.BOLD, 10);        
        
        JPanel northPanel = new JPanel(new MigLayout("insets 1", "[]"));
        JLabel label = new JLabel(labelStr);
        label.setFont(labelFont);
        label.setForeground(new Color(200, 200, 200));
        northPanel.setBackground(new Color(80, 80, 80));   
        northPanel.add(label, "gap 2 4 0 0");
        
        JPanel southPanel = new JPanel(new MigLayout("", "[grow, fill]"));
        southPanel.setBackground(new Color(220, 220, 220));
        southPanel.add(chooser, "west, grow, w 100%");
        southPanel.setBorder(PANEL_BORDER);
        
        JPanel pluginPanel = new JPanel(new MigLayout("insets 0", "[grow]"));
        pluginPanel.add(northPanel, "");
        pluginPanel.add(southPanel, "south, grow");
        
        return pluginPanel;
    }
    
    private class AddRunnerAction extends AbstractAction {
        public AddRunnerAction(String text) {
            super(text);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            runnerList.addObject(null);
            refresh();
        }
    }
    
    private class StartAction extends AbstractAction {
        public StartAction(String text) {
            super(text);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            MazeGenerator gen = mazeGeneratorChooser.getPlugin();
            //Main.startSimulation(gen);
        }        
    }

}
