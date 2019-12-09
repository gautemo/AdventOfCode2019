import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class IntStateKtTest {

    @Test
    fun `should output 16 digit number`(){
        val list = "1102,34915192,34915192,7,4,7,99,0"
        val res = boostCode(list, listOf(0))
        assertEquals(16, res.toString().length)
    }
}