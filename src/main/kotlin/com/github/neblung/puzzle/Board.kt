package com.github.neblung.puzzle

/**
 * Verkettung der Fields. Dadurch wird die Lösung ersichtlich.
 */
class Board(val predecessor: Board?, val field: Field, space: Space) {
    private val hole1 = space.hole1
    private val hole2 = space.hole2

    /**
     * Berechnet alle möglichen Nachfolge-Stellungen.
     * Diese ergeben sich dadurch, dass die Löcher - soweit möglich - von rechts, oben,
     * links oder unten befüllt werden (Schieben eines Steins).
     */
    fun calculateSuccessors(): List<Board> {
        val result = mutableListOf<Board>()
        Orientation.values().forEach { direction ->
            val thin1 = thinHole1Successor(direction)?.also { result += it }
            val thin2 = thinHole2Successor(direction)?.also { result += it }
            // Schieben mit breiter Seite ist nur möglich, falls
            //  - kein Schieben mit schmaler Seite möglich ist
            //  - die Löcher passend nebeneinander liegen
            if (thin1 == null && thin2 == null) {
                listOf(direction.previous, direction.next).forEach { side ->
                    if (hole2 == hole1.go(side)) {
                        largeSuccessor(direction, side)?.let { result += it }
                    }
                }
            }
        }
        return result
    }

    private fun thinHole1Successor(direction: Orientation): Board? {
        return field.canFillThin(hole1, direction)?.let { cell ->
            val copy = field.copy()
            val steps = copy.fillThin(hole1, direction, cell)
            val newSpace = Space(hole1.go(direction, steps), hole2)
            Board(this, copy, newSpace)
        }
    }

    private fun thinHole2Successor(direction: Orientation): Board? {
        return field.canFillThin(hole2, direction)?.let { cell ->
            val copy = field.copy()
            val steps = copy.fillThin(hole2, direction, cell)
            val newSpace = Space(hole1, hole2.go(direction, steps))
            Board(this, copy, newSpace)
        }
    }

    private fun largeSuccessor(direction: Orientation, side: Orientation): Board? {
        return field.canFillLarge(hole1, direction, side)?.let { cell ->
            val copy = field.copy()
            val steps = copy.fillLarge(hole1, direction, cell, side)
            val newSpace = Space(hole1.go(direction, steps), hole2.go(direction, steps))
            Board(this, copy, newSpace)
        }
    }
}
