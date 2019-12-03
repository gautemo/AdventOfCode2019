import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class IntStateKtTest {

    @Test
    fun `string should be int arr`() {
        val input = "1,0,0,0,99"
        val res = toArr(input)
        assertEquals(arrayListOf(1, 0, 0, 0, 99), res)
    }

    @Test
    fun `1,0,0,0,99 becomes 2,0,0,0,99`(){
        val input = mutableListOf(1,0,0,0,99)
        val exp = arrayListOf(2, 0, 0, 0, 99)
        val res = transform(input)
        assertEquals(exp, res)
    }
}