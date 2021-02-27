package com.github.neblung.puzzle

import com.github.neblung.puzzle.Orientation.*

/**
 * Der Feld-Inhalt.
 * In einer Zelle ist kodiert, welcher Teil von welchem Stein in der Zelle liegt.
 * Bit 0 ist gesetzt, wenn die rechte Nachbarzelle zum selben Stein gehört.
 * Bit 1 ist gesetzt, wenn die obere Nachbarzelle zum selben Stein gehört.
 * Bit 2 ist gesetzt, wenn die linke Nachbarzelle zum selben Stein gehört.
 * Bit 3 ist gesetzt, wenn die untere Nachbarzelle zum selben Stein gehört.
 * Das korrespondiert mit den Ordinal-Zahlen von Orientation.
 * -1 ist die Sonderbedeutung für 'kein Stein', sonder ein Loch.
 */

class Field(val cells: Array<ByteArray>) {
    companion object {
        private const val HOLE = (-1).toByte()
    }

    /**
     * Erstellt eine Kopie von diesem Feld. Um die Folge-Stellung zu berechnen,
     * machen wir zuerst eine Kopie und arbeiten dann die Änderung ein, die sich
     * durch das Schieben ergibt.
     */
    fun copy(): Field {
        val copiedCells = cells.map { row -> ByteArray(row.size) { row[it] } }
        return Field(copiedCells.toTypedArray())
    }

    /**
     * Liefert eine Darstellung.
     */
    fun print(): List<String> {
        fun rowToString(row: ByteArray): String {
            return String(row.map { cellToChar(it) }.toCharArray())
        }
        return cells.map { rowToString(it) }
    }

    /**
     * Schieben eines Steins mit der schlanken Seite.
     * Also die kleinen Steine, oder der breite Stein horizontal, oder der stehende
     * Stein vertikal.
     * @return ob sich das Loch um eine oder um zwei Positionen verschiebt
     */
    fun fillThin(hole: Position, from: Orientation, cell: Byte): Int {
        return numSteps(cell, from).also { steps ->
            doFill(hole, from, steps)
        }
    }

    /**
     * Schieben eines Steins mit der breiten Seite.
     * Also der große Stein, oder der stehende Stein horizontal, oder der breite
     * Stein vertikal.
     * @return ob sich die Löcher um eine oder um zwei Positionen verschieben
     */
    fun fillLarge(hole: Position, main: Orientation, cell: Byte, side: Orientation): Int {
        return (numSteps(cell, main)).also { steps ->
            doFill(hole, main, steps)
            doFill(hole.go(side), main, steps)
        }
    }

    private fun numSteps(cell: Byte, main: Orientation) = when {
        belongsToSamePiece(cell, main) -> 2
        else -> 1
    }

    private fun doFill(hole: Position, from: Orientation, steps: Int) {
        this[hole] = this[hole.go(from)]
        if (steps == 2) {
            this[hole.go(from)] = this[hole.go(from, steps = 2)]
            this[hole.go(from, steps = 2)] = HOLE
        } else {
            this[hole.go(from)] = HOLE
        }
    }

    operator fun get(position: Position): Byte {
        return cells[position.y][position.x]
    }

    private operator fun set(position: Position, value: Byte) {
        cells[position.y][position.x] = value
    }

    /**
     * Kann das gegebene Loch aus der gegebenen Richtung befüllt werden?
     * Ist Schieben mit schmaler Seite möglich?
     * @return die Zelle, die in das Loch geschoben wird oder aber null, wenn das nicht geht
     */
    fun canFillThin(hole: Position, from: Orientation): Byte? {
        if (hole.isAtBorder(from)) return null
        return this[hole.go(from)].takeUnless { candidate ->
            candidate == HOLE
                    || belongsToSamePiece(candidate, from.next)
                    || belongsToSamePiece(candidate, from.previous)
        }
    }

    /**
     * Kann das gegebene Loch aus der gegebenen Richtung befüllt werden?
     * Ist Schieben mit breiter Seite möglich?
     * Der Aufrufer prüft, dass das andere Loch passend angrenzt.
     * @return die Zelle, die in das Loch geschoben wird oder aber null, wenn das nicht geht
     */
    fun canFillLarge(hole: Position, main: Orientation, side: Orientation): Byte? {
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
    EAST -> x == 3
    WEST -> x == 0
    NORTH -> y == 0
    SOUTH -> y == 4
}

private fun belongsToSamePiece(cell: Byte, from: Orientation): Boolean {
    return cell.toInt() and (1 shl from.ordinal) == (1 shl from.ordinal)
}

private fun cellToChar(value: Byte): Char = when (value.toInt()) {
    0 -> 'o'
    1, 4 -> '-'
    2, 8 -> 'I'
    9, 0xC, 3, 6 -> 'X'
    (-1) -> ' '
    else -> throw IllegalStateException("can't print: $value")
}
