package com.github.neblung.puzzle

fun main() {
    findSolution()?.let { printSolution(it) } ?: println("there is no solution")
}

private fun printSolution(step: Board): Int {
    val numBoardsBefore = step.predecessor?.let { printSolution(it) } ?: 0
    return (numBoardsBefore + 1).also { nr ->
        println("\n-- $nr --\n")
        step.field.print().forEach { println(it) }
    }
}

private val initialField = Field(
    arrayOf(
        byteArrayOf(8, 0, 0, 8),    // A
        byteArrayOf(2, 0, 0, 2),    // B
        byteArrayOf(-1, 1, 4, -1),  // C
        byteArrayOf(8, 9, 0xC, 8),  // D
        byteArrayOf(2, 3, 6, 2),    // E
    )
)

/**
 * Findet eine Lösung durch Breitensuche.
 * Wir errechnen ausgehend von der Start-Stellung alle Folge-Stellungen durch die möglichen Bewegungen.
 * Bereits bekannte Stellungen werden verworfen.
 */
fun findSolution(): Board? {
    val startBoard = Board(null, initialField, space("C1", "C4"))
    val queue = ArrayDeque(listOf(startBoard))
    val known = mutableSetOf<Field>()

    fun printStatistic() {
        println("queue size == ${queue.size}  # known fields == ${known.size}")
    }

    // Die Lösung ist gefunden, wenn der große Stein oben mittig ist.
    fun Field.isSolution() = cells[0][1] == 9.toByte()

    while (queue.isNotEmpty()) {
//        printStatistic()
        val board = queue.removeFirst()
        if (board.field.isSolution()) {
            return board
        } else if (known.add(board.field)) {
            board.calculateSuccessors().filter { it.field !in known }.forEach { queue.addLast(it) }
        }
    }
    return null
}

