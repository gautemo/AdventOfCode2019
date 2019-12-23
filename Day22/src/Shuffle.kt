import java.io.File
import kotlin.math.absoluteValue

fun main(){
    val commands = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText().trim()

    val deck = Deck(10007)
    deck.shuffle(commands)
    println(deck.cards.indexOf(2019))

    val bigDeck = Deck(119315717514047)
    for(i in 0 until 101741582076661){
        deck.shuffle(commands)
    }
    println(deck.cards[2020])
}

class Deck(size: Long){
    val cards = (0 until size).toMutableList()

    fun shuffle(commands: String) {
        for(c in commands.lines()){
            when{
                c == "deal into new stack" -> dealIntoNewStack()
                c.contains("cut") -> cut(c.split(' ')[1].toInt())
                c.contains("increment") -> dealWithIncrement(c.split(' ').last().toInt())
            }
        }
    }

    private fun dealIntoNewStack(){
        cards.reverse()
    }

    private fun cut(n: Int){
        if(n < 0){
            val move = cards.takeLast(n.absoluteValue)
            cards.removeAll(move)
            cards.addAll(0, move)
        }else {
            val move = cards.take(n)
            cards.removeAll(move)
            cards.addAll(move)
        }
    }

    private fun dealWithIncrement(n: Int){
        val copy = cards.toList()
        copy.forEachIndexed { index, card ->
            val placeAt = (index * n) % cards.size
            cards[placeAt] = card
        }
    }
}