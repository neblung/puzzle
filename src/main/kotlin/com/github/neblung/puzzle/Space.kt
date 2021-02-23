package com.github.neblung.puzzle

/**
 * Position der beiden Löcher auf dem Spielfeld.
 */
data class Space(val hole1: Position, val hole2: Position)

fun position(coordinates: String): Position {
    assert(coordinates.length == 2)
    val y: Int = coordinates[0] - 'A'
    val x: Int = coordinates[1] - '1'
    return Position(y, x)
}

fun space(hole1Coordinates: String, hole2Coordinates: String): Space {
    return Space(position(hole1Coordinates), position(hole2Coordinates))
}
