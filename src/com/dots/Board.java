package com.dots;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board extends JPanel implements ActionListener {

    private static final int DOT_SIZE = 10;
    private static final int BOARD_WIDTH = 900 - DOT_SIZE;
    private static final int BOARD_HEIGHT = 900 - DOT_SIZE;
    private static final boolean SHOULD_GENERATE_DOTS = true;
    private final Random random = new Random();
    private final List<Dot> dots = new ArrayList<>();
    private int day = 0;

    private int mainCircleXCoordinate;
    private int mainCircleYCoordinate;

    public Board() {
        initBoard();

        mainCircleXCoordinate = 800;
        mainCircleYCoordinate = 800;
    }

    private void initBoard() {
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        initGame();
    }

    private void initGame() {
        Timer timer = new Timer(1000, this);
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

        if (SHOULD_GENERATE_DOTS) {
            drawMainCircle(g);

            for (final Dot dot : dots) {
                final int xCoordinate = dot.x();
                final int yCoordinate = dot.y();

                g.fillOval(xCoordinate, yCoordinate, DOT_SIZE, DOT_SIZE);
            }

            Toolkit.getDefaultToolkit().sync();
        }
    }

    private void drawMainCircle(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawOval(mainCircleXCoordinate, mainCircleYCoordinate, DOT_SIZE, DOT_SIZE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (SHOULD_GENERATE_DOTS) {
            int halfOfMonth = 16;
            if (day != halfOfMonth) {
                handleNonHalfOfMonth();
            } else {
                handleHalfOfMonth();
            }
            repaint();
        }
    }

    private void handleNonHalfOfMonth() {
        if (isCloseToTheBorder()) {
            mainCircleXCoordinate = generateRandomPosition();
            mainCircleYCoordinate = generateRandomPosition();
        } else {
            handleNonBorderCase();
        }
        day += 1;
    }

    private void handleNonBorderCase() {
        dots.add(new Dot(mainCircleXCoordinate, mainCircleYCoordinate));

        List<int[]> coordinates = DotManager.getNeighborCoordinates(mainCircleXCoordinate, mainCircleYCoordinate);
        List<int[]> validCoordinates = DotManager.filterValidCoordinates(coordinates, dots);

        if (!validCoordinates.isEmpty()) {
            int randomIndex = random.nextInt(validCoordinates.size());
            int[] chosenCoordinate = validCoordinates.get(randomIndex);
            mainCircleXCoordinate = chosenCoordinate[0];
            mainCircleYCoordinate = chosenCoordinate[1];

            createNewDotInRandomCoordinate();
        } else {
            mainCircleXCoordinate = generateRandomPosition();
            mainCircleYCoordinate = generateRandomPosition();
            createNewDotInRandomCoordinate();
        }
    }

    private void handleHalfOfMonth() {
        int monthNumber = 1;

        if (dots.size() > monthNumber) {
            Dot dot = DotManager.getNeighborWithMostNeighbors(new Dot(mainCircleXCoordinate, mainCircleYCoordinate), dots);
            if (dot != null) {
                dots.remove(dot);

                mainCircleXCoordinate = dot.x();
                mainCircleYCoordinate = dot.y();
            } else {
                Dot closestDot = DotManager.getClosestDot(new Dot(mainCircleXCoordinate, mainCircleYCoordinate), dots);
                dots.remove(closestDot);

                mainCircleXCoordinate = closestDot.x();
                mainCircleYCoordinate = closestDot.y();
            }
        }

        if (dots.size() > monthNumber) {
            Dot dotWithLessNeighbors = DotManager.findDotWithLeastNeighbors(dots);
            dots.remove(dotWithLessNeighbors);
        }
    }


    private void createNewDotInRandomCoordinate() {
        List<int[]> newCoordinates = DotManager.getNeighborCoordinates(mainCircleXCoordinate, mainCircleYCoordinate);

        newCoordinates = newCoordinates.stream()
                .filter(coordinate -> dots.stream()
                        .noneMatch(dot -> Math.abs(dot.x() - coordinate[0]) < DOT_SIZE
                                && Math.abs(dot.y() - coordinate[1]) < DOT_SIZE))
                .toList();

        if (!newCoordinates.isEmpty()) {
            int[] randomCoordinate = newCoordinates.get(random.nextInt(newCoordinates.size()));
            Dot newDot = new Dot(randomCoordinate[0], randomCoordinate[1]);
            if (newDot.x() < BOARD_HEIGHT || newDot.y() < BOARD_WIDTH) {
                dots.add(newDot);
            }
        }
    }

    private boolean isCloseToTheBorder() {
        return mainCircleXCoordinate + DOT_SIZE > BOARD_WIDTH || mainCircleYCoordinate + DOT_SIZE > BOARD_HEIGHT;
    }
}
