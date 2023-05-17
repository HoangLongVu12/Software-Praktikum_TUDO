package service

import entity.*
import kotlin.test.*

/**
 * Service test
 *
 * @constructor Create empty Service test
 */
class ServiceTest {

    private fun setUpGame(/*vararg refreshables: Refreshable*/): RootService {
        val rootService = RootService()
        assertNull(rootService.currentGame)
        val playerCards1 = mutableListOf<Card>(
            Card(CardSuit.CLUBS, CardValue.QUEEN), /*Card(CardSuit.SPADES, CardValue.SEVEN),*/
            Card(CardSuit.SPADES, CardValue.TEN),
            Card(CardSuit.DIAMONDS, CardValue.SEVEN)
        )
        val player1 = Player("Bob", playerCards1)

        val playerCards2 = mutableListOf<Card>(
            Card(CardSuit.CLUBS, CardValue.EIGHT),
            Card(CardSuit.CLUBS, CardValue.NINE),
            Card(CardSuit.HEARTS, CardValue.KING),
            )
        val player2 = Player("Alice", playerCards2)

        val playerCards3 = mutableListOf<Card>(
            Card(CardSuit.DIAMONDS, CardValue.QUEEN),
            Card(CardSuit.SPADES, CardValue.QUEEN),
            Card(CardSuit.DIAMONDS, CardValue.JACK),
        )
        val player3 = Player("Ana", playerCards3)

        val players = mutableListOf<Player>(player1, player2, player3)

        val middleCards = mutableListOf<Card>(
            Card(CardSuit.SPADES, CardValue.SEVEN),
            Card(CardSuit.DIAMONDS, CardValue.KING),
            Card(CardSuit.DIAMONDS, CardValue.NINE),

            )

        val drawCards = mutableListOf<Card>(
            Card(CardSuit.SPADES, CardValue.EIGHT),
            Card(CardSuit.HEARTS, CardValue.TEN),
            Card(CardSuit.HEARTS, CardValue.EIGHT),

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
            Card(CardSuit.HEARTS, CardValue.NINE),
        )

        rootService.currentGame = Game(players, drawCards, middleCards)
        return rootService

    }
    /**
     * Test calculate score
     *
     */
    @Test
    fun testCalculateScore(){
        val game = setUpGame()
        println(game.currentGame!!.players[0].playerCards)
        val player = game.currentGame!!.players[game.currentGame!!.currentPlayer]
        assertEquals(game.playerService.calculateScore(player),10.0)
    }

    /**
     * Test start game
     *
     */
    @Test
    fun testStartGame() {
        val game = setUpGame().currentGame!!

        println(game.players[game.currentPlayer].playerCards)
        println(game.middleCards.size)
        println(game.drawCards.size)

        assertEquals("Alice", game.players[1].name)
        assertEquals(3, game.middleCards.size)
        assertEquals(20, game.drawCards.size)

    }


    /**
     * Test swap one card
     *
     */
    @Test
    fun testSwapOneCard() {
        val rootService = setUpGame()
        val playerService = PlayerService(rootService)
        val game = rootService.currentGame!!
        game.indexChosenCard = 1
        game.players[game.currentPlayer].indexChosenCard=1
        val cardOfCurrentPlayerBeforeSwap = game.players[0].playerCards[1]
        val cardOfMiddleCardsBeforeSwap = game.middleCards[1]
        //currentPlayer: Bob
        playerService.swapOne()
        assertEquals(cardOfMiddleCardsBeforeSwap, game.players[0].playerCards[1])
        assertEquals(cardOfCurrentPlayerBeforeSwap, game.middleCards[1])
    }

    /**
     * Test swap all cards
     *
     */
    @Test
    fun testSwapAll() {
        val rootService = setUpGame()
        val playerService = PlayerService(rootService)
        val game = rootService.currentGame!!
        //currentPlayer: Bob
        playerService.swapAll()
        assertEquals(game.players[0].playerCards[0],Card(CardSuit.SPADES, CardValue.SEVEN))
        assertEquals(game.players[0].playerCards[1],Card(CardSuit.DIAMONDS, CardValue.KING))
        assertEquals(game.players[0].playerCards[2],Card(CardSuit.DIAMONDS, CardValue.NINE))
        assertEquals(game.middleCards[0],Card(CardSuit.CLUBS, CardValue.QUEEN))
        assertEquals(game.middleCards[1],Card(CardSuit.SPADES, CardValue.TEN))
        assertEquals(game.middleCards[2],Card(CardSuit.DIAMONDS, CardValue.SEVEN))
    }
    @Test
    fun testFailStartWithOnlyOnePlayer(){
        val mc = RootService()

        // there is no player
        assertFails {
            mc.gameService.startGame(mutableListOf())
        }
        // there is one player
        assertFails {
            mc.gameService.startGame(mutableListOf("A"))
        }
        // as the previous attempt failed, there should be no game started...
        assertNull(mc.currentGame)


    }

    /**
     * Test next player
     *
     */
    @Test
    fun testChangeTurn(){
        val rootService = setUpGame()
        val playerService = PlayerService(rootService)
        val gameService = GameService(rootService)
        val game = rootService.currentGame!!
        //Bob
        gameService.changeTurn()
        //Alice
        assertEquals(true,game.players[game.currentPlayer].onTurn)
        assertEquals("Alice",game.players[game.currentPlayer].name)
    }

    /**
     * Test pass
     *
     */
    @Test
    fun testPass() {
        val rootService = setUpGame()
        val playerService = PlayerService(rootService)
        val game = rootService.currentGame!!
        val middleCardBeforePass = rootService.currentGame!!.middleCards
        val drawCardsBeforePass = rootService.currentGame!!.drawCards
        playerService.pass()
        assertEquals(true,game.players[0].hasPassed)
        assertEquals(middleCardBeforePass, game.middleCards)
        assertEquals(drawCardsBeforePass, game.drawCards)

        //test pass -> endgame
    }

    /**
     * Test knock
     *
     */
    @Test
    fun testKnock(){
        val rootService = setUpGame()
        val game = rootService.currentGame!!

        rootService.playerService.knock()
        assertEquals(true, game.hasKnocked)
        rootService.playerService.pass()
        rootService.playerService.pass()
        assertEquals(true,game.hasEnded)

    }

    /**
     * Test end game
     *
     */
    @Test
    fun testEndGame(){
        val rootService = setUpGame()
        val playerService = PlayerService(rootService)
        val gameService = GameService(rootService)
        val game = rootService.currentGame!!

        playerService.pass()
        gameService.changeTurn()
        playerService.pass()
        gameService.changeTurn()
        playerService.pass()

        val end = gameService.endGame() // list of player
        end.forEach{player -> println(player.name + player.score)}
    }

}