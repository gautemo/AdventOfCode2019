import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

fun main(){
    val state = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText()

    val computers = (0 until 50).map { Computer(state, it.toLong()) }

    var found255 = false
    while(!found255){
        for(comp in computers){
            comp.run()
            val pack = comp.pack()
            pack?.let { pack ->
                println("$pack, ${comp.adress}")
                if(pack.adress == 255L){
                    println(pack)
                    found255 = true
                }else {
                    computers.first { c -> c.adress == pack.adress }.receive(pack)
                }
            }
        }
    }
}

class Computer(code: String, val adress: Long){
    val intCode = IntCode(code, listOf(adress))
    val output = mutableListOf<Long>()

    fun run(){
        when (val out = intCode.step()) {
            IntCode.NEED_INPUT -> intCode.addInput(-1)
            else -> if (out != null) output.add(out)
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