import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class CrossedWiresKtTest {

    @Test
    fun `wires should be 6`() {
        val wire1 = "R8,U5,L5,D3"
        val wire2 = "U7,R6,D4,L4"

        val path1 = path(wire1)
        val path2 = path(wire2)

        val dist = closestIntersect(path1, path2)

        assertEquals(6, dist)
    }

    @Test
    fun `wires steps to first intersect should be 30`() {
        val wire1 = "R8,U5,L5,D3"
        val wire2 = "U7,R6,D4,L4"

        val path1 = path(wire1)
        val path2 = path(wire2)

        val steps = leastSteps(path1, path2)

        assertEquals(30, steps)
    }

}