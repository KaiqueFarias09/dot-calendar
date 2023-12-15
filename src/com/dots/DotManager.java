package com.dots;

import java.util.ArrayList;
import java.util.List;

public class DotManager {
    private static final int MOVE_AMOUNT = 10;

    private DotManager() {
    }

    public static List<int[]> getNeighborCoordinates(int x, int y) {
        List<int[]> coordinates = new ArrayList<>();

        int[] dx = { 1, -1, 0, 0, 1, -1, 1, -1 };
        int[] dy = { 0, 0, 1, -1, 1, 1, -1, -1 };

        for (int i = 0; i < 8; i++) {
            coordinates.add(new int[] { x + MOVE_AMOUNT * dx[i], y + MOVE_AMOUNT * dy[i] });
        }

        return coordinates;
    }

    public static List<int[]> filterValidCoordinates(List<int[]> coordinates, List<Dot> dots) {
        return coordinates.stream()
                .filter(coordinate -> dots.stream()
                        .noneMatch(dot -> dot.x() == coordinate[0] && dot.y() == coordinate[1]))
                .toList();
    }

    public static Dot getNeighborWithMostNeighbors(Dot mainDot, List<Dot> dots) {
        List<Dot> neighbors = getNeighbors(mainDot, dots);
        int maxNeighborCount = Integer.MIN_VALUE;
        Dot neighborWithMostNeighbors = null;

        for (Dot neighbor : neighbors) {
            int neighborCount = countNeighbors(neighbor, dots);
            if (neighborCount > maxNeighborCount) {
                maxNeighborCount = neighborCount;
                neighborWithMostNeighbors = neighbor;
            }
        }

        return neighborWithMostNeighbors;
    }

    private static List<Dot> getNeighbors(Dot targetDot, List<Dot> dots) {
        return dots.stream()
                .filter(dot -> dot != targetDot && isNeighbor(dot, targetDot))
                .toList();
    }

    public static Dot getClosestDot(Dot mainDot, List<Dot> dots) {
        double minDistance = Double.MAX_VALUE;
        Dot closestDot = null;

        for (Dot dot : dots) {
            if (dot != mainDot) {
                double distance = calculateDistance(mainDot, dot);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestDot = dot;
                }
            }
        }

        return closestDot;
    }

    private static double calculateDistance(Dot dot1, Dot dot2) {
        int dx = dot1.x() - dot2.x();
        int dy = dot1.y() - dot2.y();
        return Math.sqrt((double)(dx * dx) + (dy * dy));
    }

    public static Dot findDotWithLeastNeighbors(List<Dot> dots) {
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

    public static int countNeighbors(Dot targetDot, List<Dot> dots) {
        int count = 0;

        for (Dot dot : dots) {
            if (dot != targetDot && isNeighbor(dot, targetDot)) {
                count++;
            }
        }

        return count;
    }

    public static boolean isNeighbor(Dot dot1, Dot dot2) {
        int dx = Math.abs(dot1.x() - dot2.x());
        int dy = Math.abs(dot1.y() - dot2.y());

        return (dx == MOVE_AMOUNT && dy == 0) || (dx == 0 && dy == MOVE_AMOUNT);
    }
}
