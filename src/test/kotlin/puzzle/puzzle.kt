package puzzle

import com.github.neblung.puzzle.Position
import com.github.neblung.puzzle.Space

private fun toByte(ch: Char): Byte {
    return when (ch) {
        ' ', '.' -> -1
        'C' -> 12
        else -> (ch - '0').toByte()
    }
}

fun parseField(string: String): Array<ByteArray> {
    fun toRow(rowString: String): ByteArray {
        return rowString.map { toByte(it) }.toByteArray()
    }

    return string.lines().map {
        toRow(it.padEnd(4, ' '))
    }.toTypedArray()
}

fun space(hole1Coordinates: String, hole2Coordinates: String): Space {
    fun translate(coordinates: String): Position {
        assert(coordinates.length == 2)
        val y: Int = coordinates[0] - 'A'
        val x: Int = coordinates[1] - '1'
        return Position(y, x)
    }
    return Space(translate(hole1Coordinates), translate(hole2Coordinates))
}
