import java.io.File

fun main(){
    val state = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText()
    println(boostCode(state, listOf(2)))
}

fun boostCode(start: String, input: List<Int>): Long{
    val booster = IntCode(start, input)

    while(!booster.done){
        booster.step()
    }
    return booster.lastOutput
}