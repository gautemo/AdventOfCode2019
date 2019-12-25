import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class BugsKtTest {

    @Test
    fun `total biodiversity 2129920`() {
        val map = """
            ....#
            #..#.
            #..##
            ..#..
            #....
        """.trimIndent()
        val res = findRepeatedBiodiversity(map)
        assertEquals(2129920, res)
    }

    @Test
    fun `total bugs of 99 after 10 minutes spacefold`(){
        val map = """
            ....#
            #..#.
            #.?##
            ..#..
            #....
        """.trimIndent()

        val res = findBugsSpaceFold(map, 10)
        assertEquals(99, res)
    }

}