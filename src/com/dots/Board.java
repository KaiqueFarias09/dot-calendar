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

    private final int BOARD_WIDTH = 900;
    private final int BOARD_HEIGHT = 900;
    private final int DOT_SIZE = 10;
    private final int DELAY = 1000;
    private final int MOVE_AMOUNT = 10;

    private final Random random = new Random();
    private List<Dot> dots = new ArrayList<>();

    private boolean shouldGenerateDots = true;
    private int day = 0;
    private int halfOfMonth = 16;
    private int monthNumber = 1;

    private int mainCircleXCoordinate;
    private int mainCircleYCoordinate;

    private Timer timer;

    public Board() {
        initBoard();
        mainCircleXCoordinate = generateRandomPosition();
        mainCircleYCoordinate = generateRandomPosition();
    }

    private void initBoard() {
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
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

        if (shouldGenerateDots) {
            drawMainCircle(g);

            for (int z = 0; z < dots.size(); z++) {
                final Dot dot = dots.get(z);
                final int xCoordinate = dot.getX();
                final int yCoordinate = dot.getY();

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

            if (day != halfOfMonth) {
                if (isCloseToTheBorder()) {
                    mainCircleXCoordinate = generateRandomPosition();
                    mainCircleYCoordinate = generateRandomPosition();
                } else {
                    dots.add(new Dot(mainCircleXCoordinate, mainCircleYCoordinate));

                    List<int[]> coordinates = getNeighborCoordinates(mainCircleXCoordinate, mainCircleYCoordinate);
                    List<int[]> validCoordinates = filterValidCoordinates(coordinates, dots);

                    int randomIndex = random.nextInt(validCoordinates.size());
                    int[] chosenCoordinate = validCoordinates.get(randomIndex);
                    mainCircleXCoordinate = chosenCoordinate[0];
                    mainCircleYCoordinate = chosenCoordinate[1];

                    createNewDotInRandomCoordinate();
                }
                day += 1;
            } else {
                if (dots.size() > monthNumber) {
                    Dot dotWithLessNeighbors = findDotWithLeastNeighbors(dots);
                    dots.remove(dotWithLessNeighbors);
                }

            }

            repaint();
        }
    }

    private Dot findDotWithLeastNeighbors(List<Dot> dots) {
        int minNeighborCount = Integer.MAX_VALUE;
        Dot dotWithLeastNeighbors = null;

        for (Dot dot : dots) {
            int neighborCount = countNeighbors(dot, dots);
            if (neighborCount < minNeighborCount) {
                minNeighborCount = neighborCount;
                dotWithLeastNeighbors = dot;
            }
        }

        return dotWithLeastNeighbors;
    }

    private int countNeighbors(Dot targetDot, List<Dot> dots) {
        int count = 0;

        for (Dot dot : dots) {
            if (dot != targetDot && isNeighbor(dot, targetDot)) {
                count++;
            }
        }

        return count;
    }

    private boolean isNeighbor(Dot dot1, Dot dot2) {
        int dx = Math.abs(dot1.getX() - dot2.getX());
        int dy = Math.abs(dot1.getY() - dot2.getY());

        return (dx == MOVE_AMOUNT && dy == 0) || (dx == 0 && dy == MOVE_AMOUNT);
    }

    private void createNewDotInRandomCoordinate() {
        List<int[]> newCoordinates = getNeighborCoordinates(mainCircleXCoordinate, mainCircleYCoordinate);
        int[] randomCoordinate = newCoordinates.get(random.nextInt(newCoordinates.size()));
        dots.add(new Dot(randomCoordinate[0], randomCoordinate[1]));
    }

    private List<int[]> filterValidCoordinates(List<int[]> coordinates, List<Dot> dots) {
        return coordinates.stream()
                .filter(coordinate -> dots.stream()
                        .noneMatch(dot -> dot.getX() == coordinate[0] && dot.getY() == coordinate[1]))
                .collect(Collectors.toList());
    }

    private List<int[]> getNeighborCoordinates(int x, int y) {
        List<int[]> coordinates = new ArrayList<>();

        int[] dx = { 1, -1, 0, 0, 1, -1, 1, -1 };
        int[] dy = { 0, 0, 1, -1, 1, 1, -1, -1 };

        for (int i = 0; i < 8; i++) {
            coordinates.add(new int[] { x + MOVE_AMOUNT * dx[i], y + MOVE_AMOUNT * dy[i] });
        }

        return coordinates;
    }

    private boolean isCloseToTheBorder() {
        return mainCircleXCoordinate + DOT_SIZE > BOARD_WIDTH || mainCircleYCoordinate + DOT_SIZE > BOARD_HEIGHT;
    }
}
