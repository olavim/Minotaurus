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
import com.github.tilastokeskus.minotaurus.ui.component.RectangleComponent;
import com.github.tilastokeskus.minotaurus.util.HashMap;
import java.awt.Color;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

public class ScorePanel extends JPanel {

    private final Map<Runner, JLabel> scores;
    
    public ScorePanel(List<Runner> runners) {
        super(new MigLayout("wrap 3", "[grow]"));
        scores = new HashMap<>();
        
        for (Runner r : runners) {
            JLabel l = new JLabel("0");
            scores.put(r, l);
            
            Color runnerColor = r.getComponentColor();
            RectangleComponent c = new RectangleComponent(16,
                    runnerColor, 1, runnerColor.darker());
            
            add(c);
            add(new JLabel(r.toString()));
            add(l);
        }
    }
    
    public void setScore(Runner r, int score) {
        if (scores.containsKey(r))
            scores.get(r).setText("" + score);
    }
    
}
