package service

import entity.*

/**
 * TODO manage all the players action
 * in all methods of player actions we should check whether someone has knocked, if the current player is the last one
 * after knocking that means afterKnocking equals 3, then the current game should be ended
 *
 * @property rootService
 */

class PlayerService(private val rootService: RootService) : AbstractRefreshingService() {

    private val gameService = rootService.gameService

    /**
     * calculate score of current player and is called after current player's cards were updated
     *
     * If 3 cards have the same suit, current player gets 30.5 points
     * Otherwise if current player has only 2 cards with the same suit, player's score must be the maximum points of 2 cards that
     * have the same suit and the rest card
     * Otherwise if current player has 3 cards with 3 different suits, player's score is the sum of 3 card values
     *
     * @return calculated points of current player
     */
    fun calculateScore(player: Player) : Double {
        var game = rootService.currentGame
        checkNotNull(game) { "No game is currently running." }
        var card0 = player.playerCards[0]
        var card1 = player.playerCards[1]
        var card2 = player.playerCards[2]
        var sum : Double = 0.0

        if(card0.equalsValue(card1) && card0.equalsValue(card2)){
            return 30.5
        }

        if(card0.equalsSuit(card1) && card0.equalsSuit(card2)) {
            return card0.toDoubleValue() + card1.toDoubleValue() + card2.toDoubleValue()
        }

        if(card0.equalsSuit(card1)){
            sum = card0.toDoubleValue() + card1.toDoubleValue()
            return maxOf(sum, card2.toDoubleValue())
        }

        if(card0.equalsSuit(card2)){
            sum = card0.toDoubleValue() + card2.toDoubleValue()
            return maxOf(sum, card1.toDoubleValue())
        }

        if(card1.equalsSuit(card2)){
            sum = card1.toDoubleValue() + card2.toDoubleValue()
            return maxOf(sum, card0.toDoubleValue())
        }

        return maxOf(card0.toDoubleValue(), card1.toDoubleValue(), card2.toDoubleValue())
    }

    /**
     * if noone has knocked then set hasKnocked to True
     * if someone has knocked and now is the turn of the next player, increase afterKnocking
     * if that is already the last player after one player knocked, end current game
     *
     */
    fun knock() {
        var game = rootService.currentGame
        checkNotNull(game) { "No game is currently running." }
        var currentPlayer = game.players[game.currentPlayer]
        game.indexChosenCard = -1
        currentPlayer.indexChosenCard = - 1
        game.hasKnocked = true
        onAllRefreshables {refreshAfterPlayerKnock(currentPlayer)}
        rootService.gameService.changeTurn()
        if (rootService.currentGame!!.hasEnded) onAllRefreshables { refreshAfterEndGame() }
    }
    /**
     * current player just passes
     * as long as the next player has not passed so the current player is not the last player after someone has passed
     */
    fun pass(){
        val game = rootService.currentGame
        checkNotNull(game) { "No game currently running."}
        val player = game.players[game.currentPlayer]
        val nextPlayer = game.players[(game.currentPlayer + 1) % game.players.size]
        game.indexChosenCard = -1
        player.indexChosenCard = - 1
        if(nextPlayer.hasPassed == true) {
                game.middleCards = gameService.createStackOf3Cards(game.drawCards).toMutableList()
                for (i in game.players) {
                    i.hasPassed = false
                }
                onAllRefreshables { refreshAfterSwapCard(game.players[game.currentPlayer]) }
                rootService.gameService.changeTurn()
        } else {
            player.hasPassed = true
            rootService.gameService.changeTurn()
        }
        rootService.gameService.checkStatusOfGame()

    }



    /**
     * Check if current player is valid and swap one current player's card with one in the middle stack
     *
     * @param playerCardIndex current player's card index that is chosen to exchange
     * @param openCardIndex card in middle stack that is chosen to exchange
     */
    fun swapOne() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game currently running."}
        for (i in game.players) {
            i.hasPassed = false
        }
        val currentPlayer = game.players[game.currentPlayer]
        // swap cards
        val tempCard : Card = currentPlayer.playerCards[currentPlayer.indexChosenCard]
        currentPlayer.playerCards[currentPlayer.indexChosenCard] = game.middleCards[game.indexChosenCard]
        game.middleCards[game.indexChosenCard] = tempCard
        onAllRefreshables {refreshAfterSwapCard(game.players[game.currentPlayer])}
        rootService.gameService.changeTurn()
        rootService.gameService.checkStatusOfGame()
    }

    /**
     * Check if current player is valid and exchange all current player's cards with cards in the middle stack
     *
     */
    fun swapAll() {
        var game = rootService.currentGame
        var currentPlayer = game!!.players[game.currentPlayer]
        // reset index of the chosen card
        game.indexChosenCard = -1
        game.players[game.currentPlayer].indexChosenCard = - 1
        for (i in game.players) {
            i.hasPassed = false
        }
        //swap all card
        for (cardIndex in 0..2) {
            val temp : Card = currentPlayer.playerCards[cardIndex]
            currentPlayer.playerCards[cardIndex] = game.middleCards[cardIndex]
            game.middleCards[cardIndex] = temp
        }

        onAllRefreshables {refreshAfterSwapCard(game.players[game.currentPlayer])}
        rootService.gameService.changeTurn()
        rootService.gameService.checkStatusOfGame()
    }

}
