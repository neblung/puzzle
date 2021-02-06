package com.github.neblung.puzzle

/**
 * Der Feld-Inhalt.
 * Im value ist kodiert, welcher Teil von welchem Stein in der Zelle liegt.
 * Bit 0 ist gesetzt, wenn die rechte Nachbarzelle zum selben Stein gehört.
 * Bit 1 ist gesetzt, wenn die obere Nachbarzelle zum selben Stein gehört.
 * Bit 2 ist gesetzt, wenn die linke Nachbarzelle zum selben Stein gehört.
 * Bit 3 ist gesetzt, wenn die untere Nachbarzelle zum selben Stein gehört.
 * -1 ist die Sonderbedeutung für 'kein Stein'.
 */

class Field(val cells: Array<ByteArray>) {
    fun print(): List<String> {
        fun rowToString(row: ByteArray): String {
            return String(row.map { cellToChar(it) }.toCharArray())
        }
        return cells.map { rowToString(it) }
    }
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
