import java.io.File
import kotlin.math.abs
import kotlin.system.measureTimeMillis

var keysKnower: KeyKnower? = null

fun main(){
    val input = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText()
    val res = path(input)
    println(res)
}

fun path(input: String): Int? {
    val map = Map(input)
    keysKnower = KeyKnower(map)
    return checkAll(map, History(map.map)).min()
}

fun checkAll(map: Map, history: History): List<Int>{
    val finalSteps = mutableListOf<Int>()
    val startGo = map.canGoToKeys('@').map { val m = map.copy(); ValidPath(m, m.go(it.first), it.second) }
    history.toCheck.addAll(startGo)
    while (history.toCheck.isNotEmpty()){
        val path = history.toCheck.sortedWith( compareBy({ it.map.keysLeft.size}, {it.steps })).first()
        history.toCheck.remove(path)
        history.addMap(path.map.map(), path.steps)

        path.map.open(path.goesTo)

        if(path.map.keysLeft.size == 0){
            finalSteps.add(path.steps)
            history.minStep = path.steps
            println("Found all keys in ${path.steps}")
        }

        val canGo = path.map.canGoToKeys(path.goesTo).map { val m = path.map.copy(); ValidPath(m, m.go(it.first), path.steps + it.second) }.filter { !history.exists(it.map.map(), it.steps) }
        history.toCheck.addAll(canGo)
    }
    return finalSteps
}

class History(initialMap: String){
    var minStep: Int = Int.MAX_VALUE
        set(value) {
            if(value < field){
                field = value
            }
        }
    private val alreadyExistedOnStep = hashMapOf(initialMap to 0)
    var toCheck = mutableListOf<ValidPath>()

    fun exists(map: String, steps: Int): Boolean{
        if(steps > minStep) return true
        val state = alreadyExistedOnStep[map]
        return state != null && steps > state
    }

    fun addMap(map: String, steps: Int){
        alreadyExistedOnStep[map] = steps
    }
}

class Map(var map: String){
    var keysLeft = map.filter { it.isKey() }.toMutableList()
    private val width = map.lines().first().length + 1
    var on = on('@')


    constructor(map: String, keysLeft: MutableList<Char>) : this(map) {
        this.keysLeft = keysLeft
    }

    fun open(key: Char){
        map = map.replace(key.toUpperCase(), '.')
        keysLeft.remove(key)
    }

    private fun on(key: Char) = map.indexOfFirst { it == key }

    fun go(place: Int, markMap: Boolean = false): Char{
        val char = map[place]
        on = place
        if(markMap) {
            map = map.replace('@', '.').replace(place, '@')
        }
        return char
    }

    fun canGoToKeys(lastKey: Char): List<Pair<Int, Int>>{
        return keysLeft
            .filter { keysKnower!!.keys[KeyKnower.keysToKey(lastKey, it)]!!.second.none { blocked -> keysLeft.contains(blocked) } }
            .map { Pair(on(it), keysKnower!!.keys[KeyKnower.keysToKey(lastKey, it)]!!.first) }
    }

    fun canGo(): List<Int>{
        val up = on - width
        val down = on + width
        val left = on-1
        val right = on+1
        val adjecant = mutableListOf<Int>()

        if(up >= 0 && map[up].isWalkable()) adjecant.add(up)
        if(down < map.length && map[down].isWalkable()) adjecant.add(down)
        if(right / width == on / width && map[right].isWalkable()) adjecant.add(right)
        if(left / width == on / width && map[left].isWalkable()) adjecant.add(left)

        return adjecant.toList()
    }

    fun dist(pos: Int, key: Char): Int{
        val on = on(key)
        val x = abs((pos % width) - (on % width))
        val y = abs((pos / width) - (on / width))
        return x + y
    }

    fun copy() =  Map(map, keysLeft.toMutableList())

    fun print(){
        println(map())
    }

    fun map() = map.replace('@', '.').replace(on, '@')
}

class KeyKnower(val map: Map){
    val keys = hashMapOf<String, Pair<Int, List<Char>>>()

    init {
        val t = measureTimeMillis {
            map.keysLeft.forEach { key1 ->
                keys[keysToKey('@', key1)] = findKey('@', key1)
                println("Keypath @ and $key1 added")
                map.keysLeft.forEach { key2 ->
                    val hashKey = keysToKey(key1, key2)
                    if (key1 != key2 && !keys.containsKey(hashKey)) {
                        keys[hashKey] = findKey(key1, key2)
                        println("Keypath $key1 and $key2 added")
                    }
                }
            }
        }
        println("Map keys took ${(t / 1000) / 60}m")
    }

    private fun findKey(key1: Char, key2: Char): Pair<Int, List<Char>>{
        var startAtKey = map.map
        if(key1 != '@') {
            startAtKey = startAtKey.replace('@', '.').replace(key1, '@')
        }
        val history = History(startAtKey)
        val useMap = Map(startAtKey)
        val possibleStepsToKey = mutableListOf<Pair<Int, List<Char>>>()
        val startGo = useMap.canGo().map { val m = useMap.copy(); val dist = m.dist(it, key2); ValidPath(m, m.go(it), 0, dist) }.filter { !history.exists(it.map.map(), 1) }
        history.toCheck.addAll(startGo)
        while (history.toCheck.isNotEmpty()){
            history.toCheck.sortBy { it.dist }
            val path = history.toCheck.removeAt(0)
            path.steps++
            if(path.goesTo == key2){
                history.minStep = path.steps
                return Pair(path.steps, path.blockedByKey.toList())
                //possibleStepsToKey.add(Pair(path.steps, path.blockedByKey.toList()))
            }else{
                if(path.goesTo.isDoor()){
                    path.blockedByKey.add(path.goesTo.toLowerCase())
                }
                val canGo = path.map.canGo().map { val m = path.map.copy(); val dist = m.dist(it, key2); ValidPath(m, m.go(it), path.steps, dist, path.blockedByKey.toMutableList()) }.filter { !history.exists(it.map.map(), it.steps + 1) }
                history.toCheck.addAll(canGo)
            }
            history.addMap(path.map.map(), path.steps)
        }
        return possibleStepsToKey.minBy { it.first }!!
    }

    companion object{
        fun keysToKey(key1: Char, key2: Char): String{
            val hashKey = charArrayOf(key1, key2)
            hashKey.sort()
            return hashKey.joinToString()
        }
    }
}

class ValidPath(val map: Map, val goesTo: Char, var steps: Int, val dist: Int = 0, val blockedByKey: MutableList<Char> = mutableListOf())

fun Char.isKey() = this.isLowerCase()
fun Char.isDoor() = this.isUpperCase()
fun Char.isWalkable() = this != '#'
fun String.replace(pos: Int, c: Char) = this.substring(0, pos) + c + this.substring(pos + 1)