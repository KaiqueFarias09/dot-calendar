package com.dots;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        initUI();
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            JFrame ex = new MainFrame();
            ex.setVisible(true);
        });
    }

    private void initUI() {
        add(new Board());
        pack();

        Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

        setTitle("Dynamic Dot Grid Simulation");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setSize(r.width, r.height);
        setVisible(true);
    }
}
