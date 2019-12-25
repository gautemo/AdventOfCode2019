import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import kotlin.math.abs
import kotlin.system.measureTimeMillis


fun main(){
    val input = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText()

    println(bestOneMap(input))
    println(bestSplittedMap(input.trim())) // This is slow, just try the numbers on the way
}

fun bestOneMap(input: String): Int{
    val map = Map(input, listOf(Bot(input.indexOf('@'), '@')))
    val keyKnower = KeyKnower(map)
    val finder = Finder(map, keyKnower)

    return bestPath(finder)
}

fun bestSplittedMap(input: String): Int{
    val newMap = splitMap(input)
    val singlemaps = singleMaps(newMap).map { Map(it, listOf()) }.toTypedArray()
    val keyKnower = KeyKnower(*singlemaps)

    val bot1 = Bot(newMap.indexOf('@'), '@')
    val bot2 = Bot(newMap.indexOf('&'), '&')
    val bot3 = Bot(newMap.indexOf('$'), '$')
    val bot4 = Bot(newMap.indexOf('%'), '%')

    val finder = Finder(Map(newMap, listOf(bot1, bot2, bot3, bot4)), keyKnower)
    return bestPath(finder)
}

fun bestPath(finder: Finder): Int {
    finder.check()
    return finder.bestStep
}

class Finder(map: Map, val keyKnower: KeyKnower){
    private val history = History(map.map)
    var bestStep = Int.MAX_VALUE
    private val mutex = Mutex()

    init{
        addCanGo(map)
    }

    private fun addCanGo(map: Map, alreadySteps: Int = 0){
        map.bots.forEach {b ->
            val canGo = map.canGoToKeys(b, keyKnower).map { val m = map.copy(); ValidPath(m, m.go(b.place, it.first, true, true), alreadySteps + it.second) }.filter { it.steps < bestStep }
            history.toCheck.addAll(canGo)
        }
    }

    fun check(){
        while (history.toCheck.isNotEmpty()) {
            val t = measureTimeMillis {
                runBlocking {
                    val runOn = history.toCheck.sortedWith(compareBy { it.map.keysLeft.size }).take(5000)
                    history.toCheck.removeAll(runOn)
                    runOn.forEach { path ->
                        launch {
                            path.map.open(path.goesTo)
                            if (path.map.keysLeft.size == 0) {
                                mutex.withLock {
                                    bestStep = minOf(path.steps, bestStep)
                                }
                                println("Found all keys in ${path.steps}")
                            }
                            addCanGo(path.map, path.steps)
                        }
                    }
                }
            }
            //println("Batch took $t. toCheck: ${history.toCheck.size}. Best: $bestStep")
        }
    }
}

class History(initialMap: String){
    private val alreadyExistedOnStep = hashMapOf(initialMap to 0)
    var toCheck = mutableSetOf<ValidPath>()

    fun exists(map: String, steps: Int): Boolean{
        val state = alreadyExistedOnStep[map]
        return state != null && steps > state
    }

    fun addMap(map: String, steps: Int){
        alreadyExistedOnStep[map] = steps
    }
}

class Map(var map: String, val bots: List<Bot>){
    var keysLeft = map.filter { it.isKey() }.toMutableList()
    private val width = map.lines().first().length + 1

    constructor(map: String, bots: List<Bot>, keysLeft: MutableList<Char>) : this(map, bots) {
        this.keysLeft = keysLeft
    }

    fun open(key: Char){
        map = map.replace(key.toUpperCase(), '.')
        keysLeft.remove(key)
    }

    private fun on(key: Char) = map.indexOfFirst { it == key }

    fun go(from: Int, to: Int, changeLastKey: Boolean, markMap: Boolean = false): Char{
        val go = map[to]
        val bot = bots.first { it.place == from }
        bot.place = to

        if(markMap) {
            map = map.replace(from, '.').replace(to, '@')
        }
        if(changeLastKey){
            bot.lastKey = go
        }

        return go
    }

    fun canGoToKeys(bot: Bot, keysKnower: KeyKnower): List<Pair<Int, Int>>{
        return keysLeft
            .filter { val link = KeyKnower.keysToKey(bot.lastKey, it); keysKnower.keys.containsKey(link) && keysKnower.keys[link]!!.second.none { blocked -> keysLeft.contains(blocked) } &&  keysKnower.keys[link]!!.third.none { k -> keysLeft.contains(k) }}
            .map { Pair(on(it), keysKnower.keys[KeyKnower.keysToKey(bot.lastKey, it)]!!.first) }
    }

    fun canGo(bot: Bot): List<Int>{
        val up = bot.place - width
        val down = bot.place + width
        val left = bot.place-1
        val right = bot.place+1
        val adjecant = mutableListOf<Int>()

        if(up >= 0 && map[up].isWalkable()) adjecant.add(up)
        if(down < map.length && map[down].isWalkable()) adjecant.add(down)
        if(right < map.length && right / width == bot.place / width && map[right].isWalkable()) adjecant.add(right)
        if(left >= 0 && left / width == bot.place / width && map[left].isWalkable()) adjecant.add(left)

        return adjecant.toList()
    }

