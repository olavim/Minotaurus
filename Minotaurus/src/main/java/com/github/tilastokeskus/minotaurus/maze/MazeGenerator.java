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

package com.github.tilastokeskus.minotaurus.maze;

import com.github.tilastokeskus.minotaurus.plugin.Plugin;
import com.github.tilastokeskus.minotaurus.ui.MazePanel;
import com.github.tilastokeskus.minotaurus.ui.SimulationWindow;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;

public interface MazeGenerator extends Plugin {
    
    public final static Logger LOGGER = Logger.getLogger(MazeGenerator.class.getName());
    
    /**
     * Creates an instance of {@code clazz} and generates a maze with it.
     * A window will be opened showcasing the generated maze.
     * 
     * @param clazz Class object to create an instance of.
     * @param width Width of the maze to generate.
     * @param height Height of the maze to generate.
     */
    public static void testGenerator(Class<? extends MazeGenerator> clazz,
            int width, int height) {
        try {
            MazeGenerator gen = clazz.newInstance();
            Maze maze = gen.generateMaze(width, height);
            JFrame f = new JFrame("Maze");
            f.getContentPane().setLayout(new MigLayout("", "[grow]", "[grow]"));
            f.getContentPane().add(new MazePanel(maze));
            f.pack();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
        } catch (InstantiationException | IllegalAccessException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Generates a Maze with the given width and height.
     * 
     * @param width     Width of the maze to generate.
     * @param height    Height of the maze to generate.
     * @return          A Maze object.
     * @see Maze
     */
    Maze generateMaze(int width, int height);
}
