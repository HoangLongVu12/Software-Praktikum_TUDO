package service

import entity.*

/**
 * Game service
 *
 * @property rootService
 * @constructor Create empty Game service
 */
class GameService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * Set up and start new game, in which List of Players is initialized and Cards are distributed in separate stacks
     *
     * @param playerNames List of entered player names
     * @param allCards List of 32 shuffled cards to play with
     */
    fun startGame( playerNames: MutableList<String>) {
        require(playerNames.size in 2..4){ "INVALID TOTAL PLAYER!!!"}

        val drawCards = createDrawStack()
        val middleCards = createStackOf3Cards(drawCards)
        val players: MutableList<Player> = mutableListOf()
        for(name in playerNames){
            players.add(Player(name,createStackOf3Cards(drawCards)))
        }
        val game = Game(players.toMutableList(),drawCards,middleCards)
        rootService.currentGame = game
        game.currentPlayer = 0
        game.players[game.currentPlayer].onTurn == false

        onAllRefreshables { refreshAfterStartGame() }
    }
    /**
     * Creates a shuffled 32 cards list of all four suits and cards
     * from 7 to Ace
     * @return a mutable list of 32 PlayCard object
     */
    fun createDrawStack(): MutableList<Card> {
        val drawStack: MutableList<Card> = mutableListOf()
        val cardValue = mutableListOf(
            CardValue.SEVEN,
            CardValue.EIGHT,
            CardValue.NINE,
            CardValue.TEN,
            CardValue.JACK,
            CardValue.QUEEN,
            CardValue.KING,
            CardValue.ACE)
        for (suitValue in CardSuit.values()) {
            for (valueValue in cardValue) {
                drawStack.add(Card(suitValue, valueValue))
            }
        }
        return drawStack.shuffled() as MutableList<Card>
    }
    /**
     * draw 3 cards from the draw stack for one player or for the middle stack
     *
     * @param drawStack: draw from this stack
     * @return a mutable list of 3 Playcard
     */
     fun createStackOf3Cards(drawStack: MutableList<Card>): MutableList<Card> {
        val temp: MutableList<Card> = mutableListOf()
        for(i in 0..2){
            temp.add(i,drawStack[0])
            drawStack.removeAt(0)
        }
        return temp
    }

    /** set onTurn of current player to false
     *  change the turn to next player
     *  set onTurn of next player to true
     */
    fun changeTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently running." }
        game.currentPlayer = (game.currentPlayer + 1) % game.players.size
        game.players[game.currentPlayer].onTurn = true

        onAllRefreshables { refreshAfterChangeTurn() }
    }
    /**
     * set hasEnded of current game to true
     * end game and show the sorted list of player in GUI
     * @return sorted list of player
     */
    fun endGame():MutableList<Player> {
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently running." }
        game.hasEnded = true
        var finalListOfPlayer = game.players.sortedByDescending {player: Player -> player.score }.toMutableList()
        onAllRefreshables { this.refreshAfterEndGame() }
        return finalListOfPlayer
    }
    /**
     * check the Status of current Game
     * if the current player is not the last player after knocking, increase afterKnocking
     * if afterKnocking equals the nummber of player minus 1 or hasEnded equals true or there is on 1,2 card from drawstack
     * then the current game shoulb be ended
     */
    fun checkStatusOfGame(){
        val game = rootService.currentGame
        checkNotNull(game) { "No game currently running."}
        if(game.hasKnocked){
            game.afterKnocking++
        }
        if (game.afterKnocking == game.players.size-1 || game.drawCards.size < 3){
            endGame()
        }
    }






}