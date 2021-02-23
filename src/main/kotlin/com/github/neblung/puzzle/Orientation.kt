package com.github.neblung.puzzle

/**
 * Richtung aus der man Steine ins Loch schieben kann.
 * Die Ordinal-Zahl korrespondiert mit der Kodierung der Zellen auf dem Spielfeld.
 */
enum class Orientation {
    EAST, NORTH, WEST, SOUTH;

    val next: Orientation get() = values()[(ordinal + 1) and 3]
    val previous: Orientation get() = values()[(ordinal - 1) and 3]
    val opposite: Orientation get() = values()[ordinal xor 2]
}