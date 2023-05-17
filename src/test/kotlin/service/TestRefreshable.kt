package service

import entity.Player
import view.Refreshable
/**
 * [Refreshable] implementation that refreshes nothing, but remembers
 * if a refresh method has been called (since last [reset])
 */

class TestRefreshable : Refreshable {
    var refreshAfterEndGameCalled : Boolean = false
        private set
    var refreshAfterSwapCardCalled : Boolean = false
        private set
    var refreshAfterChangeTurnCalled : Boolean = false
        private set
    var refreshAfterStartGameCalled : Boolean = false
        private set
    var refreshAfterPlayerKnockCalled : Boolean = false
        private set
    /**
     * resets all *Called properties to false
     */
    fun reset(){
        refreshAfterEndGameCalled = false;
        refreshAfterSwapCardCalled = false;
        refreshAfterChangeTurnCalled = false;
        refreshAfterStartGameCalled = false;
        refreshAfterPlayerKnockCalled = false;
    }

    override fun refreshAfterEndGame() {
        refreshAfterEndGameCalled = true
    }

    override fun refreshAfterSwapCard(player: Player) {
        refreshAfterSwapCardCalled = true
    }

    override fun refreshAfterChangeTurn() {
        refreshAfterChangeTurnCalled = true
    }

    override fun refreshAfterPlayerKnock(player: Player) {
        refreshAfterPlayerKnockCalled = true
    }

    override fun refreshAfterStartGame() {
        refreshAfterStartGameCalled = true;
    }
}