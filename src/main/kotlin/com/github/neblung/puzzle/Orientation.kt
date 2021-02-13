package com.github.neblung.puzzle

enum class Orientation {
    EAST, NORTH, WEST, SOUTH;

    val next: Orientation get() = values()[(ordinal + 1) and 3]
    val previous: Orientation get() = values()[(ordinal - 1) and 3]
    val opposite: Orientation get() = values()[ordinal xor 2]
}