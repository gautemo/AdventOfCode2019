import java.io.File
import java.math.BigInteger
import kotlin.math.absoluteValue

fun main(){
    val commands = File(Thread.currentThread().contextClassLoader.getResource("input.txt")!!.toURI()).readText().trim()

    val deck = Deck(10007)
    deck.shuffle(commands)
    println(deck.cards.indexOf(2019))

    solvePart2(commands)
}

class Deck(size: Int){
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

fun solvePart2(commands: String){
    val size = 119315717514047.toBigInteger()
    val iterations = 101741582076661.toBigInteger()
    val checkPos = 2020

    var increment = 1.toBigInteger()
    var offsetDifference = 0.toBigInteger()

    for(c in commands.lines()){
        val pair = p2(increment, offsetDifference, size, c)
        increment = pair.first
        offsetDifference = pair.second
    }

    val pair = getSeq(iterations, increment, offsetDifference, size)
    increment = pair.first
    offsetDifference = pair.second

    val card = getCard(increment, offsetDifference, checkPos, size)
    println(card)
}

fun getCard(increment: BigInteger, offsetDifference: BigInteger, checkPos: Int, size: BigInteger) = (offsetDifference + checkPos.toBigInteger() * increment) % size

fun p2(increment: BigInteger, offsetDifference: BigInteger, size: BigInteger, c: String): Pair<BigInteger, BigInteger> {
    var offset = offsetDifference
    var inc = increment
    when{
        c == "deal into new stack" -> { inc *= (-1).toBigInteger(); offset += inc }
        c.contains("cut") -> offset += c.split(' ')[1].toBigInteger() * increment
        c.contains("increment") -> inc *= c.split(' ').last().toBigInteger().modInverse(size)
    }
    return Pair(inc % size, offset % size)
}

fun getSeq(iterations: BigInteger, increment: BigInteger, offsetDifference: BigInteger, size: BigInteger): Pair<BigInteger, BigInteger>{
    val inc = increment.modPow(iterations, size)
    var offset = offsetDifference * ( 1.toBigInteger() - inc) * ((1.toBigInteger() - increment) % size).modInverse(size)
    offset %= size
    return Pair(inc, offset)

}
