import java.io.File
import kotlin.math.pow

fun main(){
    val input = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText()
    val result = findRepeatedBiodiversity(input)
    println(result)

    val spaceResult = findBugsSpaceFold(input, 200)
    println(spaceResult)
}

fun findBugsSpaceFold(map: String, minutes: Int): Int {
    val spaceFold = List(minutes + 10){ emptyTiles() }.toMutableList()
    val init = mapToTiles(map).toMutableList()
    init.removeAll { it.x == 2 && it.y == 2 }
    spaceFold[minutes/2 + 5] = init

    for(m in 0 until minutes){
        println(m)
        val copy = spaceFold.map { it.map { t -> t.copy() } }
        for((i, tiles) in spaceFold.withIndex()){
            tick(tiles, copy[i], copy.getOrNull(i-1), copy.getOrNull(i+1))
            //print(tiles)
        }
    }
    return spaceFold.map { it.count { t -> t.bug } }.sum()
}

fun mapToTiles(map: String): List<Tile>{
    val width = map.lines().first().length
    return map.filter { it != '\n' }.mapIndexed { index, c ->
        val x = index % width
        val y = index / width
        Tile(x, y, c == '#', index)
    }
}

fun emptyTiles(): List<Tile>{
    val tiles = mutableListOf<Tile>()
    for(y in 0 until 5){
        for(x in 0 until 5){
            if(x == 2 && y == 2){
                continue
            }
            tiles.add(Tile(x, y, false, 0))
        }
    }
    return tiles
}

fun findRepeatedBiodiversity(map: String): Int {
    val tiles = mapToTiles(map)

    val prevStates = mutableListOf(listOf<Tile>())
    while(true){
        val copy = tiles.map { it.copy() }
        tick(tiles, copy, null, null)
        //print(tiles)
        if(prevStates.contains(tiles)){
            return tiles.filter { it.bug }.sumBy { it.biodiversity.toInt() }
        }
        prevStates.add(tiles.map { it.copy() })
    }
}

fun tick(tiles: List<Tile>, copy: List<Tile>, around: List<Tile>?, inside: List<Tile>?){
    for(tile in tiles){
        //Same tiles
        var bugsAround = copy.filter { it.bug && tilesAreNeighbour(tile, it) }.count()

        //Around layer
        if(tile.x == 0){
            bugsAround += if(around?.first { it.x == 1 && it.y == 2 }?.bug == true) 1 else 0
        }
        if(tile.x == 4){
            bugsAround += if(around?.first { it.x == 3 && it.y == 2 }?.bug == true) 1 else 0
        }
        if(tile.y == 0){
            bugsAround += if(around?.first { it.x == 2 && it.y == 1 }?.bug == true) 1 else 0
        }
        if(tile.y == 4){
            bugsAround += if(around?.first { it.x == 2 && it.y == 3 }?.bug == true) 1 else 0
        }

        //Inside layer
        if(tile.x == 3 && tile.y == 2){
            bugsAround += inside?.filter { it.bug &&  it.x == 4 }?.count() ?: 0
        }
        if(tile.x == 1 && tile.y == 2){
            bugsAround += inside?.filter { it.bug &&  it.x == 0 }?.count() ?: 0
        }
        if(tile.x == 2 && tile.y == 3){
            bugsAround += inside?.filter { it.bug &&  it.y == 4 }?.count() ?: 0
        }
        if(tile.x == 2 && tile.y == 1){
            bugsAround += inside?.filter { it.bug &&  it.y == 0 }?.count() ?: 0
        }

        when{
            tile.bug && bugsAround != 1 -> tile.bug = false
            !tile.bug && bugsAround in (1..2) -> tile.bug = true
        }
    }
}

data class Tile(val x: Int, val y: Int, var bug: Boolean, val tileNr: Int){
    val biodiversity = 2.0.pow(tileNr)
}

fun tilesAreNeighbour(p1: Tile, p2: Tile): Boolean{
    if(p1.y == p2.y){
        return p1.x - 1 == p2.x || p1.x + 1 == p2.x
    }
    if(p1.x == p2.x){
        return p1.y - 1 == p2.y || p1.y + 1 == p2.y
    }
    return false
}

fun print(tiles: List<Tile>){
    var map = ""
    var y = 0
    for(t in tiles){
        if(t.y != y){
            y++
            map += '\n'
        }
        map += if(t.bug) '#' else '.'
    }
    println(map)
    println()
}