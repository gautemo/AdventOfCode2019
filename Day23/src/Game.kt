import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

fun main(){
    val state = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText()

    val computers = (0 until 50).map { Computer(state, it.toLong()) }
    var packAtNat: Pack? = null

    var lastYFromNat: Long? = null
    var shouldStop = false

    while(!shouldStop){
        var idle = true
        for(comp in computers){
            val isIdle = comp.run()
            if(!isIdle) idle = false
            val pack = comp.pack()
            pack?.let { pack ->
                println(pack)
                if(pack.adress == 255L){
                    println(pack)

                    packAtNat = pack
                }else {
                    computers.first { c -> c.adress == pack.adress }.receive(pack)
                }
            }
        }
        if(idle && packAtNat != null){
            if(lastYFromNat == packAtNat!!.y){
                shouldStop = true
                println(lastYFromNat)
            }else{
                lastYFromNat = packAtNat!!.y
                computers.first { c -> c.adress == 0L }.receive(packAtNat!!)
            }
        }
    }
}

class Computer(code: String, val adress: Long){
    val intCode = IntCode(code, listOf(adress))
    val output = mutableListOf<Long>()

    fun run(): Boolean{
        while (true) {
            when (val out = intCode.step()) {
                IntCode.NEED_INPUT -> {
                    intCode.addInput(-1); return true
                }
                else -> if (out != null) { output.add(out); return false }
            }
        }
    }

    fun pack(): Pack?{
        if(output.size >= 3){
            return Pack(output.removeAt(0), output.removeAt(0), output.removeAt(0))
        }
        return null
    }

    fun receive(pack: Pack){
        intCode.addInput(pack.x)
        intCode.addInput(pack.y)
    }
}

data class Pack(val adress: Long, val x: Long, val y: Long)