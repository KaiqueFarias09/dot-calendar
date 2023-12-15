package com.dots;

import javax.swing.*;
import java.awt.*;

/**
 * The main frame of the dynamic dot grid simulation.
 * Extends JFrame to create the application window.
 */
public class MainFrame extends JFrame {

    /**
     * Constructs a new MainFrame and initializes the user interface.
     */
    public MainFrame() {
        initUI();
    }

    /**
     * The entry point of the application. Creates an instance of MainFrame and sets it to be visible.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame ex = new MainFrame();
            ex.setVisible(true);
        });
    }

    /**
     * Initializes the user interface by adding a new Board to the frame and setting frame properties.
     */
    private void initUI() {
        add(new Board());
        pack();

        // Get the maximum window bounds for full screen
        Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

        setTitle("Dynamic Dot Grid Simulation");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setSize(r.width, r.height);
        setVisible(true);
    }
}
