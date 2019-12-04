import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SecureKtTest {

    @Test
    fun isPassword() {
        val input = 111111
        val res = isPassword(input, false)
        assertEquals(true, res)
    }

    @Test
    fun `is not pw because of decrease`() {
        val input = 223450
        val res = isPassword(input, false)
        assertEquals(false, res)
    }

    @Test
    fun `is not pw because of no double`() {
        val input = 123789
        val res = isPassword(input, false)
        assertEquals(false, res)
    }

    @Test
    fun `is not pw because of tripple`() {
        val input = 123444
        val res = isPassword(input, true)
        assertEquals(false, res)
    }

    @Test
    fun `is pw with double`() {
        val input = 123449
        val res = isPassword(input, true)
        assertEquals(true, res)
    }
}
