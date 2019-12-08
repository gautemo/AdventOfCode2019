import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SpaceImageTests {

    @Test
    fun `123456789013 multiplyNrDigits(1,2) should be 2`() {
        val input = "123456789013"
        val spaceImage = SpaceImage(input, 3, 2)

        val res = spaceImage.multiplyDigitsOnLeastZeros(1,2)

        assertEquals(1, res)
    }
}