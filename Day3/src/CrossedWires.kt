import java.io.File
import kotlin.math.abs

fun readLines() = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readLines()

fun main(){
    val wires = readLines()
    val path1 = path(wires[0])
    val path2 = path(wires[1])
    val closest = closestIntersect(path1, path2)
    println(closest)
    val steps = leastSteps(path1, path2)
    println(steps)
}

fun closestIntersect(path1: List<Point>, path2: List<Point>): Int? {
    return path1.filter { path2.contains(it) && !it.startPoint() }.map { abs(it.x) + abs(it.y) }.min()
}

fun leastSteps(path1: List<Point>, path2: List<Point>): Int? {
    val intersects = path1.mapIndexed { i1, p1 -> Pair(i1, p1) }.filter { path2.contains(it.second) && !it.second.startPoint()}

    return intersects.map { path2.mapIndexed { i2, p2 -> Pair(i2, p2) }.filter { pair -> pair.second == it.second }.map { pair -> pair.first }.min()!! + it.first }.min()
}

fun path(wire: String): List<Point>{
    val moves = wire.split(',')
    val path = mutableListOf(Point(0,0))
    for(move in moves){
        when(move[0]){
            'R' -> move(path, 1, 0, move.substring(1).toInt())
            'L' -> move(path, -1, 0, move.substring(1).toInt())
            'U' -> move(path, 0, 1, move.substring(1).toInt())
            'D' -> move(path, 0, -1, move.substring(1).toInt())
        }
    }
    return path
}

fun move(path: MutableList<Point>, xDir: Int, yDir: Int, moves: Int){
    for(i in 1..moves){
        path.add(Point(path.last().x + xDir, path.last().y + yDir))
    }
}

data class Point(val x: Int, val y: Int){
    fun startPoint() = x == 0 && y == 0
}