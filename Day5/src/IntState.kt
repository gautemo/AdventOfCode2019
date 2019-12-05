import java.io.File

fun main(){
    val state = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText()
    val arr = toArr(state)
    transform(arr)
}

fun transform(start: List<Int>): List<Int>{
    val arr = start.toMutableList()
    var i = 0
    loop@ while (i < arr.size){
        val de = arr[i] % 100
        val c = arr[i] % 1000 / 100
        val b = arr[i] % 10000 / 1000
        val a = arr[i] % 100000 / 10000

        when(de){
            1 -> {
                val value = op(getVal(arr, i+1, c==0), getVal(arr, i+2, b==0), Int::plus)
                if(a == 0) arr[arr[i+3]] = value else arr[i+3] = value
                i+=4
            }
            2 -> {
                val value = op(getVal(arr, i+1, c==0), getVal(arr, i+2, b==0), Int::times)
                if(a == 0) arr[arr[i+3]] = value else arr[i+3] = value
                i+=4
            }
            3 -> {
                val part1 = false
                val input = if(part1) 1 else 5
                if(c == 0) arr[arr[i+1]] = input else arr[i+1] = input
                i+=2
            }
            4 -> {println(arr[arr[i+1]]); i+=2}
            5 -> {
                val param1 = getVal(arr, i+1, c==0)
                i = if(param1 != 0) getVal(arr, i+2, b==0) else i + 3
            }
            6 -> {
                val param1 = getVal(arr, i+1, c==0)
                i = if(param1 == 0) getVal(arr, i+2, b==0) else i + 3
            }
            7 -> {
                val isLess = getVal(arr, i+1, c==0) < getVal(arr, i+2, b==0)
                val value = if(isLess) 1 else 0
                if(a == 0) arr[arr[i+3]] = value else arr[i+3] = value
                i+=4
            }
            8 -> {
                val isEq = getVal(arr, i+1, c==0) == getVal(arr, i+2, b==0)
                val value = if(isEq) 1 else 0
                if(a == 0) arr[arr[i+3]] = value else arr[i+3] = value
                i+=4
            }
            99 -> break@loop
            else -> println("Should not happen!")
        }
    }
    return arr
}

fun getVal(arr: List<Int>, pos: Int, positional: Boolean): Int{
    return if(positional) arr[arr[pos]] else arr[pos]
}

fun op(val1: Int, val2: Int, op: (Int, Int) -> Int) = op(val1, val2)

fun toArr(line: String) = line.split(',').map { it.trim().toInt() }.toMutableList()