    fun dist(pos: Int, key: Char): Int{
        val on = on(key)
        val x = abs((pos % width) - (on % width))
        val y = abs((pos / width) - (on / width))
        return x + y
    }

    fun copy() =  Map(map, bots.map { it.copy() }, keysLeft.toMutableList())

    fun print(){
        println(map())
    }

    fun map(): String{
        var realMap = map.replace('@', '.')
        bots.forEach {
            realMap = realMap.replace(it.place, '@')
        }
       return realMap
    }
}

class KeyKnower(vararg maps: Map){
    val keys = hashMapOf<String, Triple<Int, List<Char>, List<Char>>>()

    init {
        val t = measureTimeMillis {
            maps.forEach { map ->
                val botSign = botSign(map.map)
                map.keysLeft.forEach { key1 ->
                    keys[keysToKey(botSign, key1)] = findKey(map.map,botSign, key1)
                    map.keysLeft.forEach { key2 ->
                        val hashKey = keysToKey(key1, key2)
                        if (key1 != key2 && !keys.containsKey(hashKey)) {
                            keys[hashKey] = findKey(map.map, key1, key2)
                        }
                    }
                }
            }
        }
        println("Map keys took ${(t / 1000) / 60}m")
    }

    private fun findKey(map: String, key1: Char, key2: Char): Triple<Int, List<Char>, List<Char>>{
        val botSign = botSign(map)
        var startAtKey = map
        if(key1 != botSign) {
            startAtKey = startAtKey.replace(botSign, '.').replace(key1, botSign)
        }
        val history = History(startAtKey)
        val useMap = Map(startAtKey, listOf(Bot(startAtKey.indexOf(botSign), botSign)))
        val startGo = useMap.canGo(useMap.bots.first()).map { val m = useMap.copy(); val dist = m.dist(it, key2); ValidPath(m, m.go(m.bots.first().place, it, true), 0, dist) }.filter { !history.exists(it.map.map(), 1) }
        history.toCheck.addAll(startGo)
        var bestStep = Int.MAX_VALUE
        while (history.toCheck.isNotEmpty()){
            val path = history.toCheck.sortedWith(compareBy(ValidPath::steps)).first()
            history.toCheck.remove(path)
            path.steps++
            if(path.goesTo == key2){
                bestStep = minOf(path.steps, bestStep)
                return Triple(path.steps, path.blockedByKey.toList(), path.keyOnWay.toList())
            }else{
                if(path.goesTo.isKey()){
                    path.keyOnWay.add(path.goesTo)
                }else if(path.goesTo.isDoor()){
                    path.blockedByKey.add(path.goesTo.toLowerCase())
                }
                val canGo = path.map.canGo(path.map.bots.first()).map { val m = path.map.copy(); val dist = m.dist(it, key2); ValidPath(m, m.go(m.bots.first().place, it, true), path.steps, dist, path.blockedByKey.toMutableList(), path.keyOnWay.toMutableList()) }.filter { !history.exists(it.map.map(), it.steps + 1) && it.steps + 1 < bestStep }
                history.toCheck.addAll(canGo)
            }
            history.addMap(path.map.map(), path.steps)
        }
        return Triple(-1, listOf(), listOf())
    }

    companion object{
        fun keysToKey(key1: Char, key2: Char): String{
            val hashKey = charArrayOf(key1, key2)
            hashKey.sort()
            return hashKey.joinToString()
        }
    }
}

data class ValidPath(val map: Map, val goesTo: Char, var steps: Int, val dist: Int = 0, val blockedByKey: MutableList<Char> = mutableListOf(), val keyOnWay: MutableList<Char> = mutableListOf())

fun Char.isKey() = this.isLowerCase()
fun Char.isDoor() = this.isUpperCase()
fun Char.isWalkable() = this != '#'
fun String.replace(pos: Int, c: Char) = this.substring(0, pos) + c + this.substring(pos + 1)

fun splitMap(map: String): String {
    val width = map.lines().first().length

    val at = map.indexOf('@')
    return map.replace(at, '#').replace(at - 1, '#').replace(at + 1, '#')
        .replace(at - (width+1), '#').replace(at + (width+1), '#')
        .replace(at + 1 - (width+1), '@').replace(at -1 - (width+1), '&')
        .replace(at + 1 + (width+1), '$').replace(at -1 + (width+1), '%')
}

fun singleMaps(map: String): List<String>{
    val width = map.lines().first().length
    val height = map.lines().size

    var map1 = ""
    var map2 = ""
    var map3 = ""
    var map4 = ""
    for ((i, line) in map.lines().withIndex()){
        if(i < height / 2){
            map1 += line.substring(0, width / 2)
            map2 += line.substring((width / 2)+1)
            map1 += "\n"
            map2 += "\n"
        }else if(i > height / 2){
            map3 += line.substring(0, width / 2)
            map4 += line.substring((width / 2)+1)
            map3 += "\n"
            map4 += "\n"
        }
    }
    return listOf(map1.trim(), map2.trim(), map3.trim(), map4.trim())
}

fun botSign(map: String): Char{
    return when{
        map.contains('@') -> '@'
        map.contains('&') -> '&'
        map.contains('$') -> '$'
        else -> '%'
    }
}

data class Bot(var place: Int, var lastKey: Char)