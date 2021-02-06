package puzzle

import com.github.neblung.puzzle.Field
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class FieldTests {
    @Test
    fun `should be printed prettily`() {
        val content: Array<ByteArray> = arrayOf(
            byteArrayOf(8, 0, 0, 8),
            byteArrayOf(2, 0, 0, 2),
            byteArrayOf(-1, 1, 4, -1),
            byteArrayOf(8, 9, 0xC, 8),
            byteArrayOf(2, 3, 6, 2),
        )

        val initialField = Field(content)

        initialField.print() shouldBe listOf(
            "IooI",
            "IooI",
            " -- ",
            "IXXI",
            "IXXI",
        )
    }

    @Test
    fun `test parsing`() {
        val content = parseField(
            """
                8008
                2002
                 14
                89C8
                2362
            """.trimIndent()
        )

        val initialField = Field(content)

        initialField.print() shouldBe listOf(
            "IooI",
            "IooI",
            " -- ",
            "IXXI",
            "IXXI",
        )
    }
}