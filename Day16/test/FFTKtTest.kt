import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class FFTKtTest {

    @Test
    fun `12345678 becomes 01029498 after 4`(){
        val input = "12345678"
        val res = runFFT(input, 4)
        assertEquals("01029498", res)
    }

    @Test
    fun `80871224585914546619083218645595 becomes 24176176 after 100`() {
        val input = "80871224585914546619083218645595"
        val res = runFFT(input, 100)
        assertEquals("24176176", res)
    }

     @Test
     fun `03036732577212944063491565474664 times 10000 becomes 84462026 after 100`(){
         val input = "03036732577212944063491565474664"
         val res = runFFT(input, 100, 10000, 7)
         assertEquals("84462026", res)
     }

}