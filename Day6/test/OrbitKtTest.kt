import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class OrbitKtTest {

    @Test
    fun `orbits should be 42`() {
        val objects = listOf("COM)B", "B)C", "C)D" ,"D)E" ,"E)F" ,"B)G" ,"G)H" ,"D)I" ,"E)J" ,"J)K" ,"K)L")
        val spaceObjects = mapSpace(objects)

        val nrOrbits = findNrOrbits(spaceObjects)

        assertEquals(42, nrOrbits)
    }

    @Test
    fun `4 orbits to santa`(){
        val objects = listOf("COM)B", "B)C", "C)D" ,"D)E" ,"E)F" ,"B)G" ,"G)H" ,"D)I" ,"E)J" ,"J)K" ,"K)L" ,"K)YOU" ,"I)SAN")
        val spaceObjects = mapSpace(objects)

        val nrOrbits = findNrOrbitsToSanta(spaceObjects)

        assertEquals(4, nrOrbits)
    }
}