import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.*

fun main(){
    val state = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText()
    runGame(state)
}

fun runGame(start: String){
    val program = IntCode(start, listOf())
    val droid = Point(50, 50, Tile.DROID)
    val startPoint = Point(50, 50, Tile.START)
    val tiles = mutableSetOf<Point>()
    var movesX = 0
    var movesY = 0

    var randomMode = true
    var filled = 0

    var visualizer: GameVisualizer? = null

    val fillOxygen: () -> Unit = {
        tiles.filter { it.tile == Tile.OXYGEN }.forEach {o ->
            tiles.filter { it.tile == Tile.EMPTY && pointsAreNeighbour(it, o)}.forEach { it.tile = Tile.OXYGEN }
            visualizer?.drawImage(tiles.toList())
        }
        filled++
        println(filled)
    }

    val move: (Int) -> Unit = move@{ i ->
        when(i){
            1 -> { movesY = -1; movesX = 0 }
            2 -> { movesY = 1; movesX = 0 }
            3 -> { movesY = 0; movesX = -1 }
            4 -> { movesY = 0; movesX = 1 }
            99 -> { randomMode = !randomMode; return@move }
            101 -> { randomMode = false; fillOxygen(); return@move }
        }
        program.addInput(i)
    }

    visualizer = GameVisualizer(move)

    loop@ while (!program.done){
        when(program.step()){
            0L -> tiles.add(Point(droid.x + movesX, droid.y + movesY, Tile.WALL))
            1L -> {
                tiles.add(Point(droid.x + movesX, droid.y + movesY, Tile.EMPTY))
                droid.x += movesX
                droid.y += movesY
            }
            2L -> {
                tiles.add(Point(droid.x + movesX, droid.y + movesY, Tile.OXYGEN))
                droid.x += movesX
                droid.y += movesY
            }
            IntCode.NEED_INPUT -> {
                if(randomMode){
                    move(Random().nextInt(4) + 1)
                }
                continue@loop
            }
        }

        visualizer.drawImage(tiles.toList() + startPoint + droid)
    }
    println("Done")
}

enum class Tile { EMPTY, WALL, OXYGEN, DROID, START}

data class Point(var x: Long, var y: Long, var tile: Tile)

fun pointsAreNeighbour(p1: Point, p2: Point): Boolean{
    if(p1.y == p2.y){
        return p1.x - 1 == p2.x || p1.x + 1 == p2.x
    }
    if(p1.x == p2.x){
        return p1.y - 1 == p2.y || p1.y + 1 == p2.y
    }
    return false
}