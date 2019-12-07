import java.util.*

class Amplifier (start: List<Int>, input: List<Int>){
    private val inputs = LinkedList<Int>()
    private val arr = start.toMutableList()
    private var i = 0
    var done = false
    var sendTo: Amplifier? = null
    var lastOutput = -1

    constructor(start: List<Int>, input: List<Int>, next: Amplifier) : this(start, input){
        sendTo = next
    }

    init {
        input.forEach { inputs.add(it) }
    }

    fun addInput(num: Int) = inputs.add(num)

    fun step(): Int?{
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
                if(inputs.any()) {
                    if (c == 0) arr[arr[i + 1]] = inputs.pop() else arr[i + 1] = inputs.pop()
                    i += 2
                }
            }
            4 -> { lastOutput = arr[arr[i+1]]; i+=2; return lastOutput}
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
            99 -> {i = 0; done = true}
            else -> println("Should not happen!")
        }
        return null
    }

    private fun getVal(arr: List<Int>, pos: Int, positional: Boolean): Int{
        return if(positional) arr[arr[pos]] else arr[pos]
    }

    private fun op(val1: Int, val2: Int, op: (Int, Int) -> Int) = op(val1, val2)
}