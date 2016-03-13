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

import com.github.tilastokeskus.minotaurus.runner.Runner;
import com.github.tilastokeskus.minotaurus.simulation.SimulationHandler;
import java.awt.Container;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;

public class SimulationWindow extends AbstractGUI implements Observer {
    
    private static final String WINDOW_NAME = "Minotaurus Simulation";

    private final SimulationHandler sHandler;
    private final MazePanel mazePanel;
    private final ScorePanel scorePanel;
    
    public SimulationWindow(SimulationHandler sHandler) {
        this.sHandler = sHandler;
        this.mazePanel = new MazePanel(sHandler.getMaze());
        this.scorePanel = new ScorePanel(sHandler.getRunners());
        this.frame = new JFrame(WINDOW_NAME);        
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void run() {
        this.addContents(this.frame.getContentPane());
        
        this.frame.pack();
        this.frame.setLocationByPlatform(true);
        this.frame.setVisible(true);
    }

    private void addContents(Container container) {
        container.setLayout(new MigLayout("wrap 1", "[grow]", ""));
        container.add(this.mazePanel);
        container.add(this.scorePanel);
    }
    
    @Override
    public void update(Observable o, Object arg) {
        if (this.frame != null) {
            this.frame.repaint();
            this.mazePanel.repaint();
            
            for (Runner r : sHandler.getRunners()) {
                int score = sHandler.getScenario().getScore(r);
                this.scorePanel.setScore(r, score);
            }
        }
    }
    
}
