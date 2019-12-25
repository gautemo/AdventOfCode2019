import java.io.File
import java.lang.Exception
import kotlin.math.abs
import kotlin.math.hypot

fun main(){
    val input = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText()
    val result = shortest(input)
    println(result)

    val resultWithDigging = shortest(input, true)
    println(resultWithDigging)
}

fun shortest(map: String, dig: Boolean = false): Int {
    val nodes = nodes(map)
    val start = nodes.first { it.port == "AA" }
    val beenTo = mutableListOf<Pair<Point, Int>>()
    val canGo = mutableListOf(Step(start, 0, 0))

    while (true){
        val go = canGo.minBy { it.point.distToPort + it.step + it.level}!!
        if(go.point.port == "ZZ" && go.level == 0){
            return go.step
        }
        beenTo.add(Pair(go.point, go.level))
        canGo.remove(go)
        for(next in go.point.next){
            val blockedIfDigged = go.level != 0 && (next.port == "AA" || next.port == "ZZ")
            val blockedIfNotDigged = (dig && go.level == 0 && next.port.isNotEmpty() && isOuterPoint(go.point, map)) && !(next.port == "AA" || next.port == "ZZ")
            val toLevel = if(dig) getLevel(go, next, map) else go.level
            println(toLevel)
            if(!beenTo.contains(Pair(next, toLevel)) && !blockedIfDigged && !blockedIfNotDigged) {
                canGo.add(Step(next, go.step + 1, toLevel))
            }
        }
    }
}

fun getLevel(on: Step, next: Point, map: String): Int{
    return if(next.port.isNotEmpty() && !pointsAreNeighbour(on.point, next)){
        if(isOuterPoint(on.point, map)) {
            println("Goes out")
            on.level - 1
        }else{
            println("Goes in")
            on.level + 1
        }
    }else{
        on.level
    }
}

fun isOuterPoint(p: Point, map: String) = p.x == 2 || p.x == map.lines().first().length - 3 || p.y == 2 || p.y == map.length / map.lines().first().length - 3

fun distToPort(node: Point, nodes: List<Point>) = nodes.filter { it.port.isNotEmpty() }.map { hypot(abs(node.x - it.x).toDouble(), abs(node.y - it.y).toDouble()) }.min()

fun nodes(map: String): List<Point>{
    val nodes = mutableListOf<Point>()
    for(y in map.lines().indices){
        for(x in map.lines()[y].indices){
            if(map.lines()[y][x] == '.'){
                val point = Point(x,y, hasPort(x,y,map))
                val neighbors = nodes.filter { pointsAreNeighbour(it, point) }
                for (n in neighbors){
                    point.next.add(n)
                    n.next.add(point)
                }
                val portTo = nodes.firstOrNull { it.port.isNotEmpty() && it.port == point.port }
                if(portTo != null){
                    portTo.next.add(point)
                    point.next.add(portTo)
                }
                nodes.add(point)
            }
        }
    }
    for(n in nodes){
        n.distToPort = distToPort(n, nodes)!!
    }
    return nodes
}

fun hasPort(x: Int, y: Int, map: String): String{
    //To Left
    try {
        val first = map.lines()[y][x-2]
        val second = map.lines()[y][x-1]
        if(first.isUpperCase() && second.isUpperCase()){
            return first.toString() + second.toString()
        }
    }catch (e: Exception){}
    //To right
    try {
        val first = map.lines()[y][x+1]
        val second = map.lines()[y][x+2]
        if(first.isUpperCase() && second.isUpperCase()){
            return first.toString() + second.toString()
        }
    }catch (e: Exception){}
    //Up
    try {
        val first = map.lines()[y-2][x]
        val second = map.lines()[y-1][x]
        if(first.isUpperCase() && second.isUpperCase()){
            return first.toString() + second.toString()
        }
    }catch (e: Exception){}
    //Down
    try {
        val first = map.lines()[y+1][x]
        val second = map.lines()[y+2][x]
        if(first.isUpperCase() && second.isUpperCase()){
            return first.toString() + second.toString()
        }
    }catch (e: Exception){}
    return ""
}

class Point(val x: Int, val y: Int, val port: String){
    val next = mutableListOf<Point>()
    var distToPort: Double = Double.MAX_VALUE
}

class Step(val point: Point, val step: Int, val level: Int)

fun pointsAreNeighbour(p1: Point, p2: Point): Boolean{
    if(p1.y == p2.y){
        return p1.x - 1 == p2.x || p1.x + 1 == p2.x
    }
    if(p1.x == p2.x){
        return p1.y - 1 == p2.y || p1.y + 1 == p2.y
    }
    return false
}