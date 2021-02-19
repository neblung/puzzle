package puzzle

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
