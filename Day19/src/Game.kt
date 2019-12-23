import java.io.File

fun main(){
    val state = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText()
    grid50(state)
    untilShipFits(state)
}

fun grid50(start: String){
    var count = 0L
    for(x in 0 until 50){
        for(y in 0 until 50){
            count += beamAt(start, x, y)
        }
    }
    println("Count: $count")
}

fun untilShipFits(start: String){
    var found = false
    var x = 0
    var y = 0
    var streakX = 0
    while (!found){
        when(beamAt(start, x, y)){
            0L -> {
                if(streakX != 0){
                    streakX = 0
                    y++
                    x = 0
                }else{
                    x++
                }

            }
            1L -> {
                streakX++
                if(streakX >= 100 && checkShip(start, x-99, y)){
                    println((x-99)*10000 + y)
                    found = true
                }
                x++
            }
        }
    }
}

fun checkShip(intcode: String, x: Int, atY: Int): Boolean{
    for(y in atY + 1 until atY + 100){
        if(beamAt(intcode, x, y) == 0L){
            return false
        }
    }
    return true
}

fun beamAt(intcode: String, x: Int, y: Int): Long{
    val program = IntCode(intcode, listOf(x,y))

    while (!program.done){
        val output = program.step()
        if(output != null) {
            return output
        }
    }
    return -1L
}
