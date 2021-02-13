package puzzle

import com.github.neblung.puzzle.Orientation.*
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class OrientationTests {
    @Test
    fun `next -- should return clockwise`() {
        EAST.next shouldBe NORTH
        NORTH.next shouldBe WEST
        WEST.next shouldBe SOUTH
        SOUTH.next shouldBe EAST
    }

    @Test
    fun `previous -- should return counterclockwise`() {
        EAST.previous shouldBe SOUTH
        NORTH.previous shouldBe EAST
        WEST.previous shouldBe NORTH
        SOUTH.previous shouldBe WEST
    }

    @Test
    fun opposite() {
        EAST.opposite shouldBe WEST
        WEST.opposite shouldBe EAST
        NORTH.opposite shouldBe SOUTH
        SOUTH.opposite shouldBe NORTH
    }
}