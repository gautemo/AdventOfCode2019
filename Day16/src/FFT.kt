import java.io.File
import kotlin.math.abs

fun main() {
    val input = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText().trim()
    val res = runFFT(input, 100)
    println(res)
    val res2 = runFFT(input, 100, 10000, 7)
    println(res2)
}

fun runFFT(input: String, phases: Int, repeated: Int = 1, offset: Int = 0): String {
    val basePattern = listOf(0, 1, 0, -1)
    val useOffset = if(offset == 0) 0 else input.take(offset).toInt()
    val intArr = input.repeat(repeated).drop(useOffset).map { it.toString().toInt() }.toMutableList()
    repeat(phases){
        val copy = intArr.toList()
        for(i in intArr.size - 1 downTo 0){
            val actualIndex = useOffset + i
            if(actualIndex <= input.length*repeated/2){
                val pattern = pattern(basePattern, actualIndex+1)
                intArr[i] = abs(copy.drop(i).mapIndexed { x, nr -> pattern[(x+actualIndex+1) % pattern.size] * nr }.sum() % 10)
            }else{
                intArr[i] = (intArr[i] + (intArr.elementAtOrNull(i+1) ?: 0)) % 10
            }
        }
    }

    return intArr.joinToString("").take(8)
}

fun pattern(pattern: List<Int>, pos: Int) = pattern.map { n-> List(pos){n} }.flatten()