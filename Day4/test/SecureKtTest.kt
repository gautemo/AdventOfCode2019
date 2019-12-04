import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SecureKtTest {

    @Test
    fun isPassword() {
        val input = 111111
        val res = isPassword(input)
        assertEquals(false, res) //false for part 2 and true for part 1
    }

    @Test
    fun `is not pw because of decrease`() {
        val input = 223450
        val res = isPassword(input)
        assertEquals(false, res)
    }

    @Test
    fun `is not pw because of no double`() {
        val input = 123789
        val res = isPassword(input)
        assertEquals(false, res)
    }

    @Test
    fun `is not pw because of tripple`() {
        val input = 123444
        val res = isPassword(input)
        assertEquals(false, res)
    }
}
