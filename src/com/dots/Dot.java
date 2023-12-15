package com.dots;

/**
 * Represents a dot in a grid with x and y coordinates.
 * This class is immutable and serves as a simple data holder.
 *
 * @param x The x-coordinate of the dot.
 * @param y The y-coordinate of the dot.
 */
public record Dot(int x, int y) {
    // The record automatically generates a constructor, toString, equals, and hashCode methods.
    // No additional methods or state are defined since it's a simple data holder.
}
