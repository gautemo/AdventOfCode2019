import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MonitoringStationKtTest {

    @Test
    fun `best should detect 8`(){
        val map = ".#..#\n" +
                ".....\n" +
                "#####\n" +
                "....#\n" +
                "...##"
        val astroids = getAstroids(map)
        val best = findBestCount(astroids)
        assertEquals(8, best)
    }

    @Test
    fun `best should detect 33`() {
        val map = "......#.#.\n" +
                "#..#.#....\n" +
                "..#######.\n" +
                ".#.#.###..\n" +
                ".#..#.....\n" +
                "..#....#.#\n" +
                "#..#....#.\n" +
                ".##.#..###\n" +
                "##...#..#.\n" +
                ".#....####"
        val astroids = getAstroids(map)
        val best = findBestCount(astroids)
        assertEquals(33, best)
    }

    @Test
    fun `200 deleted astroid gives 802`(){
        val map = ".#..##.###...#######\n" +
                "##.############..##.\n" +
                ".#.######.########.#\n" +
                ".###.#######.####.#.\n" +
                "#####.##.#.##.###.##\n" +
                "..#####..#.#########\n" +
                "####################\n" +
                "#.####....###.#.#.##\n" +
                "##.#################\n" +
                "#####.##.###..####..\n" +
                "..######..##.#######\n" +
                "####.##.####...##..#\n" +
                ".#####..#.######.###\n" +
                "##...#.##########...\n" +
                "#.##########.#######\n" +
                ".####.#.###.###.#.##\n" +
                "....##.##.###..#####\n" +
                ".#.#.###########.###\n" +
                "#.#.#.#####.####.###\n" +
                "###.##.####.##.#..##"
        val astroids = getAstroids(map).toMutableList()
        val res = get200Kill(astroids)
        assertEquals(802, res)
    }
}