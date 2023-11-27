package service

import entity.Card
import entity.Player
import view.Refreshable

/**
 * [Refreshable] implementation that refreshes nothing, but remembers
 * if a refresh method has been called (since last [reset])
 */
class TestRefreshable: Refreshable {

    var refreshAfterStartNewGameCalled: Boolean = false
    var refreshAfterGameEndCalled: Boolean = false
    var refreshAfterRemovePairCalled: Boolean = false
    var refreshAfterRevealCardCalled: Boolean = false
    var refreshAfterPassCalled: Boolean = false

    // This would be called when a new game starts
    override fun refreshAfterStartNewGame() {
        refreshAfterStartNewGameCalled = true
    }

    // This would be called when the game ends
    override fun refreshAfterGameEnd() {
        refreshAfterGameEndCalled = true
    }

    // This could be called after a pair is successfully removed from the pyramid
    override fun refreshAfterRemovePair(player: Player, cards: List<Card>) {
        refreshAfterRemovePairCalled = true
    }

    // This could be called when a new card is revealed from the draw pile
    override fun refreshAfterRevealCard(player: Player, card: Card) {
        refreshAfterRevealCardCalled = true
    }

    // This would be called when a player passes their turn
    override fun refreshAfterPass(player: Player) {
        refreshAfterPassCalled = true
    }

    /**
     * resets all *Called properties to false
     */
    fun reset() {
        refreshAfterStartNewGameCalled = false
        refreshAfterGameEndCalled = false
        refreshAfterRemovePairCalled = false
        refreshAfterRevealCardCalled = false
        refreshAfterPassCalled = false
    }
}
