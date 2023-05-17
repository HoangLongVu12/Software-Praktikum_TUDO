package entity

import kotlin.test.*

/**
 * Test cases for [Game]
 *
 * @constructor Create empty Game test
 */
class GameTest {
    private val playerCards1 = mutableListOf<Card>(
        Card(CardSuit.CLUBS, CardValue.QUEEN),
        Card(CardSuit.SPADES, CardValue.TEN),
        Card(CardSuit.DIAMONDS, CardValue.SEVEN),
    )
    private val player1 = Player("Bob", playerCards1)

    private val playerCards2 = mutableListOf<Card>(
        Card(CardSuit.CLUBS, CardValue.EIGHT),
        Card(CardSuit.CLUBS, CardValue.NINE),
        Card(CardSuit.HEARTS, CardValue.KING),
    )
    private val player2 = Player("Alice", playerCards2)

    private val playerCards3 = mutableListOf<Card>(
        Card(CardSuit.DIAMONDS, CardValue.QUEEN),
        Card(CardSuit.SPADES, CardValue.QUEEN),
        Card(CardSuit.DIAMONDS, CardValue.JACK),
    )
    private val player3 = Player("Ana", playerCards3)

    private val players = mutableListOf<Player>(player1, player2, player3)

    private val middleCards = mutableListOf<Card>(
        Card(CardSuit.SPADES, CardValue.SEVEN),
        Card(CardSuit.DIAMONDS, CardValue.KING),
        Card(CardSuit.DIAMONDS, CardValue.NINE),
    )

    private val drawCards = mutableListOf<Card>(
        Card(CardSuit.CLUBS, CardValue.JACK),
        Card(CardSuit.HEARTS, CardValue.ACE),
        Card(CardSuit.SPADES, CardValue.NINE),

        Card(CardSuit.CLUBS, CardValue.ACE),
        Card(CardSuit.SPADES, CardValue.JACK),
        Card(CardSuit.HEARTS, CardValue.SEVEN),

        Card(CardSuit.CLUBS, CardValue.SEVEN),
        Card(CardSuit.CLUBS, CardValue.KING),
        Card(CardSuit.DIAMONDS, CardValue.EIGHT),

        Card(CardSuit.CLUBS, CardValue.TEN),
        Card(CardSuit.DIAMONDS, CardValue.TEN),
        Card(CardSuit.HEARTS, CardValue.JACK),

        Card(CardSuit.SPADES, CardValue.KING),
        Card(CardSuit.DIAMONDS, CardValue.ACE),
        Card(CardSuit.HEARTS, CardValue.QUEEN),

        Card(CardSuit.SPADES, CardValue.ACE),
        Card(CardSuit.HEARTS, CardValue.NINE)
    )

    private val game = Game(players, drawCards, middleCards)

    /**
     * Test get players
     *
     */
    @Test
    fun testGetPlayers(){
        assertEquals(mutableListOf(player1, player2, player3), game.players)
    }

    /**
     * Test get current player
     *
     */
    @Test
    fun testGetCurrentPlayer(){
        assertEquals(players.indexOf(player1), game.currentPlayer)
    }


    /**
     * Test get has Ended
     *
     */
    @Test
    fun testgethasEnded(){
        assertEquals(false, game.hasEnded)
    }

    /**
     * Test after Knocking
     *
     */
    @Test
    fun testIncreasePassCounter(){
        assertEquals(0, game.afterKnocking)
    }

    /**
     * Test get drawcards
     *
     */
    @Test
    fun testGetDrawCards(){
        assertEquals(drawCards, game.drawCards)
    }

    /**
     * Test get middlecards
     *
     */
    @Test
    fun testGetMiddleCards(){
        assertEquals(middleCards, game.middleCards)
    }
}

