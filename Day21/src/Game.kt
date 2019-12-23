import java.io.File

fun main(){
    val state = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText()
    println("PART 1:")
    walk(state)
    println("\nPART 2:")
    run(state)
}

fun walk(start: String){
    val program = IntCode(start, listOf())

    val input = """
        NOT C J
        NOT A T
        OR T J
        AND D J
        WALK
        
    """.trimIndent()

    input.forEach { ascii -> program.addInput(ascii.toInt()) }

    var render = ""

    while (!program.done){
        val output = program.step()
        if(output != null && output != IntCode.NEED_INPUT){
            println(output)
            render += output.toChar()
            println(render)
        }
    }
    println(render)
}

fun run(start: String){
    val program = IntCode(start, listOf())

    val input = """
        NOT A J
        NOT B T
        OR T J
        NOT C T
        OR T J
        AND D J
        NOT E T
        AND H T
        OR E T
        AND T J
        RUN
        
    """.trimIndent()

    input.forEach { ascii -> program.addInput(ascii.toInt()) }

    var render = ""

    while (!program.done){
        val output = program.step()
        if(output != null && output != IntCode.NEED_INPUT){
            println(output)
            render += output.toChar()
            println(render)
        }
    }
    println(render)
}