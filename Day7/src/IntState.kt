import java.io.File

fun main(){
    val state = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText()
    val arr = toArr(state)
    println(maxThrust(arr, listOf(0,1,2,3,4)))
    println(maxThrust(arr, listOf(5,6,7,8,9)))
}

fun maxThrust(start: List<Int>, settings: List<Int>): Int?{
    val permutations = permute(settings)
    return permutations.map { thrust(start, it) }.max()
}

fun thrust(start: List<Int>, settings: List<Int>): Int{
    val e = Amplifier(start, settings.subList(4,5))
    val d = Amplifier(start, settings.subList(3,4), e)
    val c = Amplifier(start, settings.subList(2,3), d)
    val b = Amplifier(start, settings.subList(1,2), c)
    val a = Amplifier(start, settings.subList(0,1) + listOf(0), b)
    e.sendTo = a

    val amplifiers = listOf(a,b,c,d,e)

    while(!e.done){
        amplifiers.forEach {
            val output = it.step()
            output?.let { o ->
                it.sendTo?.addInput(o)
            }
        }
    }
    return e.lastOutput
}

fun toArr(line: String) = line.split(',').map { it.trim().toInt() }.toMutableList()

fun <T> permute(input: List<T>): List<List<T>> {
    if (input.size == 1) return listOf(input)
    val perms = mutableListOf<List<T>>()
    val toInsert = input[0]
    for (perm in permute(input.drop(1))) {
        for (i in 0..perm.size) {
            val newPerm = perm.toMutableList()
            newPerm.add(i, toInsert)
            perms.add(newPerm)
        }
    }
    return perms
}

