import java.io.File

fun main(){
    val state = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText()
    play(state, false)
}

fun play(start: String, playmode: Boolean){
    val program = IntCode(start, listOf())

    val preSteps = pickUpAllGoToSecurityCheckpoint
    preSteps.forEach { ascii -> program.addInput(ascii.toInt()) }

    val possibilities = permute(items).map { itemPossibilities(it) }.flatten().toMutableList()

    while(!program.done){
        playUntilCommand(program)
        if(playmode) {
            val command = readLine()
            command?.forEach { ascii -> program.addInput(ascii.toInt()) }
            program.addInput(10)
        }else{
            val command = possibilities.removeAt(0)
            command.forEach { ascii -> program.addInput(ascii.toInt()) }
        }
    }
}

fun playUntilCommand(program: IntCode){
    var render = ""
    var play = true
    while(play){
        when(val output = program.step()){
            IntCode.NEED_INPUT -> play = false
            else -> if(output != null) render += output.toChar()
        }
    }
    println(render)
}

fun itemPossibilities(items: List<String>): List<String>{
    val commandSet = mutableListOf<String>()
    for(i in items.indices) {
        var commands = ""
        for (j in 0..i) {
            commands += "take ${items[j]}\n"
        }
        commands += "north\n"
        for (j in 0..i) {
            commands += "drop ${items[j]}\n"
        }
        commandSet.add(commands)
    }
    return commandSet
}

val items = listOf(
    "food ration",
    "fixed point",
    "weather machine",
    "semiconductor",
    "planetoid",
    "coin",
    "pointer",
    "klein bottle"
)

val pickUpAllGoToSecurityCheckpoint = """
        west
        take semiconductor
        west
        take planetoid
        west
        take food ration
        west
        take fixed point
        west
        take klein bottle
        east
        south
        west
        take weather machine
        east
        north
        east
        east
        south
        south
        south
        take pointer
        north
        north
        east
        take coin
        east
        north
        east
        drop food ration
        drop fixed point
        drop weather machine
        drop semiconductor
        drop planetoid
        drop coin
        drop pointer
        drop klein bottle
        
    """.trimIndent()

fun <T> permute(input: List<T>): List<List<T>> {
    if (input.size == 1) return listOf(input)
    val perms = mutableListOf<List<T>>()
    val toInsert = input[0]
    for (perm in permute(input.drop(1))) {
        for (i in 0..perm.size) {
            val newPerm = perm.toMutableList()
            newPerm.add(i, toInsert)
            perms.add(newPerm)
        }
    }
    return perms
}