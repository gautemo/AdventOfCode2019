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
    fun `best should detect 210`(){
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
        val astroids = getAstroids(map)
        val best = findBestCount(astroids)
        assertEquals(210, best)
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

    @Test
    fun `same angle`(){
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
        val station = astroids.first { it.x == 11 && it.y == 13 }
        val above1 = astroids.first { it.x == 11 && it.y == 12 }
        val above2 = astroids.first { it.x == 11 && it.y == 1 }
        val angle1 = angle(station, above1)
        val angle2 = angle(station, above2)
        assertEquals(angle1, angle2)
    }

    @Test
    fun `closest should be closest`(){
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
        val station = astroids.first { it.x == 11 && it.y == 13 }
        val above1 = astroids.first { it.x == 11 && it.y == 12 }
        val above2 = astroids.first { it.x == 11 && it.y == 1 }
        val dist1 = distance(station, above1)
        val dist2 = distance(station, above2)

        assert(dist1 < dist2)
    }
}