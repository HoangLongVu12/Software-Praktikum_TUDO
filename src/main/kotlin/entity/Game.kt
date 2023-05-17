package entity


/**
 *  Entity class represents a game state of "Swim".
 *  @param player It contains all the Information of players
 *  @param drawCards The cards from Drawstack
 *  @param middleCards The cards in the middle
 *  @param currentPlayer Index of current player
 *  @param hasEnded check if the game has ended
 *  @param afterKnocking number of player after someone has knocked
 *  @constructor Create empty Game
 */
class Game {
    var players: MutableList<Player> = emptyList<Player>().toMutableList()
    var drawCards: MutableList<Card> = emptyList<Card>().toMutableList()
    var middleCards: MutableList<Card> = emptyList<Card>().toMutableList()
    var currentPlayer: Int = 0
    var hasEnded: Boolean = false
    var hasKnocked: Boolean = false
    var afterKnocking: Int = 0
    var indexChosenCard = -1
    constructor(players: MutableList<Player>, drawCards: MutableList<Card>, middleCards: MutableList<Card>) {
        if (players.size < 2 || players.size > 4) throw IllegalArgumentException("Game can be played only by 2-4 players")
        this.players = players
        this.middleCards = middleCards
        this.drawCards = drawCards
        currentPlayer = 0
        hasEnded = false
        hasKnocked = false
        afterKnocking = 0
        indexChosenCard = -1
    }
}