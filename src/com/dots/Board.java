package com.dots;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
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

    private boolean inGame = true;

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

        if (inGame) {
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
        if (inGame) {
            if (isCloseToTheBorder()) {
                mainCircleXCoordinate = generateRandomPosition();
                mainCircleYCoordinate = generateRandomPosition();
            } else {
                int[][] coordinates = getNeighborCoordinates(mainCircleXCoordinate, mainCircleYCoordinate);

                List<int[]> validCoordinates = filterValidCoordinates(coordinates, dots);

                int randomIndex = random.nextInt(validCoordinates.size());
                int[] chosenCoordinate = validCoordinates.get(randomIndex);

                dots.add(new Dot(mainCircleXCoordinate, mainCircleYCoordinate));

                mainCircleXCoordinate = chosenCoordinate[0];
                mainCircleYCoordinate = chosenCoordinate[1];
            }

            repaint();
        }
    }

    private List<int[]> filterValidCoordinates(int[][] coordinates, List<Dot> dots) {
        return Arrays.stream(coordinates)
                .filter(coordinate -> dots.stream()
                        .noneMatch(dot -> dot.getX() == coordinate[0] && dot.getY() == coordinate[1]))
                .collect(Collectors.toList());
    }

    private int[][] getNeighborCoordinates(int x, int y) {
        int[][] coordinates = new int[8][2];

        int[] dx = { 1, -1, 0, 0, 1, -1, 1, -1 };
        int[] dy = { 0, 0, 1, -1, 1, 1, -1, -1 };

        for (int i = 0; i < 8; i++) {
            coordinates[i] = new int[] { x + MOVE_AMOUNT * dx[i], y + MOVE_AMOUNT * dy[i] };
        }

        return coordinates;
    }

    private boolean isCloseToTheBorder() {
        return mainCircleXCoordinate + DOT_SIZE > BOARD_WIDTH || mainCircleYCoordinate + DOT_SIZE > BOARD_HEIGHT;
    }
}
