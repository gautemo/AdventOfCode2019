import java.io.File

fun main(){
    val masses = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readLines()
    val fuelNeeded = sumOfFuel(masses)
    println(fuelNeeded)
    val fuelNeededRecursively = sumOfFuelRecursively(masses)
    println(fuelNeededRecursively)
}

fun sumOfFuel(masses: List<String>) = masses.sumBy { calculateFuel(it.toInt()) }

fun calculateFuel(mass: Int) = mass / 3 - 2

fun sumOfFuelRecursively(masses: List<String>) = masses.sumBy { calculateFuelRecursively(it.toInt()) }

fun calculateFuelRecursively(module: Int): Int{
    val fuelNeeded = (module / 3 - 2).coerceAtLeast(0)
    return when{
        fuelNeeded <= 0 -> 0
        else -> fuelNeeded + calculateFuelRecursively(fuelNeeded)
    }
}