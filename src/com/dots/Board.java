package com.dots;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 900;
    private final int B_HEIGHT = 900;
    private final int DOT_SIZE = 10;
    private final int DELAY = 1000;
    private final int MOVE_AMOUNT = 10;

    private int numberOfHorizontalMovements = 0;
    private int numberOfVerticalMovements = 0;

    private final Random random = new Random();
    private Dot[] dots = new Dot[0];

    private boolean inGame = true;

    private int outlinedCircleX;
    private int outlinedCircleY;

    private Timer timer;

    public Board() {
        initBoard();
        outlinedCircleX = generateRandomPosition();
        outlinedCircleY = generateRandomPosition();
    }

    private void initBoard() {
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        initGame();
    }

    private void initGame() {
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private int generateRandomPosition() {
        int min = 20;
        int max = 891;
        return random.nextInt(max - min) + min;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        if (inGame) {
            if (dots.length == 0) {
                drawOutlinedCircle(g);
            }

            for (int z = 0; z < dots.length; z++) {
                if (z == 0) {
                    drawOutlinedCircle(g);
                } else {

                    g.fillOval(outlinedCircleX + z * 10, outlinedCircleY + z * 10, DOT_SIZE,
                            DOT_SIZE);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        }
    }

    private void drawOutlinedCircle(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawOval(outlinedCircleX, outlinedCircleY, DOT_SIZE, DOT_SIZE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {

            if (numberOfHorizontalMovements < 2) {
                outlinedCircleX += MOVE_AMOUNT;
                numberOfHorizontalMovements++;
            } else if (numberOfVerticalMovements < 2) {
                outlinedCircleY += MOVE_AMOUNT;
                numberOfVerticalMovements++;
            } else {
                numberOfHorizontalMovements = 1;
                numberOfVerticalMovements = 0;
                outlinedCircleX += MOVE_AMOUNT;
            }

            if (outlinedCircleX + DOT_SIZE > B_WIDTH || outlinedCircleY + DOT_SIZE > B_HEIGHT) {
                outlinedCircleX = generateRandomPosition();
                outlinedCircleY = generateRandomPosition();
            } else {
                // getGraphics().fillOval(x[outlinedCircleX + 10], y[outlinedCircleY + 10],
                // DOT_SIZE, DOT_SIZE);
            }

            repaint();
        }
    }

}
