import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class CaveKtTest {

    @Test
    fun `should take 8 steps`(){
        val map = "#########\n" +
                "#b.A.@.a#\n" +
                "#########"
        val res = bestOneMap(map)
        assertEquals(8, res)
    }

    @Test
    fun `should take 86 steps`(){
        val map = "########################\n" +
                "#f.D.E.e.C.b.A.@.a.B.c.#\n" +
                "######################.#\n" +
                "#d.....................#\n" +
                "########################"
        val res = bestOneMap(map)
        assertEquals(86, res)
    }

    @Test
    fun `should find splitted map in 8 steps`(){
        val map = """
            #######
            #a.#Cd#
            ##...##
            ##.@.##
            ##...##
            #cB#Ab#
            #######
        """.trimIndent()
        val res = bestSplittedMap(map)
        assertEquals(8, res)
    }

    @Test
    fun `should find splitted map in 72 steps`(){
        val map = """
                #############
                #g#f.D#..h#l#
                #F###e#E###.#
                #dCba...BcIJ#
                #####.@.#####
                #nK.L...G...#
                #M###N#H###.#
                #o#m..#i#jk.#
                #############
        """.trimIndent()
        val res = bestSplittedMap(map)
        assertEquals(72, res)
    }

}