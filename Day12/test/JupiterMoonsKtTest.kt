import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class JupiterMoonsKtTest {

    @Test
    fun `should give energy 179`() {
        val input = "<x=-1, y=0, z=2>\n" +
                "<x=2, y=-10, z=-7>\n" +
                "<x=4, y=-8, z=8>\n" +
                "<x=3, y=5, z=-1>"

        val moons = getMoons(input)
        val res = energyAfterStep(moons, 10)
        assertEquals(179, res)
    }

    @Test
    fun `should give energy 1940`() {
        val input = "<x=-8, y=-10, z=0>\n" +
                "<x=5, y=5, z=10>\n" +
                "<x=2, y=-7, z=3>\n" +
                "<x=9, y=-8, z=-3>"

        val moons = getMoons(input)
        val res = energyAfterStep(moons, 100)
        assertEquals(1940, res)
    }

    @Test
    fun `should be repeated after 2772 steps`() {
        val input = "<x=-1, y=0, z=2>\n" +
                "<x=2, y=-10, z=-7>\n" +
                "<x=4, y=-8, z=8>\n" +
                "<x=3, y=5, z=-1>"

        val moons = getMoons(input)
        val res = repeatsAfter(moons)
        assertEquals(2772, res)
    }

    @Test
    fun `should be repeated after 4686774924 steps`() {
        val input = "<x=-8, y=-10, z=0>\n" +
                "<x=5, y=5, z=10>\n" +
                "<x=2, y=-7, z=3>\n" +
                "<x=9, y=-8, z=-3>"

        val moons = getMoons(input)
        val res = repeatsAfter(moons)
        assertEquals(4686774924, res)
    }

}