import java.io.File
import kotlin.math.abs
import kotlin.math.atan2

fun main(){
    val map = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText()
    val astroids = getAstroids(map)
    val best = findBestCount(astroids)
    println(best)
}

fun get200Kill(astroids: MutableList<Astroid>): Int{
    val station = findBest(astroids)

    val s = astroids.filter { it.x == 13 && it.y == 13 }
    println(count(s[0], astroids))

    var kills = 0
    var angle = 0
    var killed: Astroid? = null
    while(kills < 200){
        //killed = kill(station, astroids, angle)
        if(killed != null){
            astroids.remove(killed)
            kills++
        }
        angle++
        if(angle > 360) angle = 0
    }

    return killed!!.x * 100 + killed.y
}

//fun kill(station: Astroid, astroids: List<Astroid>, angle: Int) = astroids.filter { angle(station, it) == angle }.minBy { abs(station.x + it.x) + abs(station.y + it.y) }

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

/*fun angle(a1: Astroid, a2: Astroid): Int{
    val degree = 90 - Math.toDegrees(atan2(a1.y.toDouble() - a2.y.toDouble(), a1.x.toDouble() - a2.x.toDouble())).toInt()
    return if(degree > 0) degree else degree + 360
}*/

fun angle(a1: Astroid, a2: Astroid) = atan2(a1.y.toDouble() - a2.y.toDouble(), a1.x.toDouble() - a2.x.toDouble())

data class Astroid(val x: Int, val y: Int)