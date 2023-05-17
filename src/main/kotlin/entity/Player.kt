package entity

/**
 * Entity to represent a player in the game "Swim".
 * @param name name of player
 * @param score score that the player gets
 * @param playerCards The cards on the hand of the player
 * @param hasPassed check if the game has passed
 * @param onTurn check if the player is on his turn
 * @constructor Create empty Player
 */

class Player {
    var name: String = ""
    var score: Double = 0.0
    var hasPassed: Boolean = false
    var onTurn: Boolean = false
    var indexChosenCard = -1
    var playerCards: MutableList<Card> = emptyList<Card>().toMutableList()

    constructor(name: String, handCards: MutableList<Card>) {
        if (name.isEmpty()) throw IllegalArgumentException("Player's name must be entered")

        this.name = name
        this.playerCards = handCards
        this.indexChosenCard = -1
    }



}