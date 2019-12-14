import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.*

fun main(){
    val state = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText()
    countBlocks(state)
    runGame(state)
}

fun countBlocks(start: String){
    val program = IntCode(start, listOf())
    val outputs = getOutputs(program)
    val points = getPoints(outputs)
    println(points.count { it.tile == Tile.BLOCK })
}

fun runGame(start: String){
    val free = "2" + start.substring(1)
    var program = IntCode(free, listOf())
    var points = mutableListOf<Point>()

    val backup = Backup()

    val visualizer = GameVisualizer { i ->
        if (i == 99) {
            val b = backup.restart()
            program = b.first
            points = b.second
        } else {
            program.addInput(i)
        }
    }

    while (!program.done){
        val outputs = getOutputs(program)
        val newPoints = getPoints(outputs)
        newPoints.forEach {newP ->
            val existing = points.firstOrNull { it.x == newP.x && it.y == newP.y }
            points.remove(existing)
            points.add(newP)
        }
        visualizer.drawImage(points)

        val auto = compareValues(points.first { it.tile == Tile.BALL }.x, points.first { it.tile == Tile.PADDLE }.x)
        program.addInput(auto)

        backup.backup(program, points)

        while (program.inputs.size == 0){
            runBlocking{
                delay(5)
            }
        }
    }
}

fun getOutputs(program: IntCode): List<Long>{
    val outputs = mutableListOf<Long>()

    while(!program.done){
        val output = program.step()
        output?.let{
            if(it == IntCode.NEED_INPUT){
                return outputs
            }else{
                outputs.add(it)
            }
        }
    }
    return outputs
}

fun getPoints(outputs: List<Long>): List<Point>{
    return outputs.chunked(3).map {
        when(it[2]){
            0L -> Point(it[0], it[1], Tile.EMPTY)
            1L -> Point(it[0], it[1], Tile.WALL)
            2L -> Point(it[0], it[1], Tile.BLOCK)
            3L -> Point(it[0], it[1], Tile.PADDLE)
            4L -> Point(it[0], it[1], Tile.BALL)
            else -> Point(it[0], it[1], it[2])
        }
    }
}

enum class Tile { EMPTY, WALL, BLOCK, PADDLE, BALL}

data class Point(val x: Long, val y: Long, val tile: Any)

class Backup(){
    private var b = mutableListOf<Pair<IntCode, MutableList<Point>>>()

    fun backup(i: IntCode, p: MutableList<Point>){
        val gson = Gson()
        val copyI = gson.fromJson<IntCode>(gson.toJson(i), IntCode::class.java)
        val copyP = p.map { it.copy() }.toMutableList()
        b.add(Pair(copyI, copyP))
    }

    fun restart(): Pair<IntCode, MutableList<Point>>{
        b = b.dropLast( 10).toMutableList()
        return b.last()
    }
}