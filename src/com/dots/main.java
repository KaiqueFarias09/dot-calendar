package com.dots;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class main extends JFrame {

    public main() {
        initUI();
    }
    
    private void initUI() {
        add(new Board());
               
        setResizable(false);
        pack();
        
        setTitle("Dynamic Dot Grid Simulation");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    

    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            JFrame ex = new main();
            ex.setVisible(true);
        });
    }
}
