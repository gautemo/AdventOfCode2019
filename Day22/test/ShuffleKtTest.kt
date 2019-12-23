import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ShuffleKtTest {

    @Test
    fun `7 at the end`() {
        val deck = Deck(10)
        val commands = """
            deal with increment 7
            deal into new stack
            deal into new stack
        """.trimIndent()
        deck.shuffle(commands)
        assertEquals("0 3 6 9 2 5 8 1 4 7", deck.cards.joinToString(" "))
    }

    @Test
    fun `6 at the end`() {
        val deck = Deck(10)
        val commands = """
            cut 6
            deal with increment 7
            deal into new stack
        """.trimIndent()
        deck.shuffle(commands)
        assertEquals("3 0 7 4 1 8 5 2 9 6", deck.cards.joinToString(" "))
    }

    @Test
    fun `9 at the end`() {
        val deck = Deck(10)
        val commands = """
            deal with increment 7
            deal with increment 9
            cut -2
        """.trimIndent()
        deck.shuffle(commands)
        assertEquals("6 3 0 7 4 1 8 5 2 9", deck.cards.joinToString(" "))
    }

    @Test
    fun `9 first and 6 at the end`() {
        val deck = Deck(10)
        val commands = """
            deal into new stack
            cut -2
            deal with increment 7
            cut 8
            cut -4
            deal with increment 7
            cut 3
            deal with increment 9
            deal with increment 3
            cut -1
        """.trimIndent()
        deck.shuffle(commands)
        assertEquals("9 2 5 8 1 4 7 0 3 6", deck.cards.joinToString(" "))
    }
}