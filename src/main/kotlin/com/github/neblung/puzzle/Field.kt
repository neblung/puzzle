package com.github.neblung.puzzle

/**
 * Der Feld-Inhalt.
 * Im value ist kodiert, welcher Teil von welchem Stein in der Zelle liegt.
 * Bit 0 ist gesetzt, wenn die rechte Nachbarzelle zum selben Stein gehört.
 * Bit 1 ist gesetzt, wenn die obere Nachbarzelle zum selben Stein gehört.
 * Bit 2 ist gesetzt, wenn die linke Nachbarzelle zum selben Stein gehört.
 * Bit 3 ist gesetzt, wenn die untere Nachbarzelle zum selben Stein gehört.
 * Das korrespondiert mit den Ordinal-Zahlen von Orientation.
 * -1 ist die Sonderbedeutung für 'kein Stein'.
 */

class Field(val cells: Array<ByteArray>) {
    companion object {
        private const val HOLE = (-1).toByte()
    }

    fun copy(): Field {
        val copiedCells = cells.map { row -> ByteArray(row.size) { row[it] } }
        return Field(copiedCells.toTypedArray())
    }

    fun print(): List<String> {
        fun rowToString(row: ByteArray): String {
            return String(row.map { cellToChar(it) }.toCharArray())
        }
        return cells.map { rowToString(it) }
    }

    fun fillThin(hole: Position, from: Orientation, cell: Byte): Int {
        return doFill(hole, from, belongsToSamePiece(cell, from))
    }

    fun fillLarge(hole: Position, main: Orientation, cell: Byte, side: Orientation): Int {
        val large = belongsToSamePiece(cell, main)
        doFill(hole, main, large)
        doFill(hole.go(side), main, large)
        return if (large) 2 else 1
    }

    private fun doFill(hole: Position, from: Orientation, large: Boolean): Int {
        this[hole] = this[hole.go(from)]
        return if (large) {
            this[hole.go(from)] = this[hole.go(from, steps = 2)]
            this[hole.go(from, steps = 2)] = HOLE
            2
        } else {
            this[hole.go(from)] = HOLE
            1
        }
    }

    operator fun get(position: Position): Byte {
        return cells[position.y][position.x]
    }

    private operator fun set(position: Position, value: Byte) {
        cells[position.y][position.x] = value
    }

    fun canMoveThin(hole: Position, from: Orientation): Byte? {
        if (hole.isAtBorder(from)) return null
        return this[hole.go(from)].takeUnless { candidate ->
            candidate == HOLE
                    || belongsToSamePiece(candidate, from.next)
                    || belongsToSamePiece(candidate, from.previous)
        }
    }

    fun canLargeMove(hole: Position, main: Orientation, side: Orientation): Byte? {
        if (hole.isAtBorder(main)) return null
        return this[hole.go(main)].takeIf { candidate ->
            belongsToSamePiece(candidate, side)
        }
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            javaClass != other?.javaClass -> false
            else -> cells.contentDeepEquals((other as Field).cells)
        }
    }

    override fun hashCode(): Int {
        return cells.contentDeepHashCode()
    }
}

private fun Position.isAtBorder(orientation: Orientation) = when (orientation) {
    Orientation.EAST -> x == 3
    Orientation.WEST -> x == 0
    Orientation.NORTH -> y == 0
    Orientation.SOUTH -> y == 4
}

private fun belongsToSamePiece(cell: Byte, orientation: Orientation): Boolean {
    return cell.toInt() and (1 shl orientation.ordinal) == (1 shl orientation.ordinal)
}

private fun cellToChar(value: Byte): Char = when (value) {
    0.toByte() -> 'o'
    1.toByte() -> '-'
    4.toByte() -> '-'
    2.toByte() -> 'I'
    8.toByte() -> 'I'
    3.toByte() -> 'X'
    6.toByte() -> 'X'
    9.toByte() -> 'X'
    12.toByte() -> 'X'
    (-1).toByte() -> ' '
    else -> throw IllegalStateException("can't print: $value")
}
