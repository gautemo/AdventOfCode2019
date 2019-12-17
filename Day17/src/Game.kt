import java.io.File

fun main(){
    val state = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText()
    runGame(state)
}

fun runGame(start: String){
    val input = "2" + start.substring(1)
    val program = IntCode(input, listOf())
    val tiles = mutableSetOf<Point>()

    var visualizer: GameVisualizer? = null

    val move: (Int) -> Unit = move@{ i ->
        val droid = tiles.first { it.tile == Tile.DROID }
        when(i){
            1 -> droid.y--
            2 -> droid.y++
            3 -> droid.x--
            4 -> droid.x++
        }
        visualizer?.drawImage(tiles.toList())
    }

    visualizer = GameVisualizer(move)

    var x = 0L
    var y = 0L
    loop@ while (!program.done){
        when(val output = program.step()){
            35L -> { tiles.add(Point(x, y, Tile.SCAFFOLD)); x++ }
            46L -> { tiles.add(Point(x, y, Tile.SPACE)); x++ }
            10L -> { x = 0; y++ }
            94L -> { tiles.add(Point(x, y, Tile.DROID)); x++ }
            IntCode.NEED_INPUT -> {
                val movements = calculateMovements(tiles.toList())
                movements.forEach { it.forEach { ascii -> program.addInput(ascii.toInt()) }; program.addInput(10) }
                continue@loop
            }
            else -> if(output != null) { println(output); x++ }

        }

        visualizer.drawImage(tiles.toList())
    }
    println(sumOfAlignments(tiles.toList()))
}

fun calculateMovements(tiles: List<Point>): List<String>{
    val scaffolds = tiles.filter { it.tile == Tile.SCAFFOLD }
    val intersects = intersects(scaffolds)
    val start = tiles.first { it.tile == Tile.DROID }
    val go = mutableListOf(start)
    var canGo = true
    var dir = "U"
    val backForth: (Point) -> Boolean = { p -> (go.size >= 2 && go[go.size-2] == p) }
    val isInterectCanGoTo: (Point) -> Boolean = { p -> intersects.contains(p) && !backForth(p) }

    var moves = ""
    var straightMoves = 1

    while(canGo){
        val last = go.last()
        val straight = when(dir){
            "U" -> scaffolds.firstOrNull { it.x == last.x && it.y == last.y-1 }
            "R" -> scaffolds.firstOrNull { it.x == last.x+1 && it.y == last.y }
            "L" -> scaffolds.firstOrNull { it.x == last.x-1 && it.y == last.y }
            "D" -> scaffolds.firstOrNull { it.x == last.x && it.y == last.y+1 }
            else -> null
        }
        if(straight != null){
            go.add(straight)
            straightMoves++
            continue
        }

        val goTo = scaffolds.firstOrNull { (isInterectCanGoTo(it) || !go.contains(it)) && pointsAreNeighbour(last, it) }
        if(goTo != null){
            go.add(goTo)
            val newdir = when{
                goTo.x > last.x -> "R"
                goTo.x < last.x -> "L"
                goTo.y > last.y -> "D"
                goTo.y < last.y -> "U"
                else -> "WRONG"
            }
            if(straightMoves > 1) {
                val turn = when{
                    dir == "U" && newdir == "R" -> "R"
                    dir == "R" && newdir == "D" -> "R"
                    dir == "D" && newdir == "L" -> "R"
                    dir == "L" && newdir == "U" -> "R"

                    dir == "U" && newdir == "L" -> "L"
                    dir == "R" && newdir == "U" -> "L"
                    dir == "D" && newdir == "R" -> "L"
                    dir == "L" && newdir == "D" -> "L"

                    else -> "wrong"
                }
                moves += "$straightMoves,$turn,"
                straightMoves = 1
            }
            dir = newdir
        }else{
            moves += "$straightMoves"
            canGo = false
        }
    }

    println("Solve manually based on moves and put routine and functions in the code below:")
    println(moves)
    return listOf("A,A,B,C,B,C,B,C,B,A", "R,10,L,12,R,6", "R,6,R,10,R,12,R,6", "R,10,L,12,L,12","n")
}

fun sumOfAlignments(tiles: List<Point>) = intersects(tiles).sumBy { it.x.toInt() * it.y.toInt() }

fun intersects(tiles: List<Point>): List<Point>{
    val scaffolds = tiles.filter { it.tile == Tile.SCAFFOLD }
    return scaffolds.filter {
        scaffolds.any { c -> c.x - 1 == it.x && c.y == it.y } &&
        scaffolds.any { c -> c.x + 1 == it.x && c.y == it.y } &&
        scaffolds.any { c -> c.y - 1 == it.y && c.x == it.x } &&
        scaffolds.any { c -> c.y + 1 == it.y && c.x == it.x }
    }
}

enum class Tile { SCAFFOLD, SPACE, DROID}

data class Point(var x: Long, var y: Long, var tile: Tile)

fun pointsAreNeighbour(p1: Point, p2: Point): Boolean{
    if(p1.y == p2.y){
        return p1.x - 1 == p2.x || p1.x + 1 == p2.x
    }
    if(p1.x == p2.x){
        return p1.y - 1 == p2.y || p1.y + 1 == p2.y
    }
    return false
}