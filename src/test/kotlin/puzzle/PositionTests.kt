package puzzle

import com.github.neblung.puzzle.Orientation.*
import com.github.neblung.puzzle.Position
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PositionTests {
    @Test
    fun `go -- samples`() {
        Position(y = 3, x = 1).go(NORTH) shouldBe Position(y = 2, x = 1)
        Position(y = 3, x = 1).go(SOUTH) shouldBe Position(y = 4, x = 1)
        Position(y = 3, x = 1).go(EAST) shouldBe Position(y = 3, x = 2)
        Position(y = 3, x = 1).go(WEST) shouldBe Position(y = 3, x = 0)
        // steps = 2
        Position(y = 3, x = 1).go(NORTH, steps = 2) shouldBe Position(y = 1, x = 1)
    }
}