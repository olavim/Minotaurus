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
import com.github.tilastokeskus.minotaurus.plugin.Plugin;
import com.github.tilastokeskus.minotaurus.plugin.PluginManager;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;

public class MainWindow extends AbstractGUI {
    
    private static final String WINDOW_NAME = "Minotaurus";
    private static final Color COLOR_BG = new Color(240, 240, 240);

    @Override
    public void run() {
        this.frame = new JFrame(WINDOW_NAME);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        addContents(this.frame.getContentPane());
        
        this.frame.pack();
        this.frame.setLocationByPlatform(true);
        this.frame.setVisible(true);
    }
    
    private void addContents(Container container) {
        container.setBackground(COLOR_BG);
        container.setLayout(new MigLayout("wrap 1", "[grow]", "[grow]"));
        Collection<MazeGenerator> mazeGenerators = PluginManager.getMazeGenerators();
        PluginChooser mazeGeneratorChooser = new PluginChooser(this.frame, mazeGenerators);
        container.add(mazeGeneratorChooser, "north, grow");
        
        JButton btnStart = new JButton(new StartAction("Start"));
        container.add(btnStart, "south");
    }
    
    private class StartAction extends AbstractAction {

        public StartAction(String text) {
            super(text);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            
        }
        
    }

}
