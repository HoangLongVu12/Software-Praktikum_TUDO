package entity

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Test cases for [Player]
 *
 */

class PlayerTest {
    private val cards = mutableListOf<Card>(
        Card(CardSuit.CLUBS, CardValue.QUEEN),
        Card(CardSuit.SPADES, CardValue.TEN),
        Card(CardSuit.DIAMONDS, CardValue.SEVEN)
    )

    private val player = Player("Alice", cards)

    /**
     * Test name of player
     *
     */
    @Test
    fun testName(){
        assertEquals("Alice", player.name)
    }

    /**
     * Test score of player
     *
     */
    @Test
    fun testScore(){
        assertEquals(0.0, player.score)
    }

    /**
     * Test hasPassed of player
     *
     */
    @Test
    fun testHasPassed(){
        assertEquals(false, player.hasPassed)
    }

    /**
     * Test onTurn of player
     *
     */
    @Test
    fun testOnTurn(){
        assertEquals(false, player.onTurn)
    }

    /**
     * Test cards in hand of player
     *
     */
    @Test
    fun testCardsInHand(){
        assertEquals(Card(CardSuit.CLUBS, CardValue.QUEEN), player.playerCards[0])
        assertEquals(Card(CardSuit.SPADES, CardValue.TEN), player.playerCards[1])
        assertEquals(Card(CardSuit.DIAMONDS, CardValue.SEVEN), player.playerCards[2])
    }





}