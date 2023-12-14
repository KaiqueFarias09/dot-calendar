package com.dots;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private final int DOT_SIZE = 10;
    private final int BOARD_WIDTH = 900 - DOT_SIZE;
    private final int BOARD_HEIGHT = 900 - DOT_SIZE;

    private final Random random = new Random();
    private final List<Dot> dots = new ArrayList<>();

    private final boolean shouldGenerateDots = true;
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
        int DELAY = 1000;
        Timer timer = new Timer(DELAY, this);
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

        if (shouldGenerateDots) {
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
        if (shouldGenerateDots) {

            int halfOfMonth = 16;
            if (day != halfOfMonth) {
                if (isCloseToTheBorder()) {
                    mainCircleXCoordinate = generateRandomPosition();
                    mainCircleYCoordinate = generateRandomPosition();
                } else {
                    dots.add(new Dot(mainCircleXCoordinate, mainCircleYCoordinate));

                    List<int[]> coordinates = getNeighborCoordinates(mainCircleXCoordinate, mainCircleYCoordinate);
                    List<int[]> validCoordinates = filterValidCoordinates(coordinates, dots);

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
                day += 1;
            } else {
                int monthNumber = 1;

                if (dots.size() > monthNumber) {
                    Dot dot = DotManager
                            .getNeighborWithMostNeighbors(new Dot(mainCircleXCoordinate, mainCircleYCoordinate), dots);
                    if (dot != null) {
                        dots.remove(dot);

                        mainCircleXCoordinate = dot.x();
                        mainCircleYCoordinate = dot.y();
                    } else {
                        Dot closestDot = DotManager.getClosestDot(new Dot(mainCircleXCoordinate, mainCircleYCoordinate),
                                dots);
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

            repaint();
        }
    }

    private void createNewDotInRandomCoordinate() {
        List<int[]> newCoordinates = getNeighborCoordinates(mainCircleXCoordinate, mainCircleYCoordinate);

        newCoordinates = newCoordinates.stream()
                .filter(coordinate -> dots.stream()
                        .noneMatch(dot -> Math.abs(dot.x() - coordinate[0]) < DOT_SIZE
                                && Math.abs(dot.y() - coordinate[1]) < DOT_SIZE))
                .collect(Collectors.toList());

        if (!newCoordinates.isEmpty()) {
            int[] randomCoordinate = newCoordinates.get(random.nextInt(newCoordinates.size()));
            Dot newDot = new Dot(randomCoordinate[0], randomCoordinate[1]);
            if (newDot.x() < BOARD_HEIGHT || newDot.y() < BOARD_WIDTH) {
                dots.add(newDot);
            }
        }
    }

    private List<int[]> filterValidCoordinates(List<int[]> coordinates, List<Dot> dots) {
        return coordinates.stream()
                .filter(coordinate -> dots.stream()
                        .noneMatch(dot -> dot.x() == coordinate[0] && dot.y() == coordinate[1]))
                .collect(Collectors.toList());
    }

    private List<int[]> getNeighborCoordinates(int x, int y) {
        List<int[]> coordinates = new ArrayList<>();

        int[] dx = { 1, -1, 0, 0, 1, -1, 1, -1 };
        int[] dy = { 0, 0, 1, -1, 1, 1, -1, -1 };

        for (int i = 0; i < 8; i++) {
            int MOVE_AMOUNT = 10;
            coordinates.add(new int[] { x + MOVE_AMOUNT * dx[i], y + MOVE_AMOUNT * dy[i] });
        }

        return coordinates;
    }

    private boolean isCloseToTheBorder() {
        return mainCircleXCoordinate + DOT_SIZE > BOARD_WIDTH || mainCircleYCoordinate + DOT_SIZE > BOARD_HEIGHT;
    }
}
