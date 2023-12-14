package com.dots;

import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JFrame;

public class main extends JFrame {

    public main() {
        initUI();
    }

    private void initUI() {
        add(new Board());
        pack();

        Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

        setTitle("Dynamic Dot Grid Simulation");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setSize(r.width, r.height);
        setVisible(true);
        setUndecorated(true);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            JFrame ex = new main();
            ex.setVisible(true);
        });
    }
}
