import java.io.File
import kotlin.math.abs
import kotlinx.coroutines.*

fun main(){
    val input = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText().trim()
    val energy = energyAfterStep(getMoons(input), 1000)
    println(energy)
    val loop = repeatsAfter(getMoons(input))
    println(loop)
}

fun getMoons(input: String): List<Moon> {
    val moons = mutableListOf<Moon>()
    val reg = Regex("""-?\d+""")
    input.lines().forEach {
        val xyz = reg.findAll(it)
        val x = xyz.elementAt(0).value.toInt()
        val y = xyz.elementAt(1).value.toInt()
        val z = xyz.elementAt(2).value.toInt()
        moons.add(Moon(x, y, z))
    }
    return moons
}

fun energyAfterStep(moons: List<Moon>, steps: Int): Int {
    for(i in 0 until steps){
        doStep(moons)
    }
    return moons.sumBy { it.energy() }
}

fun repeatsAfter(moons: List<Moon>): Long{
    val start = moons.map { it.copy() }
    var counter = 1L
    do{
        doStep(moons)
        counter++
    }while (start != moons)
    return counter
}

fun doStep(moons: List<Moon>){
    moons.forEach { m -> moons.forEach { m.changeVel(it) } }
    moons.forEach { it.move() }
}

data class Moon(var x: Int, var y: Int, var z: Int){
    var velX = 0
    var velY = 0
    var velZ = 0

    fun changeVel(c: Moon){
        velX += compareValues(c.x, x)
        velY += compareValues(c.y, y)
        velZ += compareValues(c.z, z)
    }

    fun move(){
        x += velX
        y += velY
        z += velZ
    }

    fun energy() = (abs(x) + abs(y) + abs(z)) * (abs(velX) + abs(velY) + abs(velZ))
}