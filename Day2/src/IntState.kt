import java.io.File

fun main(){
    val state = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText()
    val arr = toArr(state)
    part1(arr)
    part2(arr)
}

fun part1(start: List<Int>){
    val arr = start.toMutableList()
    arr[1] = 12
    arr[2] = 2
    val output = outCode(arr)
    println("value at position 0 is $output")
}

fun part2(arr: List<Int>){
    val answer = brute(arr)
    println(answer)
}

fun brute(start: List<Int>): Int{
    val correct = 19690720

    for(i in 0..99){
        for(j in 0..99){
            val arr = start.toMutableList()
            arr[1] = i
            arr[2] = j
            if(outCode(arr) == correct){
                return 100 * i + j
            }
        }
    }
    return -1
}

fun outCode(arr: List<Int>) = transform(arr)[0]

fun transform(start: List<Int>): List<Int>{
    val arr = start.toMutableList()
    loop@ for(i in arr.indices step 4){
        when(arr[i]){
            1 -> arr[arr[i + 3]] = arr[arr[i + 1]] + arr[arr[i + 2]]
            2 -> arr[arr[i + 3]] = arr[arr[i + 1]] * arr[arr[i + 2]]
            99 -> break@loop
            else -> println("Should not happen!")
        }
    }
    return arr
}

fun toArr(line: String) = line.split(',').map { it.trim().toInt() }.toMutableList()

