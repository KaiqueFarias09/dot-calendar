package com.dots;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The Board class represents the panel where the dynamic dot grid simulation is displayed.
 * It extends JPanel and implements ActionListener to handle periodic updates.
 */
public class Board extends JPanel implements ActionListener {

    /**
     * The size of each dot.
     */
    private static final int DOT_SIZE = 10;
    /**
     * The width of the board.
     */
    private static final int BOARD_WIDTH = 900 - DOT_SIZE;
    /**
     * The height of the board.
     */
    private static final int BOARD_HEIGHT = 900 - DOT_SIZE;
    /**
     * Flag indicating whether dots should be generated.
     */
    private static final boolean SHOULD_GENERATE_DOTS = true;

    /**
     * A random number generator.
     */
    private final Random random = new Random();
    /**
     * List to store the dots on the board.
     */
    private final List<Dot> dots = new ArrayList<>();
    /**
     * The current day of the simulation.
     */
    private int day = 0;

    /**
     * The x-coordinate of the main circle.
     */
    private int mainCircleXCoordinate;
    /**
     * The y-coordinate of the main circle.
     */
    private int mainCircleYCoordinate;

    /**
     * Constructs a new Board instance, initializes the board, and sets the initial coordinates of the main circle.
     */
    public Board() {
        initBoard();
        mainCircleXCoordinate = 800;
        mainCircleYCoordinate = 800;
    }

    /**
     * Initializes the board by setting its background color, preferred size, and starting the game loop.
     */
    private void initBoard() {
        setBackground(Color.black);
        setFocusable(true);
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        initGame();
    }

    /**
     * Initializes the game loop by setting up a timer that triggers the actionPerformed method at regular intervals.
     */
    private void initGame() {
        Timer timer = new Timer(1000, this);
        timer.start();
    }

    /**
     * Overrides the paintComponent method to draw the dots and the main circle on the board.
     *
     * @param g The Graphics object to draw on.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    /**
     * Draws the main circle and all dots on the board.
     *
     * @param g The Graphics object to draw on.
     */
    private void doDrawing(Graphics g) {
        if (SHOULD_GENERATE_DOTS) {
            g.setColor(Color.BLUE);
            g.drawOval(mainCircleXCoordinate, mainCircleYCoordinate, DOT_SIZE, DOT_SIZE);

            for (final Dot dot : dots) {
                final int xCoordinate = dot.x();
                final int yCoordinate = dot.y();
                g.fillOval(xCoordinate, yCoordinate, DOT_SIZE, DOT_SIZE);
            }

            Toolkit.getDefaultToolkit().sync();
        }
    }

    /**
     * Handles the actionPerformed event triggered by the Timer.
     *
     * @param e The ActionEvent object representing the event.
     */
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

    /**
     * Handles the logic for a day that is not the half of the month.
     */
    private void handleNonHalfOfMonth() {
        if (isCloseToTheBorder()) {
            mainCircleXCoordinate = generateRandomPosition();
            mainCircleYCoordinate = generateRandomPosition();
        } else {
            handleNonBorderCase();
        }
        day += 1;
    }

    /**
     * Handles the logic for a day that is not the half of the month and the main circle is not close to the border.
     */
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

    /**
     * Handles the logic for a day that is the half of the month.
     */
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

    /**
     * Creates a new dot in a random coordinate based on the neighboring coordinates of the main circle.
     */
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

    /**
     * Generates a random position within a specified range and ensures that the generated position is not occupied by any existing dots.
     *
     * @return A randomly generated position that is not occupied by any existing dots.
     */
    private int generateRandomPosition() {
        int min = 20;
        int max = 891;
        int position;

        do {
            position = random.nextInt(max - min) + min;
        } while (isPositionOccupied(position));

        return position;
    }

    /**
     * Checks if a given position is occupied by any existing dots on the board.
     *
     * @param position The position to check for occupancy.
     * @return True if the position is occupied, false otherwise.
     */
    private boolean isPositionOccupied(int position) {
        for (Dot dot : dots) {
            // Adjust the condition based on your DOT_SIZE and proximity criteria
            if (Math.abs(dot.x() - position) < DOT_SIZE && Math.abs(dot.y() - position) < DOT_SIZE) {
                return true;
            }
        }
        return false;
    }


    /**
     * Checks if the main circle is close to the border.
     *
     * @return True if the main circle is close to the border, false otherwise.
     */
    private boolean isCloseToTheBorder() {
        return mainCircleXCoordinate + DOT_SIZE > BOARD_WIDTH || mainCircleYCoordinate + DOT_SIZE > BOARD_HEIGHT;
    }
}
