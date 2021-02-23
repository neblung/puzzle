package puzzle

import com.github.neblung.puzzle.Field
import com.github.neblung.puzzle.Orientation
import com.github.neblung.puzzle.Orientation.*
import com.github.neblung.puzzle.position
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
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

    @Nested
    inner class Movement {
        private lateinit var field: Field

        private infix fun setUpContent(beforeMove: String) {
            field = Field(parseField(beforeMove))
        }

        // method under test
        private fun fillThin(holeString: String, from: Orientation): Int {
            val hole = position(holeString)
            val cell = field[hole.go(from)]
            return field.fillThin(hole, from, cell)
        }

        // method under test
        private fun fillLarge(holeString: String, main: Orientation, side: Orientation): Int {
            val hole = position(holeString)
            val cell = field[hole.go(main)]
            return field.fillLarge(hole, main, cell, side)
        }

        private infix fun Field.shouldHaveContent(expectedString: String) {
            fun Field.printed() = "\n" + print().joinToString(separator = "\n")

            val expected = Field(parseField(expectedString))
            this.printed() shouldBe expected.printed()
        }

        @Test
        fun `move small horizontally -- should toggle cells with hole`() {
            this setUpContent """
                14.0
            """.trimIndent()

            fillThin("A3", from = EAST) shouldBe 1

            field shouldHaveContent """
                140.
            """.trimIndent()

            fillThin("A4", from = WEST) shouldBe 1

            field shouldHaveContent """
                14.0
            """.trimIndent()
        }

        @Test
        fun `move small vertically -- should toggle cells with hole`() {
            this setUpContent """
                88 8
                2202
            """.trimIndent()

            fillThin("A3", from = SOUTH) shouldBe 1

            field shouldHaveContent """
                8808
                22 2
            """.trimIndent()

            fillThin("B3", from = NORTH) shouldBe 1

            field shouldHaveContent """
                88 8
                2202
            """.trimIndent()
        }

        @Test
        fun `move wide horizontally -- hole should jump 2 cells`() {
            this setUpContent """
                014.
            """.trimIndent()

            fillThin("A4", from = WEST) shouldBe 2

            field shouldHaveContent """
                0.14
            """.trimIndent()

            fillThin("A2", from = EAST) shouldBe 2

            field shouldHaveContent """
                014.
            """.trimIndent()
        }

        @Test
        fun `move tall vertically -- hole should jump 2 cells`() {
            this setUpContent """
                800 
                2008
                 142
            """.trimIndent()

            fillThin("A4", from = SOUTH) shouldBe 2

            field shouldHaveContent """
                8008
                2002
                 14
            """.trimIndent()

            fillThin("C1", from = NORTH) shouldBe 2

            field shouldHaveContent """
                 008
                8002
                214
            """.trimIndent()
        }

        @Test
        fun `move tall horizontally`() {
            this setUpContent """
                9C 8
                36 2
            """.trimIndent()

            fillLarge("A3", main = EAST, side = SOUTH) shouldBe 1

            field shouldHaveContent """
                9C8.
                362.
            """.trimIndent()

            fillLarge("B4", main = WEST, side = NORTH) shouldBe 1

            field shouldHaveContent """
                9C 8
                36 2
            """.trimIndent()
        }

        @Test
        fun `move wide vertically`() {
            this setUpContent """
                8  8
                2142
            """.trimIndent()

            fillLarge("A3", main = SOUTH, side = WEST) shouldBe 1

            field shouldHaveContent """
                8148
                2  2
            """.trimIndent()

            fillLarge("B2", main = NORTH, side = EAST) shouldBe 1

            field shouldHaveContent """
                8  8
                2142
            """.trimIndent()
        }

        @Test
        fun `move big horizontally`() {
            this setUpContent """
                89C.
                236.
            """.trimIndent()

            fillLarge("A4", main = WEST, side = SOUTH) shouldBe 2

            field shouldHaveContent """
                8 9C
                2 36
            """.trimIndent()

            fillLarge("B2", main = EAST, side = NORTH) shouldBe 2

            field shouldHaveContent """
                89C
                236
            """.trimIndent()
        }

        @Test
        fun `move big vertically`() {
            this setUpContent """
                89C8
                2362
                0  0
            """.trimIndent()

            fillLarge("C2", main = NORTH, side = EAST) shouldBe 2

            field shouldHaveContent """
                8  8
                29C2
                0360
            """.trimIndent()

            fillLarge("A3", main = SOUTH, side = WEST) shouldBe 2

            field shouldHaveContent """
                89C8
                2362
                0  0
            """.trimIndent()
        }
    }

    @Nested
    inner class CheckThinMovement {
        private val field = Field(
            parseField(
                """
                    8008
                    2002
                    14
                    89C8
                    2362
                """.trimIndent()
            )
        )

        // method under test
        private fun check(holeString: String, from: Orientation): Byte? {
            val hole = position(holeString)
            return field.canFillThin(hole, from)
        }

        @Test
        fun samples() {
            check("C3", EAST) shouldBe null
            check("C3", NORTH) shouldBe 0
            check("C3", WEST) shouldBe 4
            check("C3", SOUTH) shouldBe null

            check("C4", EAST) shouldBe null
            check("C4", NORTH) shouldBe 2
            check("C4", WEST) shouldBe null
            check("C4", SOUTH) shouldBe 8
        }
    }

    @Nested
    inner class CheckLargeMovement {
        private lateinit var field: Field

        // method under test
        private fun check(holeString: String, main: Orientation, side: Orientation): Byte? {
            val hole = position(holeString)
            return field.canFillLarge(hole, main, side)
        }

        @Test
        fun samples() {
            field = Field(
                parseField(
                    """
                        8008
                        2142
                        0  0
                        89C8
                        2362
                    """.trimIndent()
                )
            )

            check("C2", SOUTH, EAST) shouldBe 9.toByte()
            check("C2", NORTH, EAST) shouldBe 1.toByte()

            check("C3", SOUTH, WEST) shouldBe 0xC.toByte()
            check("C3", NORTH, WEST) shouldBe 4.toByte()
        }

        @Test
        fun `at border`() {
            field = Field(
                parseField(
                    """
                          00
                        9C88
                        3622
                        8008
                        2142
                    """.trimIndent()
                )
            )

            check("A1", NORTH, EAST) shouldBe null
            check("A1", SOUTH, EAST) shouldBe 9.toByte()
        }
    }
}