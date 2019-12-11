import java.io.File

fun main(){
    val state = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText()
    println(runRobot(state, listOf(1)))
}

fun runRobot(start: String, input: List<Int>): Int{
    val program = IntCode(start, input)
    val myRobot = Robot()

    while(!program.done){
        val output = program.step()
        output?.let {
            println(it)
            val newInput = myRobot.doSomething(it.toInt())
            newInput?.let { i -> program.addInput(i) }
        }
    }

    SpaceImage(myRobot.panels).drawImage()

    return myRobot.panels.size
}

enum class Direction(val dir: Int){ UP(0), RIGHT(1), DOWN(2), LEFT(3) }

class Robot {
    private var x = 0
    private var y = 0
    private var dir = 0
    val panels = hashMapOf<Panel, Int>()
    private var paintMode = true

    fun doSomething(instruction: Int): Int?{
        var addInput: Int? = null
        if(paintMode){
            paint(instruction)
        }else{
            move(instruction)
            addInput = if(panels.containsKey(Panel(x,y))) panels[Panel(x,y)] else 0
        }
        paintMode = !paintMode
        return addInput
    }

    private fun move(turn: Int){
        dir += if(turn == 0) -1 else 1
        if(dir == -1) dir = 3
        if(dir == 4) dir = 0
        when(dir){
            Direction.UP.dir -> y--
            Direction.RIGHT.dir -> x++
            Direction.DOWN.dir -> y++
            Direction.LEFT.dir -> x--
        }
    }

    private fun paint(color: Int){
        panels[Panel(x,y)] = color
    }
}

data class Panel(val x: Int, val y: Int)