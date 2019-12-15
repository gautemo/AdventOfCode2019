import java.util.*

class IntCode (start: String, input: List<Int>){
    val inputs = LinkedList<Int>()
    private val p = Program(start)
    private var i = 0
    private var relativeBase = 0L
    var done = false
    var lastOutput = -1L

    init {
        input.forEach { inputs.add(it) }
    }

    fun addInput(num: Int) = inputs.add(num)

    fun step(): Long?{
        val de = (p.get(i) % 100).toInt()
        val c = (p.get(i) % 1000 / 100).toInt()
        val b = (p.get(i) % 10000 / 1000).toInt()
        val a = (p.get(i) % 100000 / 10000).toInt()

        when(de){
            1 -> {
                val value = plus(p.get(getPointer(i+1, c)), p.get(getPointer(i+2, b)))
                p.set(getPointer(i+3, a), value)
                i+=4
            }
            2 -> {
                val value = times(p.get(getPointer(i+1, c)), p.get(getPointer(i+2, b)))
                p.set(getPointer(i+3, a), value)
                i+=4
            }
            3 -> {
                if(inputs.any()) {
                    p.set(getPointer(i+1, c), inputs.pop().toLong())
                    i += 2
                }else{
                    return NEED_INPUT
                }
            }
            4 -> { lastOutput = p.get(getPointer(i+1, c)); i+=2; return lastOutput}
            5 -> {
                val param1 = p.get(getPointer(i+1, c))
                i = if(param1 != 0L) p.get(getPointer(i+2, b)).toInt() else i + 3
            }
            6 -> {
                val param1 = p.get(getPointer(i+1, c))
                i = if(param1 == 0L) p.get(getPointer(i+2, b)).toInt() else i + 3
            }
            7 -> {
                val isLess = p.get(getPointer(i+1, c)) < p.get(getPointer(i+2, b))
                val value = if(isLess) 1L else 0L
                p.set(getPointer(i+3, a), value)
                i+=4
            }
            8 -> {
                val isEq = p.get(getPointer(i+1, c)) == p.get(getPointer( i+2, b))
                val value = if(isEq) 1L else 0L
                p.set(getPointer(i+3, a), value)
                i+=4
            }
            9 -> {
                val value = p.get(getPointer( i+1, c))
                relativeBase += value
                i += 2
            }
            99 -> {i = 0; done = true}
            else -> println("Should not happen!")
        }
        return null
    }

    private fun getPointer(pos: Int, param: Int): Long{
        return when(param){
            0 -> p.get(pos)
            1 -> pos.toLong()
            2 -> p.get(pos) + relativeBase
            else -> -1 //Error
        }
    }

    private fun plus(val1: Long, val2: Long) = val1 + val2

    private fun times(val1: Long, val2: Long) = val1 * val2

    companion object{
        const val NEED_INPUT = -999999995L
    }
}

class Program(line: String){
    private val map = line.trim().split(',').mapIndexed { index, s -> index to s.toLong() }.toMap().toMutableMap()

    fun get(i: Int) = map.getOrDefault(i, 0)

    fun get(i: Long) = map.getOrDefault(i.toInt(), 0)

    fun set(i: Int, value: Long) { map[i] = value }

    fun set(i: Long, value: Long) { map[i.toInt()] = value }
}