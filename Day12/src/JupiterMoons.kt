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
    val x = moons.map { Axis(it.x) }
    val y = moons.map { Axis(it.y) }
    val z = moons.map { Axis(it.z) }

    var repeat = -1L
    runBlocking {

        val xRepeat = async { repeats(x) }
        val yRepeat = async { repeats(y) }
        val zRepeat = async { repeats(z) }

        repeat = lcm(lcm(xRepeat.await(), yRepeat.await()), zRepeat.await())
    }
    return repeat
}

fun repeats(a: List<Axis>): Long{
    val start = a.map { it.copy() }

    var counter = 1L
    do{
        doStep(a)
        counter++
    }while(start != a)
    return counter
}

fun doStep(moveables: List<Moveable>){
    moveables.forEach { m -> moveables.forEach { m.changeVel(it) } }
    moveables.forEach { it.move() }
}

abstract class Moveable{
    abstract fun changeVel(c: Any)
    abstract fun move()
}

data class Moon(var x: Int, var y: Int, var z: Int) : Moveable(){
    var velX = 0
    var velY = 0
    var velZ = 0

    override fun changeVel(c: Any){
        c as Moon
        velX += compareValues(c.x, x)
        velY += compareValues(c.y, y)
        velZ += compareValues(c.z, z)
    }

    override fun move(){
        x += velX
        y += velY
        z += velZ
    }

    fun energy() = (abs(x) + abs(y) + abs(z)) * (abs(velX) + abs(velY) + abs(velZ))
}

data class Axis(var pos: Int) : Moveable(){
    var vel = 0

    override fun changeVel(c: Any){
        c as Axis
        vel += compareValues(c.pos, pos)
    }

    override fun move(){
        pos += vel
    }
}

fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b