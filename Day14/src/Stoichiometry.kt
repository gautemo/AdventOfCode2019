import java.io.File

fun main(){
    val input = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText().trim()
    val ore = oreForFuel(input)
    println(ore)

    val fuelForTrillion = fuelForOre(input, 1000000000000)
    println(fuelForTrillion)
}

fun fuelForOre(input: String, maxOre: Long): Int{
    val reactions = input.lines().map { Reaction(it) }

    val fuel = reactions.first { it.output.name == "FUEL" }.output
    val ore = Material("0 ORE")
    val needs = mutableListOf(fuel, ore)
    var fuelUp = 0
    while (ore.nr < maxOre){
        fuelUp++
        fuel.nr = 1
        untilOnlyOreNeeded(reactions, needs)
        if(fuelUp % 1000000 == 0) println("Ore: ${ore.nr}, ${ore.nr * 100 / 1000000000000}%")
    }

    return fuelUp - 1
}

fun oreForFuel(input: String): Long {
    val reactions = input.lines().map { Reaction(it) }

    val needs = mutableListOf(reactions.first { it.output.name == "FUEL" }.output.copy())
    untilOnlyOreNeeded(reactions, needs)

    return needs.first { it.name == "ORE" }.nr
}

fun untilOnlyOreNeeded(reactions: List<Reaction>, needs: MutableList<Material>){
    while(needs.any { it.name != "ORE" && it.nr > 0 }){
        handleNextReaction(reactions, needs)
    }
}

fun handleNextReaction(reactions: List<Reaction>, needs: MutableList<Material>){
    val next = needs.first { it.name != "ORE" && it.nr > 0 }
    val reactionToNext = reactions.first { it.output.name == next.name }
    next.nr -= reactionToNext.output.nr
    for(r in reactionToNext.inputs){
        val alreadyNeeded = needs.firstOrNull { n -> n.name == r.name }
        if(alreadyNeeded != null){
            alreadyNeeded.nr += r.nr
        }else{
            needs.add(r.copy())
        }
    }
}

class Reaction(input: String){
    val output: Material
    val inputs: List<Material>

    init{
        val s = input.trim().split("=>")
        output = Material(s[1])

        val inputS = s[0].split(",")
        inputs = inputS.map { Material(it) }
    }
}

data class Material(val input: String){
    var nr: Long
    val name: String

    init{
        val s = input.trim().split(" ")
        nr = s[0].toLong()
        name = s[1]
    }
}