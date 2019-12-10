import java.io.File
import java.lang.Math.pow
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

fun main(){
    val map = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText()
    val astroids = getAstroids(map)
    val best = findBestCount(astroids)
    println(best)
    val killed200 = get200Kill(astroids.toMutableList())
    println(killed200)
}

fun get200Kill(astroids: MutableList<Astroid>): Int{
    val station = findBest(astroids)

    val loop = astroids.map { angle(station, it) }.toSet().sortedDescending()

    var kills = 0
    var i = 0
    var killed: Astroid? = null
    while(kills < 200){
        val angle = loop[i]
        killed = kill(station, astroids, angle)
        if(killed != null){
            astroids.remove(killed)
            kills++
        }
        i++
        if(i >= loop.size) i = 0
    }

    return killed!!.x * 100 + killed.y
}


fun kill(station: Astroid, astroids: List<Astroid>, angle: Double) = astroids.filter { angle(station, it) == angle }.minBy { distance(station, it) }

fun distance(a1: Astroid, a2: Astroid) = sqrt(pow(abs(a1.x.toDouble() - a2.x.toDouble()), 2.0) + pow(abs(a1.y.toDouble() - a2.y.toDouble()), 2.0))

fun getAstroids(map: String): List<Astroid>{
    val astroids = mutableListOf<Astroid>()
    map.lines().forEachIndexed { y, xLine -> xLine.forEachIndexed { x, c -> if(c == '#') astroids.add(Astroid(x, y)) } }
    return astroids
}

fun findBestCount(astroids: List<Astroid>) = astroids.map { count(it, astroids) }.max()!!

fun findBest(astroids: List<Astroid>) = astroids.maxBy { count(it, astroids) }!!

fun count(a: Astroid, astroids: List<Astroid>): Int{
    val hits = mutableSetOf<Double>()
    astroids.forEach {
        if(a != it){
            hits.add(angle(a, it))
        }
    }
    return hits.size
}


fun angle(a1: Astroid, a2: Astroid): Double{
    val degree = 90 - Math.toDegrees(atan2(a1.y.toDouble() - a2.y.toDouble(), a1.x.toDouble() - a2.x.toDouble())) //+90 to point to north
    return if(degree > 0) degree else degree + 360
}

data class Astroid(val x: Int, val y: Int)