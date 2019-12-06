import java.io.File

fun readLines() =  File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readLines()

fun main(){
    val spaceObjects = mapSpace(readLines())
    val orbits = findNrOrbits(spaceObjects)
    println(orbits)
    val toSanta = findNrOrbitsToSanta(spaceObjects)
    println(toSanta)
}

fun findNrOrbits(spaceObjects: Map<String, SpaceObject>) = countOrbits(spaceObjects)

fun findNrOrbitsToSanta(spaceObjects: Map<String, SpaceObject>): Int {
    val myLink = link("YOU", spaceObjects).split(",")
    val santaLink = link("SAN", spaceObjects).split(",")
    val firstCommon = myLink.first { santaLink.contains(it) }
    return myLink.indexOf(firstCommon) + santaLink.indexOf(firstCommon)
}

fun link(ob: String, objects: Map<String, SpaceObject>, soFar: String = ""): String{
    return if(objects.containsKey(ob) && objects[ob]!!.orbits != null) link(objects[ob]!!.orbits!!, objects, "$soFar,${objects[ob]!!.orbits}") else soFar.trim().trim(',')
}

fun mapSpace(objects: List<String>): Map<String, SpaceObject>{
    val spaceObjects = mutableMapOf<String, SpaceObject>()
    for(ob in objects){
        val rel = getObjects(ob)
        if(!spaceObjects.containsKey(rel.first)){
            spaceObjects[rel.first] = SpaceObject(null)
        }
        val smaller = SpaceObject(rel.first)
        spaceObjects[rel.second] = smaller
    }
    return spaceObjects
}

fun countOrbits(spaceObjects: Map<String, SpaceObject>): Int{
    var counter = 0
    for(ob in spaceObjects){
        counter += countOrbitsForObject(ob.value, spaceObjects)
    }
    return counter
}

fun countOrbitsForObject(ob: SpaceObject?, allObjects: Map<String, SpaceObject>):Int = if(ob?.orbits == null) 0 else 1 + countOrbitsForObject(allObjects[ob.orbits], allObjects)

fun getObjects(line: String): Pair<String, String>{
    val names = line.split(")")
    return Pair(names.first(), names.last())
}

data class SpaceObject(val orbits: String?)