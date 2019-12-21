import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class CaveKtTest {

    @Test
    fun `should take 8 steps`(){
        val map = "#########\n" +
                "#b.A.@.a#\n" +
                "#########"
        val res = path(map)
        assertEquals(8, res)
    }

    @Test
    fun `should take 86 steps`(){
        val map = "########################\n" +
                "#f.D.E.e.C.b.A.@.a.B.c.#\n" +
                "######################.#\n" +
                "#d.....................#\n" +
                "########################"
        val res = path(map)
        assertEquals(86, res)
    }

}