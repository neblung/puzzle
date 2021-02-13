package com.github.neblung.puzzle

import com.github.neblung.puzzle.Orientation.*

/**
 * Eine Position auf dem Board.
 */
data class Position(val y: Int, val x: Int) {
    fun isAtBorder(orientation: Orientation) = when (orientation) {
        EAST -> x == 3
        WEST -> x == 0
        NORTH -> y == 0
        SOUTH -> y == 4
    }

    fun go(orientation: Orientation, steps: Int = 1) = when (orientation) {
        EAST -> copy(x = x + steps)
        WEST -> copy(x = x - steps)
        NORTH -> copy(y = y - steps)
        SOUTH -> copy(y = y + steps)
    }
}